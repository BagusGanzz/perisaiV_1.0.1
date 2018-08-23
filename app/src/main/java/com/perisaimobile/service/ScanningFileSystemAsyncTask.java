package com.perisaimobile.service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ClipDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.perisaimobile.activities.ScanningActivity;
import com.perisaimobile.animation.ArcProgress;
import com.perisaimobile.animation.BezierTranslateAnimation;
import com.perisaimobile.iface.IOnActionFinished;
import com.perisaimobile.iface.IProblem;
import com.perisaimobile.model.AppLock;
import com.perisaimobile.model.AppProblem;
import com.perisaimobile.model.Application;
import com.studioninja.locker.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScanningFileSystemAsyncTask extends AsyncTask<Void, Integer, Void> {
    private Context _activity;
    IOnActionFinished _asyncTaskCallBack;
    private boolean _isPaused = false;
    private Collection<AppProblem> _menaces;
    private List<PackageInfo> _packagesToScan;
    private TextView bottomIssues;
    private TextView bottomIssues_booster;
    private TextView bottomIssues_privacy;
    private ClipDrawable clipDrawable2;
    private ClipDrawable clipDrawable3;
    private int countAnimation;
    private int countBooster;
    private int countFound;
    private int countPrivacyApps;
    private ImageView imgBosster;
    private ImageView imgPrivacy;
    private ImageView imgThreat;
    private ImageView img_2;
    private ImageView img_3;
    private boolean running = true;
  //  private ScanningProgress scanningProgress;
    private ArcProgress scanningProgress;
    private int step = 1;
    private int totalBooster;
    private int totalPrivacyApps;
    private int totalProgress;
    private TextView tvBooster;
    private TextView tvPrivacy;
    private TextView tvProgress;
    private TextView tvStep;
    private TextView tvThreat;

    static /* synthetic */ int access$004(ScanningFileSystemAsyncTask x0) {
        int i = x0.countAnimation + 1;
        x0.countAnimation = i;
        return i;
    }

    public void setAsyncTaskCallback(IOnActionFinished asyncTaskCallback) {
        this._asyncTaskCallBack = asyncTaskCallback;
    }

    public void pause() {
        this._isPaused = true;
    }

    public void resume() {
        this._isPaused = false;
    }

    public ScanningFileSystemAsyncTask(ScanningActivity activity, List<PackageInfo> allPackages, Collection<IProblem> menaces) {
        this._activity = activity;
        this._packagesToScan = allPackages;
        this.countFound = 0;
        this.countPrivacyApps = 0;
        this.countBooster = 0;
        this._menaces = new ArrayList();
        for (IProblem p : menaces) {
            if (p.getType() == IProblem.ProblemType.AppProblem) {
                this._menaces.add((AppProblem) p);
            }
        }
        this.tvProgress = (TextView) activity.findViewById(R.id.tv_progress);
        this.bottomIssues = (TextView) activity.findViewById(R.id.bottomIssues);
        this.imgThreat = (ImageView) activity.findViewById(R.id.img_threat);
        this.tvThreat = (TextView) activity.findViewById(R.id.tv_threat);
        this.imgPrivacy = (ImageView) activity.findViewById(R.id.img_privacy);
        this.tvPrivacy = (TextView) activity.findViewById(R.id.tv_privacy);
        this.imgBosster = (ImageView) activity.findViewById(R.id.img_booster);
        this.tvBooster = (TextView) activity.findViewById(R.id.tv_booster);
        this.tvStep = (TextView) activity.findViewById(R.id.tv_step);
        this.bottomIssues_privacy = (TextView) activity.findViewById(R.id.bottomIssues_privacy);
        this.bottomIssues_booster = (TextView) activity.findViewById(R.id.bottomIssues_booster);
        this.scanningProgress = (ArcProgress) activity.findViewById(R.id.scan_progress);
//        this.img_3 = (ImageView) activity.findViewById(R.id.img_3);
//        this.clipDrawable3 = (ClipDrawable) this.img_3.getDrawable();
//        this.img_2 = (ImageView) activity.findViewById(R.id.img_2);
//        this.clipDrawable2 = (ClipDrawable) this.img_2.getDrawable();
    }

    protected void onPreExecute() {
        ((ScanningActivity) this._activity).getMonitorShieldService()._loadData();
        for (Application application : ((ScanningActivity) this._activity).getMonitorShieldService().getRunningApplications()) {
            this.totalBooster = (int) (((long) this.totalBooster) + (application.getSize() / 1024));
        }
        for (AppLock appLock : ((ScanningActivity) this._activity).getMonitorShieldService().getAppLock()) {
            if (appLock.isRecommend()) {
                this.totalPrivacyApps++;
            }
        }
        this.totalProgress = (this._packagesToScan.size() + this.totalPrivacyApps) + this.totalBooster;
    }

    protected void onCancelled() {
        this.running = false;
    }

    protected Void doInBackground(Void... params) {
        int currentTotal = 0;
        int currentIndex = 0;
        try {
            publishProgress(new Integer[]{Integer.valueOf(0)});
            while (this.running && currentIndex < this._packagesToScan.size()) {
                Thread.sleep((long) (((int) (Math.random() * 5.0d)) + 10));
                if (!this._isPaused) {
                    if (isPackageInMenacesSet(((PackageInfo) this._packagesToScan.get(currentIndex)).packageName)) {
                        this.countFound++;
                    }
                    currentTotal++;
                    publishProgress(new Integer[]{Integer.valueOf(currentTotal)});
                    currentIndex++;
                }
            }
            this.step = 2;
            publishProgress(new Integer[]{Integer.valueOf(currentTotal)});
            currentIndex = 0;
            while (this.running && currentIndex < this.totalPrivacyApps) {
                Thread.sleep((long) (((int) (Math.random() * 5.0d)) + 10));
                if (!this._isPaused) {
                    currentIndex++;
                    this.countPrivacyApps++;
                    currentTotal++;
                    publishProgress(new Integer[]{Integer.valueOf(currentTotal)});
                }
            }
            this.step = 3;
            publishProgress(new Integer[]{Integer.valueOf(currentTotal)});
            currentIndex = 0;
            while (this.running && currentIndex < this.totalBooster) {
                Thread.sleep((long) (((int) (Math.random() * 5.0d)) + 10));
                if (!this._isPaused) {
                    currentIndex++;
                    this.countBooster++;
                    currentTotal++;
                    publishProgress(new Integer[]{Integer.valueOf(currentTotal)});
                }
            }
            this.step = 4;
            publishProgress(new Integer[]{Integer.valueOf(currentTotal)});
        } catch (InterruptedException e) {
            Log.w("APP", "Scanning task was interrupted");
        }
        return null;
    }

    boolean isPackageInMenacesSet(String packageName) {
        for (AppProblem menace : this._menaces) {
            if (menace.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    protected void onProgressUpdate(Integer... params) {
        this.tvProgress.setTextSize(2, 32.0f);
        int progress = (params[0].intValue() * 100) / this.totalProgress;
        this.tvProgress.setText("");
        this.scanningProgress.setProgress(progress);
     //   this.clipDrawable3.setLevel(10000 - (progress * 100));
      //  this.clipDrawable2.setLevel(progress * 100);
        if (this.step == 1) {
            this.bottomIssues.setText(String.valueOf(this.countFound));
            return;
        }
        if (this.imgThreat.getVisibility() == 0) {
            this.imgThreat.setVisibility(8);
            if (this.countFound == 0) {
                this.bottomIssues.setVisibility(8);
                this.tvThreat.setText("0");
                this.tvThreat.setVisibility(0);
                this.countAnimation++;
            } else {
                setupAnimation(this.bottomIssues, this.tvThreat);
                this.bottomIssues.setTextSize(2, 22.0f);
            }
            this.imgPrivacy.setImageResource(R.mipmap.ic_privacy_ac);
            this.tvStep.setText(this._activity.getResources().getString(R.string.scanning_for_privacy));
        }
        if (this.step == 2) {
            this.bottomIssues_privacy.setText(String.valueOf(this.countPrivacyApps));
            return;
        }
        if (this.imgPrivacy.getVisibility() == 0) {
            this.imgPrivacy.setVisibility(8);
            if (this.countPrivacyApps == 0) {
                this.bottomIssues_privacy.setVisibility(8);
                this.tvPrivacy.setText("0");
                this.tvPrivacy.setVisibility(0);
                this.countAnimation++;
            } else {
                setupAnimation(this.bottomIssues_privacy, this.tvPrivacy);
                this.bottomIssues_privacy.setTextSize(2, 22.0f);
            }
            this.imgBosster.setImageResource(R.mipmap.ic_phoneboost_ac);
            this.tvStep.setText(this._activity.getResources().getString(R.string.freeable_memory));
        }
        if (this.step == 3) {
            this.bottomIssues_booster.setText(String.valueOf(this.countBooster));
        } else if (this.imgBosster.getVisibility() == 0) {
            this.imgBosster.setVisibility(8);
            if (this.countBooster == 0) {
                this.bottomIssues_booster.setVisibility(8);
                this.tvBooster.setText("0MB");
                this.tvBooster.setVisibility(0);
                this.countAnimation++;
                if (this.countAnimation == 3 && this._asyncTaskCallBack != null) {
                    this._asyncTaskCallBack.onFinished();
                    return;
                }
                return;
            }
            this.bottomIssues_booster.setText(this.bottomIssues_booster.getText() + "MB");
            setupAnimation(this.bottomIssues_booster, this.tvBooster);
            this.bottomIssues_booster.setTextSize(2, 22.0f);
        }
    }

    protected void onPostExecute(Void result) {
      //  this.scanningProgress.setVisibility(8);
    }

    private void setupAnimation(final View start, final View end) {
        start.post(new Runnable() {
            public void run() {
                int[] startLocations = new int[2];
                start.getLocationOnScreen(startLocations);
                int startX = startLocations[0] + (start.getWidth() / 2);
                int startY = startLocations[1] + start.getHeight();
                int[] endLocations = new int[2];
                end.getLocationOnScreen(endLocations);
                int endX = endLocations[0] + (end.getWidth() / 2);
                BezierTranslateAnimation bezierTranslateAnimation = new BezierTranslateAnimation(0.0f, (float) (endX - startX), 0.0f, (float) ((endLocations[1] + (end.getHeight() / 2)) - startY), (float) (endX - startX), -200.0f);
                bezierTranslateAnimation.setFillAfter(true);
                bezierTranslateAnimation.setDuration(800);
                bezierTranslateAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        ScanningFileSystemAsyncTask.access$004(ScanningFileSystemAsyncTask.this);
                        if (ScanningFileSystemAsyncTask.this.countAnimation == 3 && ScanningFileSystemAsyncTask.this._asyncTaskCallBack != null) {
                            ScanningFileSystemAsyncTask.this._asyncTaskCallBack.onFinished();
                        }
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                start.startAnimation(bezierTranslateAnimation);
            }
        });
    }
}
