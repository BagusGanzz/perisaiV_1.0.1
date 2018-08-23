package com.studioninja.locker.lock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import com.studioninja.locker.util.Log;

import com.studioninja.locker.LockerAnalytics;
import com.studioninja.locker.ui.MainActivity;
import com.studioninja.locker.util.PrefUtils;
import com.studioninja.util.Analytics;
import com.studioninja.locker.R;


public class AppLockService extends Service {

	/**
	 * Sent to {@link MainActivity} when the service has been completely started
	 * and is running
	 */
	public static final String BROADCAST_SERVICE_STARTED = "com.monika.locker.intent.action.service_started";
	/**
	 * Sent to {@link MainActivity} when the service has been stopped
	 */
	public static final String BROADCAST_SERVICE_STOPPED = "com.monika.locker.intent.action.service_stopped";
	/**
	 * This category allows the receiver to receive actions relating to the
	 * state of the service, such as when it is started or stopped
	 */
	public static final String CATEGORY_STATE_EVENTS = "com.monika.locker.intent.category.service_start_stop_event";

	private static final int REQUEST_CODE = 0x1234AF;
	public static final int NOTIFICATION_ID = 0xABCD32;
	private static final String TAG = "AppLockService";

	/** Use this action to stop the intent */
	private static final String ACTION_STOP = "com.monika.locker.intent.action.stop_lock_service";
	/** Starts the alarm */
	public static final String ACTION_START = "com.monika.locker.intent.action.start_lock_service";
	/**
	 * When specifying this action, the service will initialize everything
	 * again.<br>
	 * This has only effect if the service was explicitly started using
	 * {@link #getRunIntent(Context)}
	 */
	public static final String ACTION_RESTART = "com.monika.locker.intent.action.restart_lock_service";

	private static final String EXTRA_FORCE_RESTART = "com.monika.locker.intent.extra.force_restart";
	private ActivityManager mActivityManager;

	// private AdMobInterstitialHelper mInterstitialHelper;

	/** 0 for disabled */
	private long mShortExitMillis;

	private boolean mRelockScreenOff;
	private boolean mShowNotification;

	private boolean mExplicitStarted;
	private boolean mAllowDestroy;
	private boolean mAllowRestart;
	private Handler mHandler;
	private BroadcastReceiver mScreenReceiver;
	private GetPackageTask appTask;

	/**
	 * This map contains locked apps in the form<br>
	 * <PackageName, ShortExitEndTime>
	 */
	private Map<String, Boolean> mLockedPackages;
	private Map<String, Runnable> mUnlockMap;




	@Override
	public IBinder onBind(Intent i) {
		return new LocalBinder();
	}

	public class LocalBinder extends Binder {
		public AppLockService getInstance() {
			return AppLockService.this;
		}
	}

	private final class ScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				Log.i(TAG, "Screen ON");
				// Trigger package again
				mLastPackageName = "";
				startAlarm(AppLockService.this);
			}
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				Log.i(TAG, "Screen OFF");
				stopAlarm(AppLockService.this);
				if (mRelockScreenOff) {
					lockAll();
				}
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");

	}

	/**
	 * Starts everything, including notification and repeating alarm
	 * 
	 * @return True if all OK, false if the service is not allowed to start (the
	 *         caller should stop the service)
	 */
	private boolean init() {
		Log.d(TAG, "init");
		if (new PrefUtils(this).isCurrentPasswordEmpty()) {
			Log.w(TAG, "Not starting service, current password empty");
			return false;
		}
		// if (new VersionManager(this).isDeprecated()) {
		// Log.i(TAG, "Not starting AlarmService for deprecated version");
		// new VersionUtils(this).showDeprecatedNotification();
		// return false;
		// }

		// mInterstitialHelper = new AdMobInterstitialHelper(this, new
		// LockerAdInterface());
		mHandler = new Handler();
		mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		mUnlockMap = new HashMap<String, Runnable>();
		mLockedPackages = new HashMap<String, Boolean>();
		mScreenReceiver = new ScreenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mScreenReceiver, filter);

		final Set<String> apps = PrefUtils.getLockedApps(this);
		for (String s : apps) {
			mLockedPackages.put(s, true);
		}
		PrefUtils prefs = new PrefUtils(this);
		boolean delay = prefs.getBoolean(R.string.pref_key_delay_status, R.bool.pref_def_delay_status);

		if (delay) {
			int secs = prefs.parseInt(R.string.pref_key_delay_time, R.string.pref_def_delay_time);
			mShortExitMillis = secs * 1000;
		}

		mRelockScreenOff = prefs.getBoolean(R.string.pref_key_relock_after_screenoff, R.bool.pref_def_relock_after_screenoff);

		startNotification();
		startAlarm(this);

		// Tell MainActivityantivirus we're done
		Intent i = new Intent(BROADCAST_SERVICE_STARTED);
		i.addCategory(CATEGORY_STATE_EVENTS);
		sendBroadcast(i);
		return true;
	}





	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 Log.d(TAG, "test :onStartCommand****");
		//setRepeatForAdroidLolipop();
		if (intent == null || ACTION_START.equals(intent.getAction())) {
			Log.d(TAG, "intent = null****");
			if (!mExplicitStarted) {
				Log.d(TAG, "explicitStarted = false");
				if (init() == false) {
					doStopSelf();
					return START_NOT_STICKY;
				}
				mExplicitStarted = true;
			}
			checkPackageChanged();
		} else if (ACTION_RESTART.equals(intent.getAction())) {
			Log.d(TAG, "intent action = ACTION_RESTART****");
			if (mExplicitStarted || intent.getBooleanExtra(EXTRA_FORCE_RESTART, false)) {
				Log.d(TAG, "ACTION_RESTART (force=" + intent.getBooleanExtra(EXTRA_FORCE_RESTART, false));
				// init();
				doRestartSelf(); // not allowed, so service will restart
			} else {
				doStopSelf();
			}
		} else if (ACTION_STOP.equals(intent.getAction())) {
			Log.d(TAG, "ACTION_STOP");
			doStopSelf();
		}

		return START_STICKY;
	}

	private  String mLastPackageName;




	private String printForegroundTask() {
		String currentApp = "NULL";
		Log.d(TAG, "printForegroundTask****");
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			try {
				UsageStatsManager usm = (UsageStatsManager) this.getSystemService("usagestats");
				long time = System.currentTimeMillis();
				List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
				if (appList != null && appList.size() > 0) {
					SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
					for (UsageStats usageStats : appList) {
						mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
					}
					if (mySortedMap != null && !mySortedMap.isEmpty()) {
						currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		} else if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {//

			List<ActivityManager.RunningAppProcessInfo> tasks = mActivityManager.getRunningAppProcesses();
			currentApp = tasks.get(0).processName;
		}else{
			final RunningTaskInfo top = getTopTask();
			currentApp =  top.topActivity.getPackageName();
		}

		Log.e("adapter", "Current App in foreground is: " + currentApp);
		return currentApp;
	}



	private class GetPackageTask extends AsyncTask<Void,Void,String>{
		final Handler handler = new Handler();


		@Override
		protected String doInBackground(Void... params) {
			Log.d(TAG, "doInBackground****");
			return printForegroundTask();
		}

		@Override
		protected void onPostExecute(String  packageName) {
			Log.d(TAG, "onPostExecute****");
			super.onPostExecute(packageName);
			Log.d(TAG, "packeage top :" + packageName + " ***********");
			Log.d(TAG, "mLastPackageName top :" + mLastPackageName + " ***********");
			if (!packageName.equals(mLastPackageName)) {

				Log.d(TAG, "packeage name:" +mLastPackageName +" ***********");
				onAppClose(mLastPackageName, packageName);
				onAppOpen(packageName, mLastPackageName);
			}
			appTask = null;
			// prepare for next call
			mLastPackageName = packageName;

			if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						sendBroadcast(new Intent("action.check.lollipop"));
					}
				}, 200);
			}
		}
	}




/*fdsfsda*/
	// private String mLastCompleteName;



	private void checkPackageChanged() {

		Log.d(TAG, "checkPackageChanged****");

		if(appTask == null  ) {
			appTask = new GetPackageTask();

		}
		if(appTask.getStatus() != AsyncTask.Status.RUNNING ) {
			appTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}

		Log.d(TAG, "task done****");


	}

	private void onAppOpen(final String open, final String close) {
		Log.d(TAG, "onAppOpen ");
		if (mLockedPackages.containsKey(open)) {
			onLockedAppOpen(open, close);
		}
	}

	private void onLockedAppOpen(final String open, final String close) {
		final boolean locked = mLockedPackages.get(open);
		Log.d(TAG, "onLockedAppOpen (locked=" + locked + ")");
		if (locked) {
			showLocker(open);
		}
		removeRelockTimer(open);

	}

	private void showLocker(String packageName) {
		Intent intent = LockService.getLockIntent(getBaseContext(), packageName); // chenged from this
		intent.setAction(LockService.ACTION_COMPARE);
		intent.putExtra(LockService.EXTRA_PACKAGENAME, packageName);
		startService(intent);

	}

	private void onAppClose(String close, String open) {
		if (mLockedPackages.containsKey(close)) {
			onLockedAppClose(close, open);
		}
	}

	private void onLockedAppClose(String close, String open) {
		setRelockTimer(close);
		if (getPackageName().equals(close) || getPackageName().equals(open)) {
			// Don't interact with own app
			Log.e(TAG," app is same locer app");
			return;
		}

		if (mLockedPackages.containsKey(open)) {
			// The newly opened app needs a lock screen, so don't hide previous
			Log.e(TAG," open **********");
			return;
		}
		LockService.hide(this);
		// mAdCount++;
		// if (mAdCount % Constants.APPS_PER_INTERSTITIAL == 0) {
		// mInterstitialHelper.load();
		// }
	}

	// private int mAdCount = 0;

	private void setRelockTimer(String packageName) {

		Log.d(TAG, "setRelockTimer****");
		boolean locked = mLockedPackages.get(packageName);
		Log.d(TAG, "is pkg locked**** locked:" +locked);
		if (!locked) {
			if (mShortExitMillis != 0) {
				Runnable r = new RelockRunnable(packageName);
				mHandler.postDelayed(r, mShortExitMillis);
				mUnlockMap.put(packageName, r);
			} else {
				lockApp(packageName);
			}
		}
	}

	private void removeRelockTimer(String packageName) {
		// boolean locked = mLockedPackages.get(packageName);
		// if (!locked) {
		Log.d(TAG, "removeRelockTimer****");
		if (mUnlockMap.containsKey(packageName)) {

			mHandler.removeCallbacks(mUnlockMap.get(packageName));
			mUnlockMap.remove(packageName);
		}
	}

	/** This class will re-lock an app */
	private class RelockRunnable implements Runnable {
		private final String mPackageName;

		public RelockRunnable(String packageName) {
			mPackageName = packageName;
		}

		@Override
		public void run() {
			lockApp(mPackageName);
		}
	}

	List<RunningTaskInfo> mTestList = new ArrayList<RunningTaskInfo>();

	private RunningTaskInfo getTopTask() {
		return mActivityManager.getRunningTasks(1).get(0);
	}

	/**
	 * Unlock a single application. Should be called by {@link //LockActivity}
	 * 
	 * @param //appName
	 */
	public void unlockApp(String packageName) {
		Log.d(TAG, "unlocking app (packageName=" + packageName + ")");
		if (mLockedPackages.containsKey(packageName)) {
			mLockedPackages.put(packageName, false);
		}
	}

	private void lockAll() {
		for (Map.Entry<String, Boolean> entry : mLockedPackages.entrySet()) {
			entry.setValue(true);
		}
	}

	public void lockApp(String packageName) {
		if (mLockedPackages.containsKey(packageName)) {
			mLockedPackages.put(packageName, true);
		}
	}

	private void startNotification() {


		// Start foreground anyway
		startForegroundWithNotification();

		mShowNotification = new PrefUtils(this).getBoolean(R.string.pref_key_show_notification, R.bool.pref_def_show_notification);

		// If the user doesn't want a notification (default), remove it
		if (!mShowNotification) {
			// Retain foreground state
			HelperService.removeNotification(this);

			// Remove foreground
			// stopForeground(true);
		}
	}

	@SuppressLint("InlinedApi")
	private void startForegroundWithNotification() {
		Log.d(TAG, "showNotification");

		boolean hide = new PrefUtils(this).getBoolean(R.string.pref_key_hide_notification_icon, R.bool.pref_def_hide_notification_icon);
		int priority = hide ? Notification.PRIORITY_MIN : Notification.PRIORITY_DEFAULT;
		Intent i = new Intent(this, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		String title = getString(R.string.notification_title);
		String content = getString(R.string.notification_state_locked);
		NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
		nb.setSmallIcon(R.mipmap.ic_launcher);
		nb.setContentTitle(title);
		nb.setContentText(content);
		nb.setWhen(System.currentTimeMillis());
		nb.setContentIntent(pi);
		nb.setOngoing(true);
		nb.setPriority(priority);

		startForeground(NOTIFICATION_ID, nb.build());
	}

	public static final void start(Context c) {
		new Analytics(c).increment(LockerAnalytics.SERVICE_START);
		startAlarm(c);
	}

	/**
	 * 
	 * @param c
	 * @return The new state for the service, true for running, false for not
	 *         running
	 */
	public static boolean toggle(Context c) {
		if (isRunning(c)) {
			stop(c);
			return false;
		} else {
			start(c);
			return true;
		}

	}

	public static boolean isRunning(Context c) {
		ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			Log.e(TAG,"test ***" + service.service.getClassName());
			if (AppLockService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/** Starts the service */
	private static final void startAlarm(Context c) {
		AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);
		PendingIntent pi = getRunIntent(c);
		SharedPreferences sp = PrefUtils.prefs(c);
		String defaultPerformance = c.getString(R.string.pref_val_perf_normal);
		String s = sp.getString(c.getString(R.string.pref_key_performance), defaultPerformance);
		if (s.length() == 0)
			s = "0";
		long interval = Long.parseLong(s);
		Log.d(TAG, "Scheduling alarm (interval=" + interval + ")");
		long startTime = SystemClock.elapsedRealtime();

		//am.set(AlarmManager.ELAPSED_REALTIME,interval, pi);
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
			am.set(AlarmManager.ELAPSED_REALTIME, -interval, pi);

		}else {
			am.setRepeating(AlarmManager.ELAPSED_REALTIME, startTime, interval, pi);
		}
	}



 	Runnable  lolipopRunnable = new Runnable() {
		@Override
		public void run() {
			Intent i = new Intent(AppLockService.this, AppLockService.class);
			i.setAction(ACTION_START);
			startService(i);
			startLolipopHandler();
		}
	};

	static Handler lolipopHandler  = new Handler();
	private void startLolipopHandler(){
		//if(lolipopHandler == null)
		//lolipopHandler = new Handler();
		lolipopHandler.postDelayed(lolipopRunnable,100);
	}



	private static PendingIntent running_intent;

	private static final PendingIntent getRunIntent(Context c) {
		Log.d(TAG, "getRunIntent****");
		if (running_intent == null) {
			Intent i = new Intent(c, AppLockService.class);
			i.setAction(ACTION_START);
			running_intent = PendingIntent.getService(c, REQUEST_CODE, i, 0);
		}
		return running_intent;
	}

	private static final void stopAlarm(Context c) {
		Log.d(TAG, "stopAlarm****");
		AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);
		am.cancel(getRunIntent(c));
	}

	/** Stop this service, also stopping the alarm */
	public static final void stop(Context c) {
		Log.d(TAG, "stop MD****");
		stopAlarm(c);
		new Analytics(c).increment(LockerAnalytics.SERVICE_STOP);
		Intent i = new Intent(c, AppLockService.class);
		i.setAction(ACTION_STOP);
		c.startService(i);
	}

	/**
	 * Re-initialize everything.<br>
	 * This has only effect if the service was explicitly started using
	 * {@link #start(Context)}
	 */
	public static final void restart(Context c) {
		Log.d(TAG, "restart****");
		Intent i = new Intent(c, AppLockService.class);
		i.setAction(ACTION_RESTART);
		c.startService(i);
	}

	/**
	 * Forces the service to stop and then start again. This means that if the
	 * service was already stopped, it will just start
	 */
	public static final void forceRestart(Context c) {
		Intent i = new Intent(c, AppLockService.class);
		i.setAction(ACTION_RESTART);
		i.putExtra(EXTRA_FORCE_RESTART, true);
		c.startService(i);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy: (mAllowRestart=" + mAllowRestart + ")");
		if (mScreenReceiver != null)
			unregisterReceiver(mScreenReceiver);
		if (mShowNotification)
			stopForeground(true);

		if (mAllowRestart) {
			start(this);
			mAllowRestart = false;
			return;
		}

		Log.i(TAG, "onDestroy (mAllowDestroy=" + mAllowDestroy + ")");
		if (!mAllowDestroy) {
			Log.d(TAG, "Destroy not allowed, restarting service");
			start(this);
		} else {
			// Tell MainActivityantivirus we're stopping
			Intent i = new Intent(BROADCAST_SERVICE_STOPPED);
			i.addCategory(CATEGORY_STATE_EVENTS);
			sendBroadcast(i);
		}
		mAllowDestroy = false;
	}

	private void doStopSelf() {
		stopAlarm(this);
		mAllowDestroy = true;
		stopForeground(true);
		stopSelf();
		lolipopHandler.removeCallbacks(lolipopRunnable);
	}

	private void doRestartSelf() {
		Log.d(TAG, "Setting allowrestart to true");
		mAllowRestart = true;
		stopSelf();
	}

}
