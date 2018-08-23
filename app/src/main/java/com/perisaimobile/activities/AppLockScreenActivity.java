package com.perisaimobile.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.databases.ImagesDatabaseHelper;
import com.perisaimobile.model.Image;
import com.perisaimobile.model.Selfie;
import com.perisaimobile.service.LockService;
import com.perisaimobile.util.Utils;
import com.takwolf.android.lock9.Lock9View;
import com.takwolf.android.lock9.Lock9View.CallBack;
import com.studioninja.locker.R;

import butterknife.BindView;

public class AppLockScreenActivity extends BaseToolbarActivity {
    private int countFailed;
    private ImagesDatabaseHelper imagesDatabaseHelper;
    @BindView(R.id.layout)
    View layout;
    @BindView(R.id.layout_lock)
    View layout_lock;
    @BindView(R.id.lock_view)
    Lock9View lock_view;
    @BindView(R.id.lock_view_disvibrate)
    Lock9View lock_view_disvibrate;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;
    AdRequest adRequest;
    AdView adView;

    public int getLayoutId() {
        return R.layout.activity_app_lock_screen;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.sharedPreferences = getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0);
        this.imagesDatabaseHelper = ImagesDatabaseHelper.getInstance(this);




        this.lock_view.setCallBack(new CallBack() {
            public void onFinish(String password) {
                AppLockScreenActivity.this.finish(password);
            }
        });
        this.lock_view_disvibrate.setCallBack(new CallBack() {
            public void onFinish(String password) {
                AppLockScreenActivity.this.finish(password);
            }
        });
        if (this.sharedPreferences.getBoolean(AppLockSettingsActivity.KEY_VIBRATE, false)) {
            this.lock_view.setVisibility(0);
            this.lock_view_disvibrate.setVisibility(8);
        } else {
            this.lock_view.setVisibility(8);
            this.lock_view_disvibrate.setVisibility(0);
        }
        this.tv_forgot_password.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AppLockScreenActivity.this.startActivity(new Intent(AppLockScreenActivity.this, AppLockForgotPasswordActivity.class));
            }
        });


    }

    private void finish(String password) {
        if (password.equals(this.sharedPreferences.getString(AppLockCreatePasswordActivity.KEY_PASSWORD, null))) {
            startActivity(new Intent(this, AppLockHomeActivity.class));
            if (this.sharedPreferences.getBoolean(LockService.KEY_THIEVES, false)) {
                Image image = this.imagesDatabaseHelper.findByID(this.sharedPreferences.getLong(LockService.KEY_APP_THIEVES, -1));
                if (image != null) {
                    Intent intent = new Intent(this, AppLockImageActivity.class);
                    intent.putExtra("data", image.getId());
                    Utils.notificateAppLock(this, LockService.NOTIFICATION_ID_APP_LOCK, R.mipmap.ic_thieves, "Someone tries to open your app.", image.getAppName(), "Someone tries to open your app.", intent);
                }
            }
            this.sharedPreferences.edit().putBoolean(LockService.KEY_THIEVES, false).apply();
            finish();
            return;
        }
        this.countFailed++;
        if (this.countFailed == 3 && this.sharedPreferences.getBoolean(AppLockSettingsActivity.KEY_SELFIE, false)) {
            new Selfie(this, getPackageName()).takePhoto();
        }
        Snackbar snackbar = Snackbar.make(this.layout, R.string.patterns_do_not_match, -1).setAction(getResources().getString(R.string.forgot_password), new OnClickListener() {
            public void onClick(View v) {
                AppLockScreenActivity.this.startActivity(new Intent(AppLockScreenActivity.this, AppLockForgotPasswordActivity.class));
            }
        });
        snackbar.setActionTextColor(-16711681);
        ((TextView) snackbar.getView().findViewById(R.id.snackbar_action)).setTextSize(2, 12.0f);
        snackbar.show();
    }
}
