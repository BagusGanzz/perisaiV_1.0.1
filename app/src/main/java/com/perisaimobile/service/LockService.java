package com.perisaimobile.service;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.perisaimobile.activities.AppLockCreatePasswordActivity;
import com.perisaimobile.activities.AppLockForgotPasswordActivity;
import com.perisaimobile.activities.AppLockImageActivity;
import com.perisaimobile.activities.AppLockSettingsActivity;
import com.perisaimobile.databases.ImagesDatabaseHelper;
import com.perisaimobile.model.Image;
import com.perisaimobile.model.Selfie;
import com.perisaimobile.util.HomeWatcher;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.takwolf.android.lock9.Lock9View;
import com.takwolf.android.lock9.Lock9View.CallBack;
import com.studioninja.locker.R;

public class LockService extends Service {
    public static final String ACTION_APPLICATION_PASSED = "com.star.applock.applicationpassedtest";
    public static final String KEY_APP_THIEVES = "app_thieves";
    public static final String KEY_THIEVES = "thieves";
    public static final int NOTIFICATION_ID_APP_LOCK = 1111;
    private int countFailed;
    private ImagesDatabaseHelper imagesDatabaseHelper;
    private ImageView img_app_icon;
    private HomeWatcher mHomeWatcher;
    private String pakageName;
    private SharedPreferences sharedPreferences;
    private TextView tv_app_name;
    private View view;
    private WindowManager windowManager;

    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onCreate() {
        LayoutParams params;
        super.onCreate();
        this.mHomeWatcher = new HomeWatcher(this);
        this.mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            public void onHomePressed() {
                if (Utils.isServiceRunning(LockService.this, LockService.class)) {
                    LockService.this.stopSelf();
                }
            }

            public void onHomeLongPressed() {
                if (Utils.isServiceRunning(LockService.this, LockService.class)) {
                    LockService.this.stopSelf();
                }
            }
        });
        this.mHomeWatcher.startWatch();
        this.sharedPreferences = getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0);
        this.imagesDatabaseHelper = ImagesDatabaseHelper.getInstance(this);
        this.windowManager = (WindowManager) getApplicationContext().getSystemService("window");
        if (VERSION.SDK_INT >= 19) {
            params = new LayoutParams(-1, -1, 2003, 67108872, -3);
        } else {
            params = new LayoutParams(-1, -1, 2003, 8, -3);
        }
        params.gravity = 48;
        params.screenOrientation = 1;
        this.view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.lock_view, null);
        ((ImageView) this.view.findViewById(R.id.img_close)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LockService.this.startActivity(Utils.getHomeIntent());
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        LockService.this.stopSelf();
                    }
                }, 500);
            }
        });
        this.img_app_icon = (ImageView) this.view.findViewById(R.id.img_app_icon);
        this.tv_app_name = (TextView) this.view.findViewById(R.id.tv_app_name);
        TypeFaceUttils.setNomal((Context) this, this.tv_app_name);
        Lock9View lock_view = (Lock9View) this.view.findViewById(R.id.lock_view);
        Lock9View lock_view_disvibrate = (Lock9View) this.view.findViewById(R.id.lock_view_disvibrate);
        if (this.sharedPreferences.getBoolean(AppLockSettingsActivity.KEY_VIBRATE, false)) {
            lock_view.setVisibility(0);
            lock_view_disvibrate.setVisibility(8);
        } else {
            lock_view.setVisibility(8);
            lock_view_disvibrate.setVisibility(0);
        }
        lock_view.setCallBack(new CallBack() {
            public void onFinish(String password) {
                LockService.this.finish(password);
            }
        });
        lock_view_disvibrate.setCallBack(new CallBack() {
            public void onFinish(String password) {
                LockService.this.finish(password);
            }
        });
        this.windowManager.addView(this.view, params);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        this.pakageName = intent.getStringExtra("packageName");
        this.img_app_icon.setImageDrawable(Utils.getIconFromPackage(this.pakageName, this));
        this.tv_app_name.setText(Utils.getAppNameFromPackage(this, this.pakageName));
        return 2;
    }

    private void finish(String password) {
        if (password.equals(this.sharedPreferences.getString(AppLockCreatePasswordActivity.KEY_PASSWORD, null))) {
            sendBroadcast(new Intent().setAction(ACTION_APPLICATION_PASSED).putExtra("packageName", this.pakageName));
            if (this.sharedPreferences.getBoolean(KEY_THIEVES, false)) {
                Image image = this.imagesDatabaseHelper.findByID(this.sharedPreferences.getLong(KEY_APP_THIEVES, -1));
                if (image != null) {
                    Intent intent = new Intent(this, AppLockImageActivity.class);
                    intent.setFlags(32768);
                    intent.putExtra("data", image.getId());
                    Utils.notificateAppLock(this, NOTIFICATION_ID_APP_LOCK, R.mipmap.ic_thieves, getResources().getString(R.string.someone_tries_to_open_your_app), image.getAppName(), getResources().getString(R.string.someone_tries_to_open_your_app), intent);
                }
            }
            this.sharedPreferences.edit().putBoolean(KEY_THIEVES, false).apply();
            stopSelf();
            return;
        }
        this.countFailed++;
        if (this.countFailed == 3 && this.sharedPreferences.getBoolean(AppLockSettingsActivity.KEY_SELFIE, false)) {
            new Selfie(this, this.pakageName).takePhoto();
        }
        final Dialog dialog = new Dialog(this, R.style.MaterialDialogSheet);
        dialog.getWindow().setType(2003);
        dialog.getWindow().clearFlags(2);
        dialog.getWindow().setFlags(32, 32);
        dialog.setContentView(R.layout.snackbar_view);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setGravity(80);
        TextView tv_forgot_password = (TextView) dialog.findViewById(R.id.tv_forgot_password);
        TypeFaceUttils.setNomal((Context) this, (TextView) dialog.findViewById(R.id.tv_decription));
        TypeFaceUttils.setNomal((Context) this, tv_forgot_password);
        tv_forgot_password.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LockService.this.startActivity(Utils.getHomeIntent());
                Intent intent = new Intent(LockService.this, AppLockForgotPasswordActivity.class);
                intent.addFlags(268435456);
                LockService.this.startActivity(intent);
                dialog.dismiss();
                LockService.this.stopSelf();
            }
        });
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, 1500);
    }

    public void onDestroy() {
        this.mHomeWatcher.stopWatch();
        super.onDestroy();
        try {
            if (this.view != null) {
                this.windowManager.removeView(this.view);
            }
        } catch (Exception e) {
        }
    }
}
