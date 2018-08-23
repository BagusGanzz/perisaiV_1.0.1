package com.perisaimobile.activities;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.liulishuo.magicprogresswidget.MagicProgressBar;
import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.util.RootUtil;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class PhoneInfoActivity extends BaseToolbarActivity {
    public static final int PHONE_INFO_REQUEST_CODE = 2345;
    private Handler handler = new Handler();
    @BindView(R.id.pb_cpu)
    MagicProgressBar pb_cpu;
    @BindView(R.id.pb_ram)
    MagicProgressBar pb_ram;
    @BindView(R.id.pb_storage)
    MagicProgressBar pb_storage;
    private Timer timer;
    private TimerTask timerTask;
    private TextView tvInfoCPU;
    private TextView tvInforRam;
    private TextView tvInforStorage;
    @BindView(R.id.tv_basic_information)
    TextView tv_basic_information;
    @BindView(R.id.tv_title_cpu)
    TextView tv_title_cpu;
    @BindView(R.id.tv_title_imei)
    TextView tv_title_imei;
    @BindView(R.id.tv_title_ram)
    TextView tv_title_ram;
    @BindView(R.id.tv_title_root_state)
    TextView tv_title_root_state;
    @BindView(R.id.tv_title_storage)
    TextView tv_title_storage;
    @BindView(R.id.tv_title_system_os_version)
    TextView tv_title_system_os_version;
    private AdView mAdView;


    public int getLayoutId() {
        return R.layout.activity_phone_info;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TextView) findViewById(R.id.toolbar_title)).setText(getResources().getString(R.string.phone_info));

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intreal));
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {


                    mInterstitialAd.show();

            }
        });

        initView();
        startTimer();
    }

    private void initView() {
        TextView tvSystOSVersion = (TextView) findViewById(R.id.tv_system_os_version);
        tvSystOSVersion.setText("Android " + String.valueOf(VERSION.RELEASE));
        TextView tvRootState = (TextView) findViewById(R.id.tv_root_state);
        if (RootUtil.isDeviceRooted()) {
            tvRootState.setText("Rooted");
        } else {
            tvRootState.setText("Not Rooted");
        }
        if (VERSION.SDK_INT < 23) {
            TextView tvIMEI = (TextView) findViewById(R.id.tv_imei);
            tvIMEI.setText(((TelephonyManager) getSystemService("phone")).getDeviceId());
            TypeFaceUttils.setNomal((Context) this, tvIMEI);
        }
        this.tvInforStorage = (TextView) findViewById(R.id.tv_info_storage);
        this.tvInforRam = (TextView) findViewById(R.id.tv_info_ram);
        this.tvInfoCPU = (TextView) findViewById(R.id.tv_info_cpu);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_cpu);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_ram);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_storage);
        TypeFaceUttils.setNomal((Context) this, this.tvInfoCPU);
        TypeFaceUttils.setNomal((Context) this, this.tvInforRam);
        TypeFaceUttils.setNomal((Context) this, this.tvInforStorage);
        TypeFaceUttils.setNomal((Context) this, this.tv_basic_information);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_system_os_version);
        TypeFaceUttils.setNomal((Context) this, tvSystOSVersion);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_imei);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_root_state);
        TypeFaceUttils.setNomal((Context) this, tvRootState);
        long totalInternalMemorySize = Utils.getTotalInternalMemorySize();
        this.tvInforStorage.setText(Utils.formatSize(totalInternalMemorySize - Utils.getAvailableInternalMemorySize()));
        this.pb_storage.setSmoothPercent(((float) (totalInternalMemorySize - Utils.getAvailableInternalMemorySize())) / ((float) totalInternalMemorySize));
        long freeRam = Utils.getFreeRAM(this);
        long totalRam = Utils.getTotalRAM(this);
        this.tvInforRam.setText(Utils.formatSize(totalRam - freeRam));
        this.pb_ram.setSmoothPercent(((float) (totalRam - freeRam)) / ((float) totalRam));
    }

    public void startTimer() {
        this.timer = new Timer();
        initializeTimerTask();
        this.timer.schedule(this.timerTask, 500, 1000);
    }

    public void stoptimertask() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    public void initializeTimerTask() {
        this.timerTask = new TimerTask() {
            public void run() {
                PhoneInfoActivity.this.handler.post(new Runnable() {
                    public void run() {
                        int cpu = (int) (PhoneInfoActivity.this.readUsage() * 100.0f);
                        PhoneInfoActivity.this.tvInfoCPU.setText(cpu + "%");
                        PhoneInfoActivity.this.pb_cpu.setSmoothPercent((float) (((double) cpu) / 100.0d));
                    }
                });
            }
        };
    }

    private float readUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String[] toks = reader.readLine().split(" +");
            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = ((((Long.parseLong(toks[2]) + Long.parseLong(toks[3])) + Long.parseLong(toks[5])) + Long.parseLong(toks[6])) + Long.parseLong(toks[7])) + Long.parseLong(toks[8]);
            try {
                Thread.sleep(360);
            } catch (Exception e) {
            }
            reader.seek(0);
            String load = reader.readLine();
            reader.close();
            toks = load.split(" +");
            long cpu2 = ((((Long.parseLong(toks[2]) + Long.parseLong(toks[3])) + Long.parseLong(toks[5])) + Long.parseLong(toks[6])) + Long.parseLong(toks[7])) + Long.parseLong(toks[8]);
            return ((float) (cpu2 - cpu1)) / ((float) ((cpu2 + Long.parseLong(toks[4])) - (cpu1 + idle1)));
        } catch (IOException ex) {
            ex.printStackTrace();
            return 0.0f;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PHONE_INFO_REQUEST_CODE && grantResults.length == 1 && grantResults[0] == 0) {
            TextView tvIMEI = (TextView) findViewById(R.id.tv_imei);
            tvIMEI.setText(((TelephonyManager) getSystemService("phone")).getDeviceId());
            TypeFaceUttils.setNomal((Context) this, tvIMEI);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                stoptimertask();
                finish();
                break;
        }
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        stoptimertask();
    }

    protected void onStop() {
        super.onStop();
        stoptimertask();
    }
}
