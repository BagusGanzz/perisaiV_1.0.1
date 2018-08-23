package com.perisaimobile.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.util.Log;

import com.perisaimobile.activities.AppLockCreatePasswordActivity;
import com.perisaimobile.activities.MainActivityantivirus;
import com.perisaimobile.iface.ActivityStartingListener;
import com.perisaimobile.iface.IPackageChangesListener;
import com.perisaimobile.iface.IProblem;
import com.perisaimobile.model.AppData;
import com.perisaimobile.model.AppLock;
import com.perisaimobile.model.AppProblem;
import com.perisaimobile.model.Application;
import com.perisaimobile.model.MenacesCacheSet;
import com.perisaimobile.model.PackageData;
import com.perisaimobile.model.PermissionData;
import com.perisaimobile.model.Scanner;
import com.perisaimobile.model.UserWhiteList;
import com.perisaimobile.receiver.PackageBroadcastReceiver;
import com.perisaimobile.util.ActivityStartingHandler;
import com.perisaimobile.util.ProblemsDataSetTools;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class MonitorShieldService extends Service {
    static int _currentNotificationId = 0;
    private final IBinder _binder = new MonitorShieldLocalBinder();
    Set<PackageData> _blackListActivities;
    Set<PackageData> _blackListPackages;
    IClientInterface _clientInterface = null;
    final String _logTag = MonitorShieldService.class.getSimpleName();
    MenacesCacheSet _menacesCacheSet = null;
    PackageBroadcastReceiver _packageBroadcastReceiver;
    Set<PermissionData> _suspiciousPermissions;
    UserWhiteList _userWhiteList = null;
    Set<PackageData> _whiteListPackages;
    List<AppLock> appLock;
    private BroadcastReceiver forceStopBroadcast = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            MonitorShieldService.this.isBoosterRunning = intent.getBooleanExtra(MyAccessibilityService.FORCE_STOP, false);
        }
    };
    private boolean isBoosterRunning = false;
    private TimerTask lockAppTask;
    List<Application> runningApplications;

    public interface IClientInterface {
        void onMonitorFoundMenace(IProblem iProblem);

        void onScanResult(List<PackageInfo> list, Set<IProblem> set);
    }

    private class LockAppTask extends TimerTask {
        private ActivityStartingListener listener;

        public LockAppTask(ActivityStartingListener listener) {
            this.listener = listener;
        }

        public void run() {
            if (VERSION.SDK_INT >= 22) {
                String topPackageName = null;
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) MonitorShieldService.this.getSystemService("usagestats");
                long time = System.currentTimeMillis();
                List<UsageStats> stats = mUsageStatsManager.queryUsageStats(0, time - 10000, time);
                if (stats != null) {
                    SortedMap<Long, UsageStats> mySortedMap = new TreeMap();
                    for (UsageStats usageStats : stats) {
                        mySortedMap.put(Long.valueOf(usageStats.getLastTimeUsed()), usageStats);
                    }
                    if (!mySortedMap.isEmpty()) {
                        topPackageName = ((UsageStats) mySortedMap.get(mySortedMap.lastKey())).getPackageName();
                    }
                }
                if (this.listener != null && topPackageName != null && !MonitorShieldService.this.isBoosterRunning) {
                    this.listener.onActivityStarting(topPackageName);
                    return;
                }
                return;
            }
            ActivityManager am = (ActivityManager) MonitorShieldService.this.getBaseContext().getSystemService("activity");
            PackageManager pm = MonitorShieldService.this.getBaseContext().getPackageManager();
            String mPackageName = null;
            if (VERSION.SDK_INT > 20) {
                List<RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
                if (appProcesses.size() >= 0) {
                    RunningAppProcessInfo appProcess = (RunningAppProcessInfo) appProcesses.get(0);
                    if (appProcess.importance == 100) {
                        mPackageName = appProcess.processName;
                    }
                }
            } else {
                List<RunningTaskInfo> runningTaskInfos = am.getRunningTasks(1);
                if (runningTaskInfos.size() > 0) {
                    mPackageName = ((RunningTaskInfo) runningTaskInfos.get(0)).topActivity.getPackageName();
                }
            }
            PackageInfo foregroundAppPackageInfo = null;
            if (mPackageName != null) {
                try {
                    foregroundAppPackageInfo = pm.getPackageInfo(mPackageName, 0);
                } catch (NameNotFoundException e) {
                    System.out.println("Exception in run method " + e);
                    e.printStackTrace();
                }
            }
            if (this.listener != null && foregroundAppPackageInfo != null) {
                String s = foregroundAppPackageInfo.packageName;
                if (s != null && !MonitorShieldService.this.isBoosterRunning) {
                    this.listener.onActivityStarting(s);
                }
            }
        }
    }

    public class MonitorShieldLocalBinder extends Binder {
        public MonitorShieldService getServiceInstance() {
            return MonitorShieldService.this;
        }
    }

    public Set<PackageData> getWhiteListPackages() {
        return this._whiteListPackages;
    }

    public Set<PackageData> getBlackListPackages() {
        return this._blackListPackages;
    }

    public Set<PackageData> getBlackListActivities() {
        return this._blackListActivities;
    }

    public Set<PermissionData> getSuspiciousPermissions() {
        return this._suspiciousPermissions;
    }

    public UserWhiteList getUserWhiteList() {
        return this._userWhiteList;
    }

    public MenacesCacheSet getMenacesCacheSet() {
        return this._menacesCacheSet;
    }

    public void registerClient(IClientInterface clientInterface) {
        this._clientInterface = clientInterface;
    }

    public List<Application> getRunningApplications() {
        return this.runningApplications;
    }

    public List<AppLock> getAppLock() {
        return this.appLock;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    public void onCreate() {
        super.onCreate();
        if (getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0).getBoolean(AppLockCreatePasswordActivity.KEY_APPLOCKER_SERVICE, true)) {
            startLockAppTask();
        }
        this._packageBroadcastReceiver = new PackageBroadcastReceiver();
        PackageBroadcastReceiver packageBroadcastReceiver = this._packageBroadcastReceiver;
        PackageBroadcastReceiver.setPackageBroadcastListener(new IPackageChangesListener() {
            public void OnPackageAdded(Intent i) {
                if (MonitorShieldService.this.getSharedPreferences("Settings", 0).getBoolean("auto_scan", true)) {
                    MonitorShieldService.this.scanApp(i.getData().getSchemeSpecificPart());
                }
            }

            public void OnPackageRemoved(Intent intent) {
                String packageName = intent.getData().getSchemeSpecificPart();
                if (ProblemsDataSetTools.removeAppProblemByPackage(MonitorShieldService.this._menacesCacheSet, packageName)) {
                    Log.e(MonitorShieldService.this._logTag, ">>>>>>>>>>>>>>>>>>>  The application " + packageName + " was removed from menace list because it was uninstalled.");
                } else {
                    Log.e(MonitorShieldService.this._logTag, ">>>>>>>>>>>>>>>>>>>  The application " + packageName + " could no be removed from menaceCache while being uninstalled. ERRRRRORRRRRRRR!!!!!!");
                }
                MonitorShieldService.this._menacesCacheSet.writeToJSON();
                if (ProblemsDataSetTools.removeAppProblemByPackage(MonitorShieldService.this._userWhiteList, packageName)) {
                    Log.e(MonitorShieldService.this._logTag, ">>>>>>>>>>>>>>>>>>>  The application " + packageName + " was removed from white list because it was uninstalled.");
                } else {
                    Log.e(MonitorShieldService.this._logTag, ">>>>>>>>>>>>>>>>>>>  The application " + packageName + " could no be removed from white while being uninstalled. ERRRRRORRRRRRRR!!!!!!");
                }
                MonitorShieldService.this._userWhiteList.writeToJSON();
            }
        });
        IntentFilter packageFilter = new IntentFilter();
        packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
        packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        packageFilter.addDataScheme("package");
        registerReceiver(this._packageBroadcastReceiver, packageFilter);
        registerReceiver(this.forceStopBroadcast, new IntentFilter(MyAccessibilityService.BROADCAST_FORCE_STOP));
        _loadDataFiles();
        _loadData();
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this._packageBroadcastReceiver);
        unregisterReceiver(this.forceStopBroadcast);
        this._packageBroadcastReceiver = null;
        if (this.lockAppTask != null) {
            this.lockAppTask.cancel();
        }
    }

    public IBinder onBind(Intent i) {
        return this._binder;
    }

    private void _loadDataFiles() {
        JSONArray m_jArry;
        int i;
        this._whiteListPackages = new HashSet();
        this._blackListPackages = new HashSet();
        this._blackListActivities = new HashSet();
        this._suspiciousPermissions = new HashSet();
        this._userWhiteList = new UserWhiteList(this);
        this._menacesCacheSet = new MenacesCacheSet(this);
        try {
            m_jArry = new JSONObject(Utils.loadJSONFromAsset(this, "whiteList.json")).getJSONArray("data");
            for (i = 0; i < m_jArry.length(); i++) {
                this._whiteListPackages.add(new PackageData(m_jArry.getJSONObject(i).getString("packageName")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            m_jArry = new JSONObject(Utils.loadJSONFromAsset(this, "blackListPackages.json")).getJSONArray("data");
            for (i = 0; i < m_jArry.length(); i++) {
                this._blackListPackages.add(new PackageData(m_jArry.getJSONObject(i).getString("packageName")));
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        try {
            m_jArry = new JSONObject(Utils.loadJSONFromAsset(this, "blackListActivities.json")).getJSONArray("data");
            for (i = 0; i < m_jArry.length(); i++) {
                this._blackListActivities.add(new PackageData(m_jArry.getJSONObject(i).getString("packageName")));
            }
        } catch (JSONException e22) {
            e22.printStackTrace();
        }
        try {
            m_jArry = new JSONObject(Utils.loadJSONFromAsset(this, "permissions.json")).getJSONArray("data");
            for (i = 0; i < m_jArry.length(); i++) {
                JSONObject temp = m_jArry.getJSONObject(i);
                this._suspiciousPermissions.add(new PermissionData(temp.getString("permissionName"), temp.getInt("dangerous")));
            }
        } catch (JSONException e222) {
            e222.printStackTrace();
        }
    }

    public void _loadData() {
        this.runningApplications = new ArrayList();
        if (Utils.getCurrentTime() - getSharedPreferences("Settings", 0).getLong(BoosterService.PREFERENCES_LAST_TIME_BOOST, 0) >= 300000) {
            this.runningApplications.addAll(Utils.getRunningApplications(this));
        }
        this.appLock = new ArrayList();
        this.appLock.addAll(Utils.getAppLock(this));
    }

    protected boolean _checkIfPackageInWhiteList(String packageName, Set<PackageData> whiteListPackages) {
        for (PackageData packageInfo : whiteListPackages) {
            if (Utils.stringMatchesMask(packageName, packageInfo.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public void scanFileSystem() {
        List<PackageInfo> allPackages = Utils.getApps(this, 4097);
        List<PackageInfo> nonSystemApps = Utils.getNonSystemApps(this, allPackages);
        Set<IProblem> tempBadResults = new HashSet();
        List<PackageInfo> potentialBadApps = _removeWhiteListPackagesFromPackageList(_removeWhiteListPackagesFromPackageList(nonSystemApps, this._whiteListPackages), ProblemsDataSetTools.getAppProblemsAsPackageDataList(this._userWhiteList));
        Scanner.scanForBlackListedActivityApps(potentialBadApps, this._blackListActivities, tempBadResults);
        Scanner.scanForSuspiciousPermissionsApps(potentialBadApps, this._suspiciousPermissions, tempBadResults);
        Scanner.scanInstalledAppsFromGooglePlay(this, potentialBadApps, tempBadResults);
        Scanner.scanSystemProblems(this, this._userWhiteList, tempBadResults);
        this._menacesCacheSet.addItems(tempBadResults);
        this._menacesCacheSet.writeToJSON();
        if (this._clientInterface != null) {
            this._clientInterface.onScanResult(allPackages, tempBadResults);
        }
    }

    public void scanApp(String packageName) {
        AppData appData = AppData.getInstance(this);
        Intent intent = new Intent(this, MainActivityantivirus.class);
        Intent openAppIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        String appName = Utils.getAppNameFromPackage(this, packageName);
        if (Scanner.isAppWhiteListed(packageName, this._whiteListPackages)) {
            int i = _currentNotificationId;
            _currentNotificationId = i + 1;
            Utils.notificatePush(this, i, R.drawable.ic_noti_safe, appName + " " + getString(R.string.trusted_message), appName, "App " + appName + " " + getString(R.string.trusted_by_app), openAppIntent);
            return;
        }
        PackageInfo pi;
        try {
            pi = Utils.getPackageInfo(this, packageName, 4097);
        } catch (NameNotFoundException e) {
            pi = null;
        }
        if (pi != null) {
            AppProblem appProblem = new AppProblem(pi.packageName);
            List<ActivityInfo> recycleList = new ArrayList();
            Scanner.scanForBlackListedActivityApp(pi, appProblem, this._blackListActivities, recycleList);
            Scanner.scanForSuspiciousPermissionsApp(pi, appProblem, this._suspiciousPermissions);
            Scanner.scanInstalledAppFromGooglePlay(this, appProblem);
            if (appProblem.isMenace()) {
                if (appData.getFirstScanDone()) {
                    this._menacesCacheSet.addItem( appProblem);
                    this._menacesCacheSet.writeToJSON();
                }
                if (this._clientInterface != null) {
                    this._clientInterface.onMonitorFoundMenace(appProblem);
                }
                int i2 = _currentNotificationId;
                _currentNotificationId = i2 + 1;
                Utils.notificatePush(this, i2, R.drawable.ic_noti_problems, appName + " " + getString(R.string.has_been_scanned), appName, getString(R.string.enter_to_solve_problems), intent);
                return;
            }
            int i = _currentNotificationId;
            _currentNotificationId = i + 1;
            Utils.notificatePush(this, i, R.drawable.ic_noti_safe, appName + " " + getString(R.string.is_secure), appName, getString(R.string.has_no_threats), openAppIntent);
        }
    }

    protected List<PackageInfo> _removeWhiteListPackagesFromPackageList(List<PackageInfo> packagesToSearch, Set<? extends PackageData> whiteListPackages) {
        List<PackageInfo> trimmedPackageList = new ArrayList(packagesToSearch);
        for (PackageData pd : whiteListPackages) {
            int index = 0;
            String mask = pd.getPackageName();
            while (!false && index < trimmedPackageList.size()) {
                if (Utils.stringMatchesMask(((PackageInfo) trimmedPackageList.get(index)).packageName, mask)) {
                    trimmedPackageList.remove(index);
                } else {
                    index++;
                }
            }
        }
        return trimmedPackageList;
    }

    public void startLockAppTask() {
        if (this.lockAppTask != null) {
            this.lockAppTask.cancel();
        }
        this.lockAppTask = new LockAppTask(new ActivityStartingHandler(this));
        new Timer().scheduleAtFixedRate(this.lockAppTask, 0, 50);
    }

    public void stopLockAppTask() {
        if (this.lockAppTask != null) {
            this.lockAppTask.cancel();
        }
    }
}
