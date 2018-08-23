package com.perisaimobile.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.fragment.ApplicationFragment;
import com.perisaimobile.fragment.JunkFilesFragment;
import com.perisaimobile.fragment.PhoneBoostFragment;
import com.perisaimobile.fragment.ProblemDetailFragment;
import com.perisaimobile.iface.Communicator;
import com.perisaimobile.iface.IProblem;
import com.perisaimobile.model.AppLock;
import com.perisaimobile.model.Application;
import com.perisaimobile.model.JunkOfApplication;
import com.perisaimobile.service.MonitorShieldService;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import java.util.List;

import butterknife.BindView;

public class ScanningResultActivity extends BaseToolbarActivity implements OnClickListener, Communicator {
    public static final String PROBLEM_DETAIL_FRAGMENT_TAG = "PROBLEM_DETAIL";
    public static final String RESULT_APPLICATION_FRAGMENT_TAG = "APPLICATION";
    public static final String RESULT_APPLOCK_FRAGMENT_TAG = "APPLOCK";
    public static final String RESULT_JUNKS_FRAGMENT_TAG = "JUNKS";
    public static final String RESULT_PHONEBOOST_FRAGMENT_TAG = "PHONEBOOST";
    private boolean app_skip_all;
    private boolean bound;
    private IProblem comu;
    @BindView(R.id.img_app_lock)
    ImageView img_app_lock;
    @BindView(R.id.img_app_lock_1)
    ImageView img_app_lock_1;
    @BindView(R.id.img_app_lock_2)
    ImageView img_app_lock_2;
    @BindView(R.id.img_app_lock_3)
    ImageView img_app_lock_3;
    @BindView(R.id.img_application)
    ImageView img_application;
    @BindView(R.id.img_application_1)
    ImageView img_application_1;
    @BindView(R.id.img_application_2)
    ImageView img_application_2;
    @BindView(R.id.img_application_3)
    ImageView img_application_3;
    private MonitorShieldService monitorShieldService;
    @BindView(R.id.result_app_lock)
    View result_app_lock;
    @BindView(R.id.result_application)
    View result_application;
    @BindView(R.id.result_booster)
    View result_booster;
    @BindView(R.id.result_junk_files)
    View result_junk_files;
    @BindView(R.id.result_layout)
    View result_layout;
    @BindView(R.id.scanning_result_layout)
    View scanning_result_layout;

    @BindView(R.id.card_view_recommend_app)
    CardView card_view_recommend_app;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            ScanningResultActivity.this.bound = true;
            ScanningResultActivity.this.monitorShieldService = ((MonitorShieldService.MonitorShieldLocalBinder) service).getServiceInstance();
            new Init().execute(new Void[0]);
        }

        public void onServiceDisconnected(ComponentName name) {
            ScanningResultActivity.this.bound = false;
            ScanningResultActivity.this.monitorShieldService = null;
        }
    };
    private boolean showAd = true;
    @BindView(R.id.tv_app_lock)
    TextView tv_app_lock;
    @BindView(R.id.tv_application)
    TextView tv_application;
    @BindView(R.id.tv_detecting_dangerous)
    TextView tv_detecting_dangerous;
    @BindView(R.id.tv_freeable_memory)
    TextView tv_freeable_memory;
    @BindView(R.id.tv_junk_files_size)
    TextView tv_junk_files_size;
    @BindView(R.id.tv_junk_found)
    TextView tv_junk_found;
    @BindView(R.id.tv_mb_junk_files)
    TextView tv_mb_junk_files;
    @BindView(R.id.tv_mb_phone_boost)
    TextView tv_mb_phone_boost;
    @BindView(R.id.tv_num_of_issues)
    TextView tv_num_of_issues;
    @BindView(R.id.tv_phone_boost)
    TextView tv_phone_boost;
    @BindView(R.id.tv_title_application)
    TextView tv_title_application;
    @BindView(R.id.tv_title_junk_files)
    TextView tv_title_junk_files;
    @BindView(R.id.tv_title_phone_boost)
    TextView tv_title_phone_boost;

    private class Init extends AsyncTask<Void, Integer, Void> {
        int item;

        private Init() {
            this.item = 1;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ScanningResultActivity.this.tv_num_of_issues.setText(ScanningResultActivity.this.getResources().getString(R.string.found) + " " + ScanningResultActivity.this.monitorShieldService.getMenacesCacheSet().getItemCount() + " " + ScanningResultActivity.this.getResources().getString(R.string.issues));
            ScanningResultActivity.this.result_app_lock.setVisibility(8);
            ScanningResultActivity.this.result_application.setVisibility(8);
            ScanningResultActivity.this.result_booster.setVisibility(8);
            ScanningResultActivity.this.result_junk_files.setVisibility(8);
        }

        protected Void doInBackground(Void... params) {
            int countApp = 0;
            for (AppLock appLock : ScanningResultActivity.this.monitorShieldService.getAppLock()) {
                if (appLock.isRecommend()) {
                    countApp++;
                }
            }
            publishProgress(new Integer[]{Integer.valueOf(countApp)});
            publishProgress(new Integer[]{Integer.valueOf(ScanningResultActivity.this.monitorShieldService.getMenacesCacheSet().getItemCount())});
            int totalBoost = 0;
            for (Application application : ScanningResultActivity.this.monitorShieldService.getRunningApplications()) {
                totalBoost = (int) (((long) totalBoost) + (application.getSize() / 1024));
            }
            long cacheSize = 0;
            for (JunkOfApplication junkOfApplication : Utils.junkOfApplications) {
                cacheSize += junkOfApplication.getCacheSize();
            }
            publishProgress(new Integer[]{Integer.valueOf(totalBoost), Integer.valueOf(((int) cacheSize) / 1048576)});
            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            switch (this.item) {
                case 1 /*1*/:
                    if (values[0].intValue() != 0) {
                        ScanningResultActivity.this.tv_app_lock.setText(Html.fromHtml(ScanningResultActivity.this.getResources().getString(R.string.more_than) + " " + ("<font color='#cc0000'>" + values[0] + "</font>") + " " + ScanningResultActivity.this.getResources().getString(R.string.apps_with_privacy_issues) + "."));
                        ScanningResultActivity.this.result_app_lock.setVisibility(0);
                    } else {
                        ScanningResultActivity.this.result_app_lock.setVisibility(8);
                    }
                    this.item++;
                    return;
                case 2 /*2*/:
                    if (values[0].intValue() == 0 || ScanningResultActivity.this.app_skip_all) {
                        ScanningResultActivity.this.result_application.setVisibility(8);
                        ScanningResultActivity.this.tv_application.setText(String.valueOf(Utils.numOfPackages));
                    } else {
                        ScanningResultActivity.this.result_application.setVisibility(0);
                        ScanningResultActivity.this.tv_application.setText(String.valueOf(Utils.numOfPackages));
                    }
                    this.item++;
                    return;
                case 3 /*3*/:
                    if (values[0].intValue() == 0) {
                        ScanningResultActivity.this.result_booster.setVisibility(8);
                    } else {
                        ScanningResultActivity.this.result_booster.setVisibility(0);
                        ScanningResultActivity.this.tv_phone_boost.setText(String.valueOf(values[0]));
                    }
                    if (values[1].intValue() == 0) {
                        ScanningResultActivity.this.result_junk_files.setVisibility(8);
                    } else {
                        ScanningResultActivity.this.result_junk_files.setVisibility(0);
                        ScanningResultActivity.this.tv_junk_files_size.setText(String.valueOf(values[1]));
                    }
                    this.item++;
                    return;
                default:
                    return;
            }
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class LoadIcon extends AsyncTask<Void, Drawable, Void> {
        private int count;

        private LoadIcon() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            ScanningResultActivity.this.img_app_lock.setImageResource(R.mipmap.ic_launcher);
            ScanningResultActivity.this.img_app_lock_1.setImageResource(R.mipmap.ic_launcher);
            ScanningResultActivity.this.img_app_lock_2.setImageResource(R.mipmap.ic_launcher);
            ScanningResultActivity.this.img_app_lock_3.setImageResource(R.mipmap.ic_launcher);
            ScanningResultActivity.this.img_application.setImageResource(R.mipmap.ic_launcher);
            ScanningResultActivity.this.img_application_1.setImageResource(R.mipmap.ic_launcher);
            ScanningResultActivity.this.img_application_2.setImageResource(R.mipmap.ic_launcher);
            ScanningResultActivity.this.img_application_3.setImageResource(R.mipmap.ic_launcher);
        }

        protected Void doInBackground(Void... params) {
            List<PackageInfo> nonSystemApps = Utils.getNonSystemApps(ScanningResultActivity.this, Utils.getApps(ScanningResultActivity.this, 4097));
            int maxSize = nonSystemApps.size();
            if (maxSize != 0) {
                Drawable drawable = Utils.getIconFromPackage(((PackageInfo) nonSystemApps.get((int) (Math.random() * ((double) maxSize)))).packageName, ScanningResultActivity.this);
                publishProgress(new Drawable[]{drawable});
                Drawable drawable_1 = Utils.getIconFromPackage(((PackageInfo) nonSystemApps.get((int) (Math.random() * ((double) maxSize)))).packageName, ScanningResultActivity.this);
                publishProgress(new Drawable[]{drawable_1});
                Drawable drawable_2 = Utils.getIconFromPackage(((PackageInfo) nonSystemApps.get((int) (Math.random() * ((double) maxSize)))).packageName, ScanningResultActivity.this);
                publishProgress(new Drawable[]{drawable_2});
                Drawable drawable_3 = Utils.getIconFromPackage(((PackageInfo) nonSystemApps.get((int) (Math.random() * ((double) maxSize)))).packageName, ScanningResultActivity.this);
                publishProgress(new Drawable[]{drawable_3});
            }
            return null;
        }

        protected void onProgressUpdate(Drawable... values) {
            super.onProgressUpdate(values);
            switch (this.count) {
                case org.zakariya.stickyheaders.R.styleable.RecyclerView_android_orientation /*0*/:
                    ScanningResultActivity.this.img_application.setImageDrawable(values[0]);

                    this.count++;
                    return;
                case 1 /*1*/:
                    ScanningResultActivity.this.img_application_1.setImageDrawable(values[0]);

                    this.count++;
                    return;
                case org.zakariya.stickyheaders.R.styleable.RecyclerView_layoutManager /*2*/:
                    ScanningResultActivity.this.img_application_2.setImageDrawable(values[0]);

                    this.count++;
                    return;
                case org.zakariya.stickyheaders.R.styleable.RecyclerView_spanCount /*3*/:
                    ScanningResultActivity.this.img_application_3.setImageDrawable(values[0]);

                    return;
                default:
                    return;
            }
        }
    }

    public void setBackground(int id) {
        this.scanning_result_layout.setBackgroundResource(id);
    }

    public void setShowAd(boolean showAd) {
        this.showAd = showAd;
    }

    public IProblem getComu() {
        return this.comu;
    }

    public void setComu(IProblem comu) {
        this.comu = comu;
    }

    public void setApp_skip_all(boolean app_skip_all) {
        this.app_skip_all = app_skip_all;
    }

    private void customFont() {

        TypeFaceUttils.setNomal((Context) this, this.tv_detecting_dangerous);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_application);
        TypeFaceUttils.setNomal((Context) this, this.tv_application);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_phone_boost);
        TypeFaceUttils.setNomal((Context) this, this.tv_phone_boost);
        TypeFaceUttils.setNomal((Context) this, this.tv_mb_phone_boost);
        TypeFaceUttils.setNomal((Context) this, this.tv_freeable_memory);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_junk_files);
        TypeFaceUttils.setNomal((Context) this, this.tv_junk_found);
        TypeFaceUttils.setNomal((Context) this, this.tv_junk_files_size);
        TypeFaceUttils.setNomal((Context) this, this.tv_mb_junk_files);
        TypeFaceUttils.setNomal((Context) this, this.tv_num_of_issues);
    }

    public MonitorShieldService getMonitorShieldService() {
        return this.monitorShieldService;
    }

    public int getLayoutId() {
        return R.layout.activity_scanning_result;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AdRequest adRequest = new AdRequest.Builder().build();


        ((NativeExpressAdView) findViewById(R.id.adView)).loadAd(adRequest);
        getSupportFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
            public void onBackStackChanged() {
                if (ScanningResultActivity.this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    ScanningResultActivity.this.result_layout.setVisibility(0);
                } else {
                    ScanningResultActivity.this.result_layout.setVisibility(8);
                }
            }
        });
        customFont();
        new LoadIcon().execute(new Void[0]);
        this.result_application.setOnClickListener(this);
        this.result_booster.setOnClickListener(this);
        this.result_junk_files.setOnClickListener(this);



        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intreal));
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                card_view_recommend_app.setVisibility(View.VISIBLE);
                if (mInterstitialAd.isLoaded() && ScanningResultActivity.this.showAd) {
                    mInterstitialAd.show();
                }
            }
        });
    }

    public void respond(IProblem iProblem, int type) {
        ApplicationFragment fragment = (ApplicationFragment) getSupportFragmentManager().findFragmentByTag(RESULT_APPLICATION_FRAGMENT_TAG);
        if (fragment != null) {
            fragment.deleteItem(iProblem, type);
        }
    }

    private boolean isSafeSate() {
        boolean application = this.monitorShieldService.getMenacesCacheSet().getItemCount() == 0;
        if (this.app_skip_all) {
            application = true;
        }
        boolean phoneBoost = this.monitorShieldService.getRunningApplications().size() == 0;
        long cacheSize = 0;
        for (JunkOfApplication junkOfApplication : Utils.junkOfApplications) {
            cacheSize += junkOfApplication.getCacheSize();
        }
        boolean junk = cacheSize / 1048576 == 0;
        int countApp = 0;
        for (AppLock appLock : this.monitorShieldService.getAppLock()) {
            if (appLock.isRecommend()) {
                countApp++;
            }
        }
        return application && phoneBoost && junk && (countApp == 0);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.result_application /*2131820818*/:
                slideInFragment(RESULT_APPLICATION_FRAGMENT_TAG);
                return;
            case R.id.result_booster /*2131820826*/:
                slideInFragment(RESULT_PHONEBOOST_FRAGMENT_TAG);
                return;
            case R.id.result_junk_files /*2131820831*/:
                slideInFragment(RESULT_JUNKS_FRAGMENT_TAG);
                return;
            default:
                return;
        }
    }

    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, MonitorShieldService.class), this.serviceConnection, 1);
    }

    protected void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.scanning_result_layout.setBackgroundResource(R.drawable.background_danger);
    }



    public PhoneBoostFragment getPhoneBoostFragment() {
        PhoneBoostFragment phoneBoostFragment = (PhoneBoostFragment) getSupportFragmentManager().findFragmentByTag(RESULT_PHONEBOOST_FRAGMENT_TAG);
        if (phoneBoostFragment == null) {
            return new PhoneBoostFragment();
        }
        return phoneBoostFragment;
    }

    public JunkFilesFragment getJunkFilesFragment() {
        JunkFilesFragment junkFilesFragment = (JunkFilesFragment) getSupportFragmentManager().findFragmentByTag(RESULT_JUNKS_FRAGMENT_TAG);
        if (junkFilesFragment == null) {
            return new JunkFilesFragment();
        }
        return junkFilesFragment;
    }

    public ApplicationFragment getApplicationFragment() {
        ApplicationFragment applicationFragment = (ApplicationFragment) getSupportFragmentManager().findFragmentByTag(RESULT_APPLICATION_FRAGMENT_TAG);
        if (applicationFragment == null) {
            return new ApplicationFragment();
        }
        return applicationFragment;
    }

    public ProblemDetailFragment getProblemDetailFragment() {
        ProblemDetailFragment problemDetailFragment = (ProblemDetailFragment) getSupportFragmentManager().findFragmentByTag(PROBLEM_DETAIL_FRAGMENT_TAG);
        if (problemDetailFragment == null) {
            return new ProblemDetailFragment();
        }
        return problemDetailFragment;
    }

    public Fragment slideInFragment(String fragmentId) {
        Fragment fmt = getSupportFragmentManager().findFragmentByTag(fragmentId);
        if (fmt != null && fmt.isVisible()) {
            return null;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        Fragment f = null;
        int obj = -1;
        switch (fragmentId.hashCode()) {
            case -1837744623:
                if (fragmentId.equals(PROBLEM_DETAIL_FRAGMENT_TAG)) {
                    obj = 4;
                    break;
                }
                break;
            case -1336211723:
                if (fragmentId.equals(RESULT_PHONEBOOST_FRAGMENT_TAG)) {
                    obj = 1;
                    break;
                }
                break;
            case -587753168:
                if (fragmentId.equals(RESULT_APPLICATION_FRAGMENT_TAG)) {
                    obj = 3;
                    break;
                }
                break;
            case -75246932:
                if (fragmentId.equals(RESULT_APPLOCK_FRAGMENT_TAG)) {
                    obj = 0;
                    break;
                }
                break;
            case 70950155:
                if (fragmentId.equals(RESULT_JUNKS_FRAGMENT_TAG)) {
                    obj = 2;
                    break;
                }
                break;
        }
        switch (obj) {
            case 0 /*0*/:

                break;
            case 1 /*1*/:
                f = getPhoneBoostFragment();
                break;
            case 2 /*2*/:
                f = getJunkFilesFragment();
                break;
            case 3 /*3*/:
                f = getApplicationFragment();
                break;
            case 4 /*4*/:
                f = getProblemDetailFragment();
                break;
        }
        transaction.replace(R.id.container_fragment, f, fragmentId);
        transaction.addToBackStack(fragmentId);
        transaction.commitAllowingStateLoss();
        return f;
    }

//    public Fragment shareElementFragment(Fragment fragmentOne, ImageView startImage) {
//        Fragment f = getProblemDetailFragment();
//        if (VERSION.SDK_INT >= 21) {
//            Transition changeTransform = TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform);
//            Transition explodeTransform = TransitionInflater.from(this).inflateTransition(17760259);
//            fragmentOne.setSharedElementReturnTransition(changeTransform);
//            fragmentOne.setExitTransition(explodeTransform);
//            f.setSharedElementEnterTransition(changeTransform);
//            f.setEnterTransition(explodeTransform);
//            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, f, PROBLEM_DETAIL_FRAGMENT_TAG).addToBackStack(PROBLEM_DETAIL_FRAGMENT_TAG).addSharedElement(startImage, getString(R.string.transition_problem_to_detail)).commitAllowingStateLoss();
//            return f;
//        }
//        Fragment fmt = getSupportFragmentManager().findFragmentByTag(PROBLEM_DETAIL_FRAGMENT_TAG);
//        if (fmt != null && fmt.isVisible()) {
//            return null;
//        }
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(17432578, 17432579, 17432578, 17432579);
//        transaction.replace(R.id.container_fragment, f, PROBLEM_DETAIL_FRAGMENT_TAG);
//        transaction.addToBackStack(PROBLEM_DETAIL_FRAGMENT_TAG);
//        transaction.commitAllowingStateLoss();
//        return f;
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
        }
        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.bound && this.monitorShieldService != null) {
            unbindService(this.serviceConnection);
            this.bound = false;
        }
    }

    public void refresh() {
        if (isSafeSate()) {
            if (getSharedPreferences("Settings", 0).getInt("rate", 0) == 0) {
                getSharedPreferences("Settings", 0).edit().putInt("rate", 1).apply();
            }
            startActivity(new Intent(this, SafeActivity.class));
            finish();
            return;
        }
        new Init().execute(new Void[0]);
    }
}
