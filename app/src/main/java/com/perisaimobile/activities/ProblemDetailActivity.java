package com.perisaimobile.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.perisaimobile.adapter.WarningAdapter;
import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.iface.IProblem;
import com.perisaimobile.iface.IProblem.ProblemType;
import com.perisaimobile.model.AppProblem;
import com.perisaimobile.model.MenacesCacheSet;
import com.perisaimobile.model.SystemProblem;
import com.perisaimobile.model.UserWhiteList;
import com.perisaimobile.service.MonitorShieldService;
import com.perisaimobile.util.ProblemsDataSetTools;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import butterknife.BindView;

public class ProblemDetailActivity extends BaseToolbarActivity {
    IProblem _problem = null;
    boolean _uninstallingPackage = false;
    @BindView(R.id.adView)
    AdView adView;
    private boolean bound;
    @BindView(R.id.bt_ignore_setting)
    ImageView bt_ignore_setting;
    @BindView(R.id.bt_open_setting)
    ImageView bt_open_setting;
    @BindView(R.id.bt_trust_app)
    ImageView bt_trust_app;
    @BindView(R.id.bt_uninstall_app)
    ImageView bt_uninstall_app;
    @BindView(R.id.iv_icon_app)
    ImageView iv_icon_app;
    @BindView(R.id.ll_layout_for_app)
    LinearLayout ll_layout_for_app;
    @BindView(R.id.ll_layout_for_system)
    LinearLayout ll_layout_for_system;
    private MonitorShieldService monitorShieldService;
    @BindView(R.id.rv_warning_problem)
    RecyclerView rv_warning_problem;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            ProblemDetailActivity.this.bound = true;
            ProblemDetailActivity.this.monitorShieldService = ((MonitorShieldService.MonitorShieldLocalBinder) service).getServiceInstance();
            ProblemDetailActivity.this.init();
        }

        public void onServiceDisconnected(ComponentName name) {
            ProblemDetailActivity.this.bound = false;
            ProblemDetailActivity.this.monitorShieldService = null;
        }
    };
    @BindView(R.id.tv_app_name)
    TextView tv_app_name;

    private void customFont() {
        TypeFaceUttils.setNomal((Context) this, this.tv_app_name);
    }

    public int getLayoutId() {
        return R.layout.activity_problem_detail;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customFont();
        this._problem = Utils.selectedProblem;
        this.rv_warning_problem.setAdapter(new WarningAdapter(this, this._problem));
        this.rv_warning_problem.setLayoutManager(new LinearLayoutManager(this));
        bindService(new Intent(this, MonitorShieldService.class), this.serviceConnection, 1);

        AdRequest adRequest = new AdRequest.Builder().build();

        this.adView.loadAd(adRequest);
    }

    private void init() {
        if (this._problem.getType() == ProblemType.AppProblem) {
            this.ll_layout_for_app.setVisibility(0);
            this.ll_layout_for_system.setVisibility(8);
            final AppProblem appProblem = (AppProblem) this._problem;
            this.bt_uninstall_app.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ProblemDetailActivity.this._uninstallingPackage = true;
                    ProblemDetailActivity.this.startActivity(new Intent("android.intent.action.DELETE", Uri.fromParts("package", appProblem.getPackageName(), null)));
                }
            });
            this.bt_trust_app.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    new AlertDialog.Builder(ProblemDetailActivity.this).setTitle(ProblemDetailActivity.this.getString(R.string.warning)).setMessage(ProblemDetailActivity.this.getString(R.string.dialog_trust_app)).setPositiveButton(ProblemDetailActivity.this.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            UserWhiteList userWhiteList = ProblemDetailActivity.this.monitorShieldService.getUserWhiteList();
                            userWhiteList.addItem(appProblem);
                            userWhiteList.writeToJSON();
                            MenacesCacheSet menacesCacheSet = ProblemDetailActivity.this.monitorShieldService.getMenacesCacheSet();
                            menacesCacheSet.removeItem(appProblem);
                            menacesCacheSet.writeToJSON();
                            ProblemDetailActivity.this.bt_trust_app.setEnabled(false);
                            ProblemDetailActivity.this.sendResult();
                            ProblemDetailActivity.this.finish();
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create().show();
                }
            });
            Drawable s = Utils.getIconFromPackage(appProblem.getPackageName(), this);
            setTitle(Utils.getAppNameFromPackage(this, appProblem.getPackageName()));
            this.iv_icon_app.setImageDrawable(s);
            this.tv_app_name.setText(Utils.getAppNameFromPackage(this, appProblem.getPackageName()));
            return;
        }
        this.ll_layout_for_app.setVisibility(8);
        this.ll_layout_for_system.setVisibility(0);
        final SystemProblem systemProblem = (SystemProblem) this._problem;
        this.iv_icon_app.setImageDrawable(systemProblem.getIcon(this));
        setTitle(systemProblem.getTitle(this));
        this.bt_open_setting.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                systemProblem.doAction(ProblemDetailActivity.this);
            }
        });
        this.bt_ignore_setting.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                UserWhiteList _userWhiteList = ProblemDetailActivity.this.monitorShieldService.getUserWhiteList();
                _userWhiteList.addItem(ProblemDetailActivity.this._problem);
                _userWhiteList.writeToJSON();
                MenacesCacheSet menaceCacheSet = ProblemDetailActivity.this.monitorShieldService.getMenacesCacheSet();
                menaceCacheSet.removeItem(ProblemDetailActivity.this._problem);
                menaceCacheSet.writeToJSON();
                ProblemDetailActivity.this.sendResult();
                ProblemDetailActivity.this.finish();
            }
        });
    }

    private void initForResume() {
        if (this.monitorShieldService == null) {
            return;
        }
        AppProblem appProblem;
        MenacesCacheSet menacesCacheSet;
        if (this._uninstallingPackage) {
            if (this._problem != null) {
                appProblem = (AppProblem) this._problem;
                if (!Utils.isPackageInstalled(this, appProblem.getPackageName())) {
                    menacesCacheSet = this.monitorShieldService.getMenacesCacheSet();
                    menacesCacheSet.removeItem( appProblem);
                    menacesCacheSet.writeToJSON();
                    sendResult();
                    this._uninstallingPackage = false;
                    finish();
                }
            }
        } else if (this._problem.getType() == ProblemType.AppProblem) {
            appProblem = (AppProblem) this._problem;
            if (!ProblemsDataSetTools.checkIfPackageInCollection(appProblem.getPackageName(), this.monitorShieldService.getMenacesCacheSet().getSet()) && !Utils.isPackageInstalled(this, appProblem.getPackageName())) {
                menacesCacheSet = this.monitorShieldService.getMenacesCacheSet();
                menacesCacheSet.removeItem( appProblem);
                menacesCacheSet.writeToJSON();
                finish();
            }
        } else if (this._problem.getType() == ProblemType.SystemProblem) {
            SystemProblem systemProblem = (SystemProblem) this._problem;
            if (!systemProblem.problemExists(this)) {
                menacesCacheSet = this.monitorShieldService.getMenacesCacheSet();
                menacesCacheSet.removeItem( systemProblem);
                menacesCacheSet.writeToJSON();
                sendResult();
                finish();
            }
        }
    }

    public void onResume() {
        super.onResume();
        initForResume();
        if (this.adView != null) {
            this.adView.resume();
        }
    }

    private void sendResult() {
        Intent intent = new Intent();
        intent.putExtra("is_trust", true);
        setResult(3, intent);
    }

    public void onPause() {
        if (this.adView != null) {
            this.adView.pause();
        }
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        if (this.bound && this.monitorShieldService != null) {
            unbindService(this.serviceConnection);
            this.bound = false;
        }
        if (this.adView != null) {
            this.adView.destroy();
        }
        super.onDestroy();
    }
}
