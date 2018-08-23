package com.perisaimobile.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.perisaimobile.model.AppData;
import com.perisaimobile.service.MonitorShieldService;
import com.perisaimobile.uninstaller;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.studioninja.battery.BatteryInfoActivity;
import com.studioninja.locker.R;
import com.studioninja.locker.ui.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivityantivirus extends AppCompatActivity {

    private AdView mAdView;

    @BindView(R.id.bg_animation_scan)
    ImageView bg_animation_scan;
    boolean bound = false;
    @BindView(R.id.img_resolvep_roblems)
    ImageView img_resolvep_roblems;
    @BindView(R.id.iv_start_scan_anim)
    ImageView iv_start_scan;
    private MonitorShieldService monitorShieldService;
    @BindView(R.id.noti_danger)
    View noti_danger;
    @BindView(R.id.notifi_safe)
    View notifi_safe;
    @BindView(R.id.img_threat)
    ImageView img_threat;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainActivityantivirus.this.bound = true;
            MainActivityantivirus.this.monitorShieldService = ((MonitorShieldService.MonitorShieldLocalBinder) service).getServiceInstance();
            MainActivityantivirus.this.initView();
        }

        public void onServiceDisconnected(ComponentName name) {
            MainActivityantivirus.this.monitorShieldService = null;
            MainActivityantivirus.this.bound = false;
        }
    };
    @BindView(R.id.tv_app_system)
    TextView tv_app_system;
    @BindView(R.id.tv_danger)
    TextView tv_danger;
    @BindView(R.id.tv_first_run)
    TextView tv_first_run;
    @BindView(R.id.tv_found_problem)
    TextView tv_found_problem;

    @BindView(R.id.tv_safe)
    TextView tv_safe;
    @BindView(R.id.tv_scan)
    TextView tv_scan;
    private View view;
    SharedPreferences appLockSettings;

    AdRequest adRequest;
    AdView adView;


    @OnClick({R.id.batteryx})
    void batteryx() {
        Intent browserIntent2 = new Intent(MainActivityantivirus.this, BatteryInfoActivity.class);
        startActivity(browserIntent2);
    }

    @OnClick({R.id.button5})
    void appmanager() {
        Intent browserIntent2 = new Intent(MainActivityantivirus.this, uninstaller.class);
        startActivity(browserIntent2);
    }

    @OnClick({R.id.button2})
    void infosdevice() {
        Intent browserIntent2 = new Intent(MainActivityantivirus.this, PhoneInfoActivity.class);
        startActivity(browserIntent2);
    }

    @OnClick({R.id.bg_animation_scan})
    void onStartScan() {
        startActivity(new Intent(this, ScanningActivity.class));
    }

    @OnClick({R.id.img_threat})
    void onPhoneInfo() {
        startActivity(new Intent(this, PhoneInfoActivity.class));
    }

    @OnClick({R.id.button4})
    void onStartAppLock() {
        Intent testbutton = new Intent(MainActivityantivirus.this, MainActivity.class);
        startActivity(testbutton);
    }

    @OnClick({R.id.img_booster})
    void onRate() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("market://details?id=" + getPackageName()));
        startActivity(intent);
    }

    //-------------------------------------------------------------------------------------------//
    @OnClick({R.id.soon1})
    void onSoon1(){
        Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.soon2})
    void onSoon2(){
        Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.soon3})
    void onSoon3(){
        Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
    }
    //------------------------------------------------------------------------------------------//

    private void customFont() {
        TypeFaceUttils.setNomal((Context) this, this.tv_scan);
        TypeFaceUttils.setNomal((Context) this, this.tv_app_system);
        //  TypeFaceUttils.setNomal((Context) this, this.tv_storage);
        // TypeFaceUttils.setNomal((Context) this, this.tv_memory);
        // TypeFaceUttils.setNomal((Context) this, this.tv_percent_storage);
        // TypeFaceUttils.setNomal((Context) this, this.tv_percent_memory);
        //  TypeFaceUttils.setNomal((Context) this, this.tv_info_storage);
        //  TypeFaceUttils.setNomal((Context) this, this.tv_info_memory);
        TypeFaceUttils.setNomal((Context) this, this.tv_first_run);
        TypeFaceUttils.setNomal((Context) this, this.tv_safe);
        TypeFaceUttils.setNomal((Context) this, this.tv_danger);
        TypeFaceUttils.setNomal((Context) this, this.tv_found_problem);
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_bar_main);

        appLockSettings = getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0);

        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT >= 23){
            if(!Settings.canDrawOverlays(this)){

            }
        }
        adRequest = new AdRequest.Builder().build();
        adView = (AdView) findViewById(R.id.adView);
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });

        // call first method...
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
      //  collapsingToolbarLayout.setTitle(getString(R.string.drawer_item_collapsing_toolbar_drawer));

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withHeaderBackground(R.drawable.finalmenu)
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withFullscreen(true)
                .addDrawerItems(

                        new SectionDrawerItem().withName(R.string.menu),
                        new PrimaryDrawerItem().withName(R.string.Home).withIcon(R.drawable.homee).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.IgnoredList).withIcon(R.drawable.ignoredlist).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.InfosDevice).withIcon(R.drawable.infosdevice).withIdentifier(4),

                        new SectionDrawerItem().withName(R.string.Help),
                        new PrimaryDrawerItem().withName(R.string.Privacy).withIcon(R.drawable.privacyicone).withIdentifier(5),
                        new PrimaryDrawerItem().withName(R.string.Share).withIcon(R.drawable.sharee).withIdentifier(6),
                        new PrimaryDrawerItem().withName(R.string.Rate).withIcon(R.drawable.ratee).withIdentifier(7)
                )

                .withSelectedItemByPosition(1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


                        if (drawerItem.getIdentifier() == 1) {
                            // Toast.makeText(MainActivityantivirus.this,"2",Toast.LENGTH_LONG).show();


                            result.closeDrawer();




                        }else if (drawerItem.getIdentifier() == 3) {

                            // Toast.makeText(MainActivityantivirus.this,"2",Toast.LENGTH_LONG).show();
                            Intent browserIntent2 = new Intent(MainActivityantivirus.this, IgnoredListActivity.class);
                            startActivity(browserIntent2);
                            result.closeDrawer();

                        } else if (drawerItem.getIdentifier() == 4) {
                            // Toast.makeText(MainActivityantivirus.this,"2",Toast.LENGTH_LONG).show();
                            Intent browserIntent2 = new Intent(MainActivityantivirus.this, PhoneInfoActivity.class);
                            startActivity(browserIntent2);
                            result.closeDrawer();

                        } else if (drawerItem.getIdentifier() == 5) {
                            // Toast.makeText(MainActivityantivirus.this,"2",Toast.LENGTH_LONG).show();
                            try {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://privacyperisaimobile.tumblr.com"));
                                startActivity(browserIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            result.closeDrawer();


                        } else if (drawerItem.getIdentifier() == 6) {

                            Intent myapp = new Intent(Intent.ACTION_SEND);
                            myapp.setType("text/plain");
                            myapp.putExtra(Intent.EXTRA_TEXT, "Hey my friend check out this app\n https://play.google.com/store/apps/details?id="+ getPackageName() +" \n");
                            startActivity(myapp);
                            result.closeDrawer();


                        }else if (drawerItem.getIdentifier() == 7) {
                            // Toast.makeText(MainActivityantivirus.this,"3",Toast.LENGTH_LONG).show();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                            }

                            result.closeDrawer();
                        }
//
//

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(false)
                .build();

        fillFab();
        loadBackdrop();

        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_home);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setTitle(R.string.app_name);

        //make the toolbar transparent
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

       // final IProfile profile2 = new ProfileDrawerItem().withIcon(R.drawable.user_icon);

//        .withDividerBelowHeader(false)
//                .withOnlySmallProfileImagesVisible(true)
//                .withResetDrawerOnProfileListClick(false)
//                .withSelectionFistLineShown(false)
//                .withCurrentProfileHiddenInList(true)
//                .withOnlyMainProfileImageVisible(true)
//                .withResetDrawerOnProfileListClick(false)
//                .withAlternativeProfileHeaderSwitching(false)
//                .withCompactStyle(false)
//                .withSelectionListEnabledForSingleProfile(false)


        // Create the AccountHeader
     /*   headerResult = new AccountHeaderBuilder()
                .withActivity(this)

                .withTranslucentStatusBar(false)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(profile2)
                .build();






        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        //  final IProfile profile = new ProfileDrawerItem().withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460");
//        final IProfile profile2 = new ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460"));


//Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDrawerLayout(R.layout.crossfade_material_drawer)
                .withHasStableIds(true)
                .withDrawerWidthDp(72)
                .withGenerateMiniDrawer(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.Home).withIcon(R.drawable.homee).withIdentifier(1),


                        new PrimaryDrawerItem().withName(R.string.IgnoredList).withIcon(R.drawable.ignoredlist).withIdentifier(3),

                        new PrimaryDrawerItem().withName(R.string.InfosDevice).withIcon(R.drawable.infosdevice).withIdentifier(4),
                        new PrimaryDrawerItem().withName(R.string.Privacy).withIcon(R.drawable.privacyicone).withIdentifier(5),

                        new PrimaryDrawerItem().withName(R.string.Share).withIcon(R.drawable.sharee).withIdentifier(6),
                        new PrimaryDrawerItem().withName(R.string.Rate).withIcon(R.drawable.ratee).withIdentifier(7)
                        // new DividerDrawerItem(),
                        //  new SecondaryDrawerItem().withName(R.string.drawer_item_seventh).withIcon(FontAwesome.Icon.faw_github).withIdentifier(7).withSelectable(false)
                ) // add the items we want to use with our Drawer
                .withSelectedItemByPosition(1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


                        if (drawerItem.getIdentifier() == 1) {
                            // Toast.makeText(MainActivityantivirus.this,"2",Toast.LENGTH_LONG).show();


                            result.closeDrawer();




                        }else if (drawerItem.getIdentifier() == 3) {

                            // Toast.makeText(MainActivityantivirus.this,"2",Toast.LENGTH_LONG).show();
                            Intent browserIntent2 = new Intent(MainActivityantivirus.this, IgnoredListActivity.class);
                            startActivity(browserIntent2);
                            result.closeDrawer();

                        } else if (drawerItem.getIdentifier() == 4) {
                            // Toast.makeText(MainActivityantivirus.this,"2",Toast.LENGTH_LONG).show();
                            Intent browserIntent2 = new Intent(MainActivityantivirus.this, PhoneInfoActivity.class);
                            startActivity(browserIntent2);
                            result.closeDrawer();

                        } else if (drawerItem.getIdentifier() == 5) {
                            // Toast.makeText(MainActivityantivirus.this,"2",Toast.LENGTH_LONG).show();
                            try {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://metacafeeprostormclean.tumblr.com"));
                                startActivity(browserIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            result.closeDrawer();


                        } else if (drawerItem.getIdentifier() == 6) {

                            Intent myapp = new Intent(Intent.ACTION_SEND);
                            myapp.setType("text/plain");
                            myapp.putExtra(Intent.EXTRA_TEXT, "Hey my friend check out this app\n https://play.google.com/store/apps/details?id="+ getPackageName() +" \n");
                            startActivity(myapp);
                            result.closeDrawer();


                        }else if (drawerItem.getIdentifier() == 7) {
                            // Toast.makeText(MainActivityantivirus.this,"3",Toast.LENGTH_LONG).show();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                            }

                            result.closeDrawer();
                        }
//
//

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(false)
                .build();


*/
        //get out our drawerLyout
     //   crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();

        //define maxDrawerWidth
     //   crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
     //   MiniDrawer miniResult = result.getMiniDrawer();
        //build the view for the MiniDrawer
     //   View view = miniResult.build(this);
        //set the background of the MiniDrawer as this would be transparent
    //    view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
     //   crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
    /*    miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });
//hook to the crossfade event
        crossfadeDrawerLayout.withCrossfadeListener(new CrossfadeDrawerLayout.CrossfadeListener() {
            @Override
            public void onCrossfade(View containerView, float currentSlidePercentage, int slideOffset) {
                Log.e("CrossfadeDrawerLayout", "crossfade: " + currentSlidePercentage + " - " + slideOffset);
            }
        });

        //  TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //  mTitle.setText(R.string.app_name);
*/
        customFont();
        this.view = findViewById(R.id.background);
        this.iv_start_scan = (ImageView) this.view.findViewById(R.id.iv_start_scan_anim);
        this.iv_start_scan.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation));
        this.bg_animation_scan.setAnimation(AnimationUtils.loadAnimation(this, R.anim.bg_animation_scan));
        this.img_resolvep_roblems.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivityantivirus.this.startActivity(new Intent(MainActivityantivirus.this, ResultActivity.class));
            }
        });
    }

    protected void onResume() {
        super.onResume();
        Utils.getCacheSize(this);
        getMemoryInfo();
        Intent i = new Intent(this, MonitorShieldService.class);
        if (!Utils.isServiceRunning(this, MonitorShieldService.class)) {
            startService(i);
        }
        bindService(i, this.serviceConnection, 1);
        if (getSharedPreferences("Settings", 0).getInt("rate", 0) == 1) {
            showRateDialog();
        }
    }

    private void getMemoryInfo() {
//        long totalInternalMemorySize = Utils.getTotalInternalMemorySize();
//        long availableInternalMemorySize = Utils.getAvailableInternalMemorySize();
//        this.tv_info_storage.setText(Utils.formatSize(totalInternalMemorySize - availableInternalMemorySize) + "/" + Utils.formatSize(totalInternalMemorySize));
//        int percent_storage = (int) (((totalInternalMemorySize - availableInternalMemorySize) * 100) / totalInternalMemorySize);
//        this.tv_percent_storage.setText(String.valueOf(percent_storage + "%"));
//        long freeRam = Utils.getFreeRAM(this);
//        long totalRam = Utils.getTotalRAM(this);
//        this.tv_info_memory.setText(Utils.formatSize(totalRam - freeRam) + "/" + Utils.formatSize(totalRam));
//        int percent_memory = (int) (((totalRam - freeRam) * 100) / totalRam);
//        this.tv_percent_memory.setText(String.valueOf(percent_memory) + "%");
//        this.pb_storage.setSmoothPercent((float) (((double) percent_storage) / 100.0d));
//        this.pb_memory.setSmoothPercent((float) (((double) percent_memory) / 100.0d));
    }

    private void showRateDialog() {
        getSharedPreferences("Settings", 0).edit().putInt("rate", 2).apply();
        new ExitDialog(this, 0x1030071);
    }

    public void setBackgroungDanger() {
        this.view.setBackgroundResource(R.drawable.background_danger);
    }

    public void setBackgroungSafe() {
        this.view.setBackgroundResource(R.drawable.settings_background);
    }

    public MonitorShieldService getMonitorShieldService() {
        return this.monitorShieldService;
    }

    private void initView() {
        if (!AppData.getInstance(this).getFirstScanDone()) {
            showFirstScan();
        } else if (this.monitorShieldService.getMenacesCacheSet().getItemCount() == 0) {
            showSafe();
        } else {
            showDanger();
        }
    }

    private void showFirstScan() {
        this.tv_first_run.setVisibility(0);
        this.noti_danger.setVisibility(4);
        this.notifi_safe.setVisibility(4);
    }

    private void showSafe() {
        this.tv_first_run.setVisibility(4);
        this.noti_danger.setVisibility(4);
        this.notifi_safe.setVisibility(0);
        setBackgroungSafe();
    }

    private void showDanger() {
        this.tv_first_run.setVisibility(4);
        this.notifi_safe.setVisibility(4);
        this.noti_danger.setVisibility(0);
        setBackgroungDanger();
        this.tv_found_problem.setText(this.monitorShieldService.getMenacesCacheSet().getItemCount() + " " + getResources().getString(R.string.problem_found));
    }

    public void onStop() {
        super.onStop();
        if (this.bound && this.monitorShieldService != null) {
            unbindService(this.serviceConnection);
            this.bound = false;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        //   AppEventsLogger.deactivateApp(this);
    }

    private void loadBackdrop() {
      //  final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
       // Glide.with(this).load("https://unsplash.it/600/300/?random").centerCrop().into(imageView);
    }

    private void fillFab() {
      //  final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floating_action_button);
       // fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_favorite).actionBar().color(Color.WHITE));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
