package com.perisaimobile.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.TextView;

import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.iface.IOnActionFinished;
import com.perisaimobile.iface.IProblem;
import com.perisaimobile.model.AppData;
import com.perisaimobile.model.AppLock;
import com.perisaimobile.model.JunkOfApplication;
import com.perisaimobile.service.MonitorShieldService;
import com.perisaimobile.service.MonitorShieldService.IClientInterface;
import com.perisaimobile.service.ScanningFileSystemAsyncTask;
import com.perisaimobile.util.ProblemsDataSetTools;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

public class ScanningActivity extends BaseToolbarActivity implements MonitorShieldService.IClientInterface {
    MonitorShieldService.IClientInterface _appMonitorServiceListener = null;
    ScanningFileSystemAsyncTask _currentScanTask = null;
    @BindView(R.id.bottomIssues)
    TextView bottomIssues;
    @BindView(R.id.bottomIssues_booster)
    TextView bottomIssues_booster;
    @BindView(R.id.bottomIssues_privacy)
    TextView bottomIssues_privacy;
    private boolean bound;

    private MonitorShieldService monitorShieldService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            ScanningActivity.this.bound = true;
            ScanningActivity.this.monitorShieldService = ((MonitorShieldService.MonitorShieldLocalBinder) service).getServiceInstance();
            ScanningActivity.this.monitorShieldService.registerClient(ScanningActivity.this);
            new MyTask().execute(new Void[0]);
            if (ScanningActivity.this.monitorShieldService.getMenacesCacheSet().getItemCount() == 0) {
              //  ScanningActivity.this.img2.setImageResource(R.drawable.clip_bg2);
            } else if (ScanningActivity.this.monitorShieldService.getMenacesCacheSet().getItemCount() <= 10) {
             //   ScanningActivity.this.img2.setImageResource(R.drawable.clip_bg2_1);
            } else {
             //   ScanningActivity.this.img2.setImageResource(R.drawable.clip_bg2_2);
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            ScanningActivity.this.bound = false;
            ScanningActivity.this.monitorShieldService = null;
        }
    };
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.tv_step)
    TextView tv_step;
    @BindView(R.id.tv_title_booster)
    TextView tv_title_booster;
    @BindView(R.id.tv_title_privacy)
    TextView tv_title_privacy;
    @BindView(R.id.tv_title_threat)
    TextView tv_title_threat;

    class MyTask extends AsyncTask<Void, Void, Void> {
        MyTask() {
        }

        protected Void doInBackground(Void... params) {
            ScanningActivity.this._startRealScan();
            return null;
        }
    }

    private void customFont() {
        TypeFaceUttils.setNomal((Context) this, this.tv_progress);
        TypeFaceUttils.setNomal((Context) this, this.tv_step);
        TypeFaceUttils.setNomal((Context) this, this.bottomIssues);
        TypeFaceUttils.setNomal((Context) this, this.bottomIssues_privacy);
        TypeFaceUttils.setNomal((Context) this, this.bottomIssues_booster);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_threat);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_privacy);
        TypeFaceUttils.setNomal((Context) this, this.tv_title_booster);
    }

    public int getLayoutId() {
        return R.layout.activity_scanning;
    }

    public MonitorShieldService getMonitorShieldService() {
        return this.monitorShieldService;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        getWindow().addFlags(2097152);
        bindService(new Intent(this, MonitorShieldService.class), this.serviceConnection, 1);
        customFont();
        this.tv_progress.setTextSize(2, 22.0f);
    //    ((ClipDrawable) this.img3.getDrawable()).setLevel(10000);
    }

    public void onMonitorFoundMenace(IProblem menace) {
        if (this._appMonitorServiceListener != null) {
            this._appMonitorServiceListener.onMonitorFoundMenace(menace);
        }
    }

    public void onScanResult(List<PackageInfo> allPackages, Set<IProblem> menacesFound) {
        if (this._appMonitorServiceListener != null) {
            this._appMonitorServiceListener.onScanResult(allPackages, menacesFound);
        }
    }

    private void _startRealScan() {
        startMonitorScan(new IClientInterface() {
            public void onMonitorFoundMenace(IProblem menace) {
            }

            public void onScanResult(List<PackageInfo> allPackages, Set<IProblem> menacesFound) {
                AppData appData = ScanningActivity.this.getAppData();
                appData.setFirstScanDone(true);
                appData.setFirstScanDone(true);
                appData.serialize(ScanningActivity.this);
                Utils.numOfPackages = allPackages.size();
                ScanningActivity.this._startScanningAnimation(allPackages, menacesFound);
            }
        });
        if (this.monitorShieldService != null) {
            this.monitorShieldService.scanFileSystem();
        }
    }

    public void startMonitorScan(IClientInterface listener) {
        this._appMonitorServiceListener = listener;
    }

    private void _startScanningAnimation(List<PackageInfo> allPackages, final Collection<? extends IProblem> tempBadResults) {
        Collection<IProblem> appProblems = new ArrayList();
        ProblemsDataSetTools.getAppProblems(tempBadResults, appProblems);
        this._currentScanTask = new ScanningFileSystemAsyncTask(this, allPackages, appProblems);
        this._currentScanTask.setAsyncTaskCallback(new IOnActionFinished() {
            public void onFinished() {
                ScanningActivity.this._currentScanTask = null;
                AppData appData = ScanningActivity.this.getAppData();
                appData.setLastScanDate(new DateTime());
                appData.serialize(ScanningActivity.this);
                ScanningActivity.this._doAfterScanWork(tempBadResults);
            }
        });
        this._currentScanTask.execute(new Void[0]);
    }

    public AppData getAppData() {
        return AppData.getInstance(this);
    }

    void _doAfterScanWork(Collection<? extends IProblem> collection) {
        this._currentScanTask = null;
        try {
            Thread.sleep(600);

        }catch (Exception e){

        }

        if (isSafeSate()) {
            startActivity(new Intent(this, SafeActivity.class));
        } else {
            startActivity(new Intent(this, ScanningResultActivity.class));
        }
        finish();
    }

    private boolean isSafeSate() {
        boolean application = this.monitorShieldService.getMenacesCacheSet().getItemCount() == 0;
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

    public void onStart() {
        super.onStart();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this._currentScanTask != null) {
            this._currentScanTask.cancel(true);
        }
    }

    public void onBackPressed() {
        Utils.showConfirmDialog(this, getString(R.string.stop_scanning), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ScanningActivity.this.finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                Utils.showConfirmDialog(this, getString(R.string.stop_scanning), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ScanningActivity.this.finish();
                    }
                });
                break;
        }
        return true;
    }

    public void onPause() {
        super.onPause();
        if (this._currentScanTask != null) {
            this._currentScanTask.pause();
        }
    }

    public void onResume() {
        super.onResume();
        if (this._currentScanTask != null) {
            this._currentScanTask.resume();
        }
    }

    protected void onStop() {
        super.onStop();
        if (this.bound && this.monitorShieldService != null) {
            unbindService(this.serviceConnection);
            this.bound = false;
        }
    }
}
