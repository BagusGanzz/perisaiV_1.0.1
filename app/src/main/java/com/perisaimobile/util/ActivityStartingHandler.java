package com.perisaimobile.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Handler;
import android.provider.Settings;

import com.perisaimobile.activities.AppLockCreatePasswordActivity;
import com.perisaimobile.iface.ActivityStartingListener;
import com.perisaimobile.model.AppsLocked;
import com.perisaimobile.service.LockService;
import com.studioninja.locker.BuildConfig;

import java.util.Hashtable;

public class ActivityStartingHandler implements ActivityStartingListener {
    private Context context;
    public String lastRunningPackage = BuildConfig.FLAVOR;
    private SharedPreferences sharedPreferences;
    private Hashtable<String, Runnable> tempAllowedPackages;

    private class RemoveFromTempRunnable implements Runnable {
        private String mPackageName;

        public RemoveFromTempRunnable(String pname) {
            this.mPackageName = pname;
        }

        public void run() {
            ActivityStartingHandler.this.tempAllowedPackages.remove(this.mPackageName);
        }
    }

    public ActivityStartingHandler(Context context) {
        this.context = context;
        this.tempAllowedPackages = new Hashtable();
        this.sharedPreferences = context.getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0);
        context.registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ActivityStartingHandler.this.lastRunningPackage = intent.getStringExtra("packageName");
                if (ActivityStartingHandler.this.sharedPreferences.getBoolean(AppLockCreatePasswordActivity.KEY_RELOCK_POLICY, false)) {
                    Runnable runnable = new RemoveFromTempRunnable(ActivityStartingHandler.this.lastRunningPackage);
                    ActivityStartingHandler.this.tempAllowedPackages.put(ActivityStartingHandler.this.lastRunningPackage, runnable);
                    new Handler().postDelayed(runnable, (long) (60000 * ActivityStartingHandler.this.sharedPreferences.getInt(AppLockCreatePasswordActivity.KEY_RELOCK_TIMEOUT, 1)));
                }
            }
        }, new IntentFilter(LockService.ACTION_APPLICATION_PASSED));
    }

    public void onActivityStarting(String packageName) {
        synchronized (this) {
            if (packageName.equals(this.lastRunningPackage)) {
            } else if (packageName.equals(this.context.getPackageName())) {
                this.lastRunningPackage = packageName;
            } else if (this.sharedPreferences.getBoolean(AppLockCreatePasswordActivity.KEY_RELOCK_POLICY, false) && this.tempAllowedPackages.containsKey(packageName)) {
            } else {
                AppsLocked appsLocked = new AppsLocked(this.context);
                if (this.sharedPreferences.getString(AppLockCreatePasswordActivity.KEY_PASSWORD, null) != null) {
                    if (packageName.equals("com.android.packageinstaller") && !Utils.isServiceRunning(this.context, LockService.class)) {
                        blockApp(packageName);
                        return;
                    } else if (appsLocked.isAppLocked(packageName) && !Utils.isServiceRunning(this.context, LockService.class)) {
                        if (VERSION.SDK_INT < 23 || !Settings.canDrawOverlays(this.context)) {
                            blockApp(packageName);
                            return;
                        }
                        blockApp(packageName);
                        return;
                    }
                }
                this.lastRunningPackage = packageName;
            }
        }
    }

    private void blockApp(String packageName) {
        Intent intent = new Intent(this.context, LockService.class);
        intent.putExtra("packageName", packageName);
        this.context.startService(intent);
    }
}
