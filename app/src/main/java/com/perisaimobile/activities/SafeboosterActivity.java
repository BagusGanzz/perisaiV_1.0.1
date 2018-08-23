package com.perisaimobile.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.perisaimobile.base.BaseToolbarActivity;
import com.studioninja.locker.R;

public class SafeboosterActivity extends BaseToolbarActivity {

    private ImageView img_rocket;
    private View ll_done;
    CardView cardView;

    InterstitialAd mInterstitialAd;
    NativeExpressAdView mAdView;
    public int getLayoutId() {
        return R.layout.activity_safebooster;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Locate the NativeExpressAdView.
        mAdView = (NativeExpressAdView) findViewById(R.id.adView);



        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(new AdRequest.Builder().build());
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intreal));
        mInterstitialAd.loadAd(adRequest);


        this.ll_done = findViewById(R.id.ll_done);
        this.img_rocket = (ImageView) findViewById(R.id.img_rocket);
        cardView = (CardView) findViewById(R.id.card_view_recommend_app);
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flip_left_in);
        set.setTarget(this.img_rocket);


        final Animation animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                animShow.setDuration(1000);

            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.ll_done.startAnimation(animation);
        set.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent browserIntent2 = new Intent(SafeboosterActivity.this, MainActivityantivirus.class);
        startActivity(browserIntent2);
        mInterstitialAd.show();

    }
}
