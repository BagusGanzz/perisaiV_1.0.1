package com.perisaimobile.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.perisaimobile.service.MonitorShieldService;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;


public class SplishActivity  extends AwesomeSplash {
    public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    TextView appName;
    private Animation mFadeIn;
    private Animation mFadeInScale;
    private Animation mFadeOut;
    ImageView mImageView;
    TextView textCopyRight;
    TextView title;

    @Override
    public void initSplash(ConfigSplash configSplash){

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        configSplash.setBackgroundColor(R.color.bg_splash);
        configSplash.setAnimCircularRevealDuration(1000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagX(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.drawable.splash_logo_2);
        configSplash.setAnimLogoSplashDuration(1000);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeInDown);

        configSplash.setTitleSplash("P E R I S A I");
        configSplash.setTitleTextColor(R.color.text_splash);
        configSplash.setTitleFont("fonts/FireyeGF_3_Bold.ttf");
        configSplash.setTitleTextSize(25f);
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FadeInRight);
    }

    @Override
    public void animationsFinished(){
        Intent intent = new Intent(SplishActivity.this, IntroSlider.class);
        startActivity(intent);
        SplishActivity.this.finish();

        Intent i = new Intent(getApplicationContext(), MonitorShieldService.class);
        if (!Utils.isServiceRunning(getApplicationContext(), MonitorShieldService.class)){
            stopService(i);
        }
    }


//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splish);
//        this.mImageView = (ImageView) findViewById(R.id.image);
//        this.appName = (TextView) findViewById(R.id.app_name);
//
////        if (!SharedPreferencesUtils.isShortCut(this.mContext).booleanValue()) {
////            createShortCut();
////        }
//
//
//        initAnim();
//        setListener();
//    }

//    private void initAnim() {
//        this.mFadeIn = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in);
//        this.mFadeIn.setDuration(500);
//        this.mFadeInScale = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in_scale);
//        this.mFadeInScale.setDuration(2000);
//        this.mFadeOut = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_out);
//        this.mFadeOut.setDuration(500);
//        this.mImageView.startAnimation(this.mFadeIn);
//    }

//    public void setListener() {
//        this.mFadeIn.setAnimationListener(new Animation.AnimationListener() {
//            public void onAnimationStart(Animation animation) {
//            }
//
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            public void onAnimationEnd(Animation animation) {
//                SplishActivity.this.mImageView.startAnimation(SplishActivity.this.mFadeInScale);
//            }
//        });
//        this.mFadeInScale.setAnimationListener(new Animation.AnimationListener() {
//            public void onAnimationStart(Animation animation) {
//            }
//
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            public void onAnimationEnd(Animation animation) {
//
//                Intent intent = new Intent(SplishActivity.this, MainActivityantivirus.class);
//                startActivity(intent);
//                SplishActivity.this.finish();
//
//
//                Intent i = new Intent(getApplicationContext(), MonitorShieldService.class);
//                if (!Utils.isServiceRunning(getApplicationContext(), MonitorShieldService.class)) {
//                    stopService(i);
//                }
//            }
//        });
//        this.mFadeOut.setAnimationListener(new Animation.AnimationListener() {
//            public void onAnimationStart(Animation animation) {
//            }
//
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            public void onAnimationEnd(Animation animation) {
//            }
//        });
//    }
}
