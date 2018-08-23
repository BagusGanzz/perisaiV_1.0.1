package com.perisaimobile.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.perisaimobile.activities.SafeboosterActivity;
import com.perisaimobile.model.Application;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import java.util.List;


public class BoosterService extends Service {
    public static final String BROADCAST_ACCESSIBILITY = "BROADCAST_ACCESSIBILITY";
    public static final String PREFERENCES_LAST_TIME_BOOST = "LAST_TIME_BOOST";
    public static final String STOPPED = "STOPPED";
    private BroadcastReceiver accessibilityBroadcast = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            BoosterService.this.img_app_icon.startAnimation(BoosterService.this.animationset);
            BoosterService.this.animationset.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    BoosterService.access$206(BoosterService.this);
                    BoosterService.this.applications.remove(BoosterService.this.applications.size() - 1);
                    if (BoosterService.this.applications.isEmpty()) {
                        BoosterService.this.settings.edit().putLong(BoosterService.PREFERENCES_LAST_TIME_BOOST, Utils.getCurrentTime()).apply();
                        BoosterService.this.sendBroadcast(new Intent().setAction(MyAccessibilityService.BROADCAST_FORCE_STOP).putExtra(MyAccessibilityService.FORCE_STOP, false));
                        BoosterService.this.stopSelf();
                        return;
                    }
                    BoosterService.this.img_app_icon.setImageDrawable(Utils.getIconFromPackage(((Application) BoosterService.this.applications.get(BoosterService.this.index)).getPackageName(), BoosterService.this));
                    BoosterService.this.tv_progress.setText("Boosting " + (BoosterService.this.size - BoosterService.this.index) + "/" + BoosterService.this.size);
                    BoosterService.this.startActivity(Utils.AppDetailsIntent(((Application) BoosterService.this.applications.get(BoosterService.this.applications.size() - 1)).getPackageName()));
                }

                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    };
    private AnimationSet animationset;
    private List<Application> applications;
    private IBinder iBinder = new BoosterBinder();
    private ImageView img_app_icon;
    private ImageView img_bin;
    private ImageView img_close;
    private int index;
    private LayoutInflater layoutInflater;
    private LayoutParams params;
    private SharedPreferences settings;
    private int size;
    private TextView tv_progress;
    private View view;
    private WindowManager windowManager;

    public class BoosterBinder extends Binder {
        public BoosterService getService() {
            return BoosterService.this;
        }
    }

    static /* synthetic */ int access$206(BoosterService x0) {
        int i = x0.index - 1;
        x0.index = i;
        return i;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 2;
    }

    public IBinder onBind(Intent intent) {
        return this.iBinder;
    }

    public void onCreate() {
        super.onCreate();
        this.settings = getSharedPreferences("Settings", 0);
        registerReceiver(this.accessibilityBroadcast, new IntentFilter(BROADCAST_ACCESSIBILITY));
        this.windowManager = (WindowManager) getApplicationContext().getSystemService("window");
        if (VERSION.SDK_INT >= 19) {
            this.params = new LayoutParams(-1, -1, 2010, 67371020, -3);
        } else {
            this.params = new LayoutParams(-1, -1, 2010, 262156, -3);
        }
        this.params.gravity = 48;
        this.params.screenOrientation = 1;
        this.layoutInflater = (LayoutInflater) getSystemService("layout_inflater");
        this.view = this.layoutInflater.inflate(R.layout.booster_layout, null);
        this.img_close = (ImageView) this.view.findViewById(R.id.img_close);
        this.img_close.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Utils.isServiceRunning(BoosterService.this, LockService.class)) {
                    BoosterService.this.stopService(new Intent(BoosterService.this, LockService.class));
                }
                BoosterService.this.sendBroadcast(new Intent().setAction(MyAccessibilityService.BROADCAST_FORCE_STOP).putExtra(MyAccessibilityService.FORCE_STOP, false));
                BoosterService.this.sendBroadcast(new Intent().setAction(MyAccessibilityService.BROADCAST_BACK_TAP));
                BoosterService.this.stopSelf();
            }
        });
        this.img_app_icon = (ImageView) this.view.findViewById(R.id.img_app_icon);
        this.img_bin = (ImageView) this.view.findViewById(R.id.img_bin);
        this.tv_progress = (TextView) this.view.findViewById(R.id.tv_progress);
        this.view.findViewById(R.id.bg_animation_scan).setAnimation(AnimationUtils.loadAnimation(this, R.anim.bg_animation_scan));
        this.animationset = new AnimationSet(true);
        Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setStartOffset(250);
        scaleAnimation.setDuration(250);
        scaleAnimation.setFillAfter(true);
        Animation translateAnimation = new TranslateAnimation((float) this.img_app_icon.getLeft(), (float) this.img_app_icon.getLeft(), (float) this.img_app_icon.getTop(), (float) (this.img_app_icon.getTop() - 400));
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        this.animationset.addAnimation(scaleAnimation);
        this.animationset.addAnimation(translateAnimation);
    }

    public void boost(List<Application> applications) {
        this.windowManager.addView(this.view, this.params);
        this.applications = applications;
        this.size = applications.size();
        this.index = applications.size() - 1;
        this.img_app_icon.setImageDrawable(Utils.getIconFromPackage(((Application) applications.get(this.index)).getPackageName(), this));
        this.tv_progress.setText("Boosting " + (this.size - this.index) + "/" + this.size);
        sendBroadcast(new Intent().setAction(MyAccessibilityService.BROADCAST_FORCE_STOP).putExtra(MyAccessibilityService.FORCE_STOP, true));
        startActivity(Utils.AppDetailsIntent(((Application) applications.get(applications.size() - 1)).getPackageName()));
    }

    public void onDestroy() {
        unregisterReceiver(this.accessibilityBroadcast);
        sendBroadcast(new Intent().setAction(MyAccessibilityService.BROADCAST_FORCE_STOP).putExtra(MyAccessibilityService.FORCE_STOP, false));
        Toast.makeText(getApplicationContext(), "Phone Boosted", Toast.LENGTH_SHORT).show();
        try {
            if (this.view != null) {
                this.windowManager.removeView(this.view);
                Intent intent = new Intent(this, SafeboosterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } catch (Exception e) {
        }
        super.onDestroy();
    }
}
