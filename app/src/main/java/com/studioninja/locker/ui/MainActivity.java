package com.studioninja.locker.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.studioninja.locker.BuildConfig;
import com.studioninja.locker.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.studioninja.locker.LockerAnalytics;
import com.studioninja.locker.R;
import com.studioninja.locker.lock.AppLockService;
import com.studioninja.locker.lock.LockService;
import com.studioninja.locker.ui.NavigationFragment.NavigationListener;
import com.studioninja.locker.util.PrefUtils;
import com.studioninja.locker.util.Util;
import com.studioninja.util.Analytics;
import com.studioninja.util.DialogSequencer;

public class MainActivity extends ActionBarActivity implements NavigationListener {

	// private static final String VERSION_URL_PRD =
	// "https://twinone.org/apps/locker/update.php";
	// private static final String VERSION_URL_DBG =
	// "https://twinone.org/apps/locker/dbg-update.php";
	// public static final String VERSION_URL = Constants.DEBUG ?
	// VERSION_URL_DBG : VERSION_URL_PRD;
	public static final String EXTRA_UNLOCKED = "com.monika.locker.unlocked";

	/** code to post/handler request for permission */
	public final static int REQUEST_CODE = 101;
	public final static int REQ_CODE = 102;
	public final static int REQCODE = 103;

	private DialogSequencer mSequencer;
	private Fragment mCurrentFragment;
	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationFragment mNavFragment;

	/**
	 * Used to store the last screen title. For use in {@link //#restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private ActionBar mActionBar;
	private BroadcastReceiver mReceiver;
	private IntentFilter mFilter;
	private int mActivityRequestCode;
	private int mActivityResultCode;
	private boolean hasActivityResult;
    private InterstitialAd mInterstitialAd;

    private class ServiceStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("MainACtivity", "Received broadcast (action=" + intent.getAction());
			updateLayout();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handleIntent();

		mReceiver = new ServiceStateReceiver();
		mFilter = new IntentFilter();
		mFilter.addCategory(AppLockService.CATEGORY_STATE_EVENTS);
		mFilter.addAction(AppLockService.BROADCAST_SERVICE_STARTED);
		mFilter.addAction(AppLockService.BROADCAST_SERVICE_STOPPED);

		mNavFragment = (NavigationFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		// Set up the drawer.
		mNavFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
		mTitle = getTitle();

		mActionBar = getSupportActionBar();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_home);
		mCurrentFragment = new AppsFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.container, mCurrentFragment).commit();
		mCurrentFragmentType = NavigationElement.TYPE_APPS;

		mSequencer = new DialogSequencer();
		Analytics a = new Analytics(this);
		long count = a.increment(LockerAnalytics.OPEN_MAIN);
		boolean never = a.getBoolean(LockerAnalytics.SHARE_NEVER);
		// Every 5 times the user opens the app, but only after 10 initial opens
		if (!never && count >= 10 && count % 5 == 0) {
			mSequencer.addDialog(Dialogs.getShareEditDialog(this, true));
		}
		if (Util.checkDrawOverlayPermission(this,REQ_CODE))
		showDialogs();
		showLockerIfNotUnlocked(false);

        AdRequest adRequest;
        if (BuildConfig.DEBUG_MODE){
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
        }else {
            adRequest = new AdRequest.Builder().build();
        }

        mInterstitialAd = newInterstitialAd();
        loadInterstitial(adRequest);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("Main", "onResume");
		// showLockerIfNotUnlocked(true);
		registerReceiver(mReceiver, mFilter);
		updateLayout();
		//checkForAppUsage();
		accessPermission();

	}


	private void checkForAppLockerServise(){
		//action.check.lollipop
		Log.e("Main activity", "MD checkForAppLockerServise");
		sendBroadcast(new Intent("action.check.lollipop"));

	}

	private void checkForAppUsage(){
		if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			if(!usageAccessGranted(MainActivity.this)){

				AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
				dialog.setMessage("App Locker need  usage access permission to work properly on android lollipop and above.");
				dialog.setTitle(getString(R.string.application_name));
				dialog.setCancelable(false);
				dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
						startActivity(intent);
					}
				});
				dialog.show();
			}
		}
	}



	private void accessPermission(){

		if(android.os.Build.VERSION.SDK_INT >= 23) {
			int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.SYSTEM_ALERT_WINDOW);
			if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
				checkForAppUsage();
				/*Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
				startActivity(intent);*/
			}else{
				checkForAppUsage();
			}
		}else {
			checkForAppUsage();
		}


	}






	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static boolean usageAccessGranted(Context context) {
		AppOpsManager appOps = (AppOpsManager)context.getSystemService(Context.APP_OPS_SERVICE);
		int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
				android.os.Process.myUid(), context.getPackageName());
		return mode == AppOpsManager.MODE_ALLOWED;
	}





	@Override
	protected void onPause() {
		super.onPause();
		// mSequencer.stop();
		LockService.hide(this);
		unregisterReceiver(mReceiver);
		mSequencer.stop();

		// We have to finish here or the system will assign a lower priority to
		// the app (since 4.4?)
		if (mCurrentFragmentType != NavigationElement.TYPE_SETTINGS &&mCurrentFragmentType != NavigationElement.TYPE_PHOTO  && mCurrentFragmentType != NavigationElement.TYPE_VIDEO  ) {
			 finish();
		}
	}

	@Override
	protected void onDestroy() {
		Log.v("Main", "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.d("", "onNewIntent");
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent();
	}

	@Override
	public void setTitle(CharSequence title) {
		super.setTitle(title);
		mTitle = title;
		getSupportActionBar().setTitle(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.global, menu);
		return true;
	}

	/**
	 * Provide a way back to {@link MainActivity} without having to provide a password again. It finishes the calling {@link Activity}
	 * 
	 * @param context
	 */
	public static final void showWithoutPassword(Context context) {
		Intent i = new Intent(context, MainActivity.class);
		i.putExtra(EXTRA_UNLOCKED, true);
		if (!(context instanceof Activity)) {
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(i);
	}

	public void setActionBarTitle(int resId) {
		mActionBar.setTitle(resId);
	}

	/**
	 * 
	 * @return True if the service is allowed to start
	 */
	private boolean showDialogs() {
		boolean deny = false;

		// Recovery code
		mSequencer.addDialog(Dialogs.getRecoveryCodeDialog(this));

		// Empty password
		deny = Dialogs.addEmptyPasswordDialog(this, mSequencer);

		mSequencer.start();
		return !deny;
	}

	private void showLockerIfNotUnlocked(boolean relock) {
		boolean unlocked = getIntent().getBooleanExtra(EXTRA_UNLOCKED, false);
		if (new PrefUtils(this).isCurrentPasswordEmpty()) {
			unlocked = true;
		}
		if (!unlocked) {
			LockService.showCompare(this, getPackageName());
		}
		getIntent().putExtra(EXTRA_UNLOCKED, !relock);
	}

	private void updateLayout() {
		Log.d("Main", "UPDATE LAYOUT Setting service state: " + AppLockService.isRunning(this));
		mNavFragment.getAdapter().setServiceState(AppLockService.isRunning(this));
	}

	/**
	 * Handle this Intent for searching...
	 */
	private void handleIntent() {
		if (getIntent() != null && getIntent().getAction() != null) {
			if (getIntent().getAction().equals(Intent.ACTION_SEARCH)) {
				Log.d("MainActivityantivirus", "Action search!");
				if (mCurrentFragmentType == NavigationElement.TYPE_APPS) {
					final String query = getIntent().getStringExtra(SearchManager.QUERY);
					if (query != null) {
						((AppsFragment) mCurrentFragment).onSearch(query);
					}
				}
			}
		}
	}

	private boolean mNavPending;
	int mCurrentFragmentType;
	int mNavPendingType = -1;

	@Override
	public boolean onNavigationElementSelected(int type) {
		if (type == NavigationElement.TYPE_TEST) {
			// Test something here
			return false;
		} else if (type == NavigationElement.TYPE_STATUS) {
			if (Util.checkDrawOverlayPermission(this,REQ_CODE))
			toggleService();
			return false;
		}
		mNavPending = true;
		mNavPendingType = type;
		return true;
	}

	private void toggleService() {
		boolean newState = false;
		if (AppLockService.isRunning(this)) {
			Log.d("", "toggleService() Service is running, now stopping");
			AppLockService.stop(this);
		} else if (Dialogs.addEmptyPasswordDialog(this, mSequencer)) {
			mSequencer.start();
		} else {
			newState = AppLockService.toggle(this);
		}
		if (mNavFragment != null)
			mNavFragment.getAdapter().setServiceState(newState);
	}

	@Override
	public void onDrawerOpened(View drawerView) {
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	public void onDrawerClosed(View drawerView) {
		getSupportActionBar().setTitle(mTitle);
		if (mNavPending) {
			navigateToFragment(mNavPendingType);
			mNavPending = false;
		}
	}

	/**
	 * Open a specific Fragment
	 * 
	 * @param type
	 */
	public void navigateToFragment(int type) {
		if (type == mCurrentFragmentType) {
			// Don't duplicate
			return;
		}
		if (type == NavigationElement.TYPE_CHANGE) {
			if (Util.checkDrawOverlayPermission(this,REQUEST_CODE))
			Dialogs.getChangePasswordDialog(this).show();
			// Don't change current fragment type
			return;
		}

		switch (type) {
		case NavigationElement.TYPE_APPS:
			mCurrentFragment = new AppsFragment();
			break;

		case NavigationElement.TYPE_STATISTICS:
			mCurrentFragment = new StatisticsFragment();
			break;
		case NavigationElement.TYPE_PHOTO:
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						111);
				return ;
			}
			mCurrentFragment = new PhotoFragment();
			break;
		case NavigationElement.TYPE_VIDEO:
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						112);
				return ;
			}
			mCurrentFragment = new VideoFragment();
			break;
		case NavigationElement.TYPE_THEMES:
//			mCurrentFragment = new ThemesFragment();
			Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
			break;

		}
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.container, mCurrentFragment).commit();
		mCurrentFragmentType = type;
	}


	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		//if some result was returned in onActivityResult, then send it for processing
		if (this.hasActivityResult) {
			this.hasActivityResult = false;
			processActivityResult(mActivityRequestCode, mActivityResultCode);
		}
	}

	private void processActivityResult(int requestCode, int mActivityResultCode) {
		switch (requestCode) {
			case 111:
				if (mActivityResultCode== PackageManager.PERMISSION_GRANTED) {
					mCurrentFragment = new PhotoFragment();
					FragmentManager fm = getSupportFragmentManager();
					fm.beginTransaction().replace(R.id.container, mCurrentFragment).commit();
					mCurrentFragmentType = NavigationElement.TYPE_PHOTO;
				}
				return;
			case 112:
				if (mActivityResultCode == PackageManager.PERMISSION_GRANTED) {
					mCurrentFragment = new VideoFragment();
					FragmentManager fm = getSupportFragmentManager();
					fm.beginTransaction().replace(R.id.container, mCurrentFragment).commit();
				}
				return;
			default:
		}
	}

	@Override
	public void onShareButton() {
		// Don't add never button, the user wanted to share
		Dialogs.getShareEditDialog(this, false).show();
	}

	@Override
	public void onRateButton() {
		// toGooglePlay();
	}

	// private void toGooglePlay() {
	// Intent intent = new Intent(Intent.ACTION_VIEW);
	// intent.setData(Uri.parse("market://details?id=" + getPackageName()));
	// if (getPackageManager().queryIntentActivities(intent,
	// PackageManager.MATCH_DEFAULT_ONLY).size() >= 1) {
	// startActivity(intent);
	// }
	// }



	@Override
	protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
		/** check if received result code
		 is equal our requested code for draw permission  */
		if (requestCode == REQUEST_CODE) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (Settings.canDrawOverlays(this)) {
                    // continue here - permission was granted
					Dialogs.getChangePasswordDialog(this).show();
                }
			}
		}else if (requestCode == REQ_CODE) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if (Settings.canDrawOverlays(this)) {
					// continue here - permission was granted
					showDialogs();
				}
			}
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQCODE:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
					toggleService();
				}
				break;
			case REQ_CODE:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
					showDialogs();
				}
				break;
			case REQUEST_CODE:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
					Dialogs.getChangePasswordDialog(this).show();
				}
				break;
			case 1000:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
					checkForAppUsage();
				} else {
					// Permission Denied
					Toast.makeText(MainActivity.this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				this.mActivityRequestCode = requestCode;
				this.mActivityResultCode = grantResults[0];
				this.hasActivityResult=true;
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}


    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClosed() {
                finish();
            }
        });
        return interstitialAd;
    }

    private boolean showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            return false;
        }
        return true;
    }

    private void loadInterstitial(AdRequest adRequest) {
        // Disable the next level button and load the ad.
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        if (showInterstitial()) {
            finish();
        }
    }
}
