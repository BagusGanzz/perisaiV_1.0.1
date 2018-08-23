package com.perisaimobile.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.perisaimobile.activities.AppLockCreatePasswordActivity;
import com.perisaimobile.activities.AppLockScreenActivity;
import com.perisaimobile.activities.IgnoredListActivity;
import com.perisaimobile.activities.PhoneInfoActivity;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.BuildConfig;
import com.studioninja.locker.R;

import butterknife.ButterKnife;

public abstract class BaseNavigationDrawerActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    static final /* synthetic */ boolean $assertionsDisabled = (!BaseNavigationDrawerActivity.class.desiredAssertionStatus());
    private DrawerLayout drawer_layout;
    private NavigationView nav_view;

    InterstitialAd mInterstitialAd;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_two);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(org.zakariya.stickyheaders.R.string.app_name);
        TypeFaceUttils.setNomal((Context) this, mTitle);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.nav_view = (NavigationView) findViewById(R.id.nav_view);
        this.drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        this.nav_view.setNavigationItemSelectedListener(this);
        changeTextFont();
        initMenu();

        AdRequest adRequest = new AdRequest.Builder().build();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intreal));
        mInterstitialAd.loadAd(adRequest);
    }

    private void changeTextFont() {
        TypeFaceUttils.setNomal((Context) this, (TextView) this.nav_view.findViewById(R.id.tv_show_ignored_list));
        TypeFaceUttils.setNomal((Context) this, (TextView) this.nav_view.findViewById(R.id.tv_auto_scan));
        TypeFaceUttils.setNomal((Context) this, (TextView) this.nav_view.findViewById(R.id.tv_app_lock));
        TypeFaceUttils.setNomal((Context) this, (TextView) this.nav_view.findViewById(R.id.tv_phone_info));
        TypeFaceUttils.setNomal((Context) this, (TextView) this.nav_view.findViewById(R.id.tv_feedback));
        TypeFaceUttils.setNomal((Context) this, (TextView) this.nav_view.findViewById(R.id.tv_rate_us));
        TypeFaceUttils.setNomal((Context) this, (TextView) this.nav_view.findViewById(R.id.tv_more_app));
        TextView tvVersion = (TextView) this.nav_view.findViewById(R.id.tv_version);
        TypeFaceUttils.setNomal((Context) this, tvVersion);
        tvVersion.setText(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
    }

    private void initMenu() {
        this.nav_view.findViewById(R.id.menu_phone_info).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mInterstitialAd.show();
                BaseNavigationDrawerActivity.this.startActivity(new Intent(BaseNavigationDrawerActivity.this, PhoneInfoActivity.class));
                BaseNavigationDrawerActivity.this.drawer_layout.closeDrawer(8388611);
            }
        });
        this.nav_view.findViewById(R.id.menu_show_ignored_list).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BaseNavigationDrawerActivity.this.startActivity(new Intent(BaseNavigationDrawerActivity.this, IgnoredListActivity.class));
                BaseNavigationDrawerActivity.this.drawer_layout.closeDrawer(8388611);
            }
        });
        final SharedPreferences settings = getSharedPreferences("Settings", 0);
        ToggleButton toggleAutoScan = (ToggleButton) this.nav_view.findViewById(R.id.toggle_auto_scan);
        toggleAutoScan.setChecked(settings.getBoolean("auto_scan", true));
        toggleAutoScan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Editor editor = settings.edit();
                if (isChecked) {
                    editor.putBoolean("auto_scan", true);
                    editor.apply();
                    return;
                }
                editor.putBoolean("auto_scan", false);
                editor.apply();
            }
        });
        View appLock = findViewById(R.id.menu_app_lock);
        final SharedPreferences appLockSettings = getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0);
        appLock.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (VERSION.SDK_INT >= 22 && !Utils.isUsageAccessEnabled(BaseNavigationDrawerActivity.this)) {
                    Utils.openUsageAccessSetings(BaseNavigationDrawerActivity.this);
                } else if (VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(BaseNavigationDrawerActivity.this)) {
                    BaseNavigationDrawerActivity.this.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + BaseNavigationDrawerActivity.this.getPackageName())));
                } else if (Utils.isAccessibilitySettingsOn(BaseNavigationDrawerActivity.this)) {
                    if (appLockSettings.getString(AppLockCreatePasswordActivity.KEY_PASSWORD, null) != null) {
                        BaseNavigationDrawerActivity.this.startActivity(new Intent(BaseNavigationDrawerActivity.this, AppLockScreenActivity.class));
                    } else {
                        BaseNavigationDrawerActivity.this.startActivity(new Intent(BaseNavigationDrawerActivity.this, AppLockCreatePasswordActivity.class));
                    }
                    BaseNavigationDrawerActivity.this.drawer_layout.closeDrawer(8388611);
                } else {
                    BaseNavigationDrawerActivity.this.startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                    Dialog dialog = new Dialog(BaseNavigationDrawerActivity.this);
                    dialog.getWindow().setType(2003);
                    dialog.requestWindowFeature(1);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.setContentView(R.layout.dialog_guide_accessibility);
                    dialog.show();
                }
            }
        });
        this.nav_view.findViewById(R.id.menu_rate_us).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("market://details?id=" + BaseNavigationDrawerActivity.this.getPackageName()));
                BaseNavigationDrawerActivity.this.startActivity(intent);
                BaseNavigationDrawerActivity.this.drawer_layout.closeDrawer(8388611);
            }
        });
        this.nav_view.findViewById(R.id.menu_more_app).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = BaseNavigationDrawerActivity.this.getPackageManager().getLaunchIntentForPackage("com.android.vending");
                if (intent != null) {
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    BaseNavigationDrawerActivity.this.startActivity(intent);
                } else {
                    BaseNavigationDrawerActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                BaseNavigationDrawerActivity.this.drawer_layout.closeDrawer(8388611);
            }
        });
        this.nav_view.findViewById(R.id.menu_feedback).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent emailIntent = new Intent("android.intent.action.SENDTO");
                emailIntent.setData(Uri.parse("mailto: "));
                BaseNavigationDrawerActivity.this.startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                BaseNavigationDrawerActivity.this.drawer_layout.closeDrawer(8388611);
            }
        });
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!$assertionsDisabled && drawer == null) {
            throw new AssertionError();
        } else if (drawer.isDrawerOpen(8388611)) {
            drawer.closeDrawer(8388611);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if ($assertionsDisabled || drawer != null) {
            drawer.closeDrawer(8388611);
            return true;
        }
        throw new AssertionError();
    }
}
