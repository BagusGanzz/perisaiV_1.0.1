package com.perisaimobile.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.perisaimobile.adapter.ResultAdapter;
import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.fragment.ResloveProblemDetailsFragment;
import com.perisaimobile.iface.IProblem;
import com.perisaimobile.iface.IResultItemSelectedListener;
import com.perisaimobile.service.MonitorShieldService;
import com.perisaimobile.util.TypeFaceUttils;
import com.studioninja.locker.R;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;

public class ResultActivity extends BaseToolbarActivity {
    public static final String PROBLEM_DETAIL_FRAGMENT_TAG = "PROBLEM_DETAIL";
    public static final int REQUEST_DETAIL_PROBLEM = 3;
    ResultAdapter adapter;
    private boolean bound;
    private IProblem comu;
    @BindView(R.id.framelayout_skip_all)
    View framelayout_skip_all;
    private MonitorShieldService monitorShieldService;
    @BindView(R.id.result_layout)
    View result_layout;
    @BindView(R.id.rv_scan_result)
    RecyclerView rv_scan_result;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            ResultActivity.this.bound = true;
            ResultActivity.this.monitorShieldService = ((MonitorShieldService.MonitorShieldLocalBinder) service).getServiceInstance();
            ResultActivity.this.init();
        }

        public void onServiceDisconnected(ComponentName name) {
            ResultActivity.this.bound = false;
            ResultActivity.this.monitorShieldService = null;
        }
    };
    @BindView(R.id.tv_num_of_issues)
    TextView tv_num_of_issues;
    @BindView(R.id.tv_skip_all)
    TextView tv_skip_all;

    private void customFont() {
        TypeFaceUttils.setNomal((Context) this, this.tv_num_of_issues);
        TypeFaceUttils.setNomal((Context) this, this.tv_skip_all);
    }

    public int getLayoutId() {
        return R.layout.activity_scan_result;
    }

    public MonitorShieldService getMonitorShieldService() {
        return this.monitorShieldService;
    }

    public IProblem getComu() {
        return this.comu;
    }

    public void setComu(IProblem comu) {
        this.comu = comu;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT >= 19) {
            getWindow().setFlags(67108864, 67108864);
        }
        bindService(new Intent(this, MonitorShieldService.class), this.serviceConnection, 1);
        customFont();
        getSupportFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
            public void onBackStackChanged() {
                if (ResultActivity.this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    ResultActivity.this.result_layout.setVisibility(0);
                } else {
                    ResultActivity.this.result_layout.setVisibility(8);
                }
            }
        });
    }

    private void init() {
        this.tv_num_of_issues.setText(getResources().getString(R.string.found) + " " + this.monitorShieldService.getMenacesCacheSet().getItemCount() + " " + getResources().getString(R.string.issues));
        this.adapter = new ResultAdapter(this, new ArrayList(this.monitorShieldService.getMenacesCacheSet().getSet()));
        this.rv_scan_result.setAdapter(this.adapter);
        this.rv_scan_result.setLayoutManager(new StickyHeaderLayoutManager());
        this.adapter.setResultItemSelectedStateChangedListener(new IResultItemSelectedListener() {
            public void onItemSelected(IProblem bpdw, ImageView iv_icon_app, Context c) {
                ResultActivity.this.setComu(bpdw);
                ResloveProblemDetailsFragment f = (ResloveProblemDetailsFragment) ResultActivity.this.getSupportFragmentManager().findFragmentByTag(ResultActivity.PROBLEM_DETAIL_FRAGMENT_TAG);
                if (f == null) {
                    f = new ResloveProblemDetailsFragment();
                }
                FragmentTransaction transaction = ResultActivity.this.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.container, f, ResultActivity.PROBLEM_DETAIL_FRAGMENT_TAG);
                transaction.addToBackStack(ResultActivity.PROBLEM_DETAIL_FRAGMENT_TAG);
                transaction.commitAllowingStateLoss();
            }
        });
        this.framelayout_skip_all.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ResultActivity.this.startActivity(new Intent(ResultActivity.this, SafeActivity.class));
                ResultActivity.this.finish();
            }
        });
    }

    protected void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        if (this.bound && this.monitorShieldService != null) {
            unbindService(this.serviceConnection);
            this.bound = false;
        }
        super.onDestroy();
    }

    public void refresh(IProblem iProblem) {
        this.adapter.remove(iProblem);
        this.tv_num_of_issues.setText(getResources().getString(R.string.found) + " " + this.monitorShieldService.getMenacesCacheSet().getItemCount() + " " + getResources().getString(R.string.issues));
        if (this.monitorShieldService.getMenacesCacheSet().getItemCount() == 0) {
            startActivity(new Intent(this, SafeActivity.class));
            finish();
        }
    }
}
