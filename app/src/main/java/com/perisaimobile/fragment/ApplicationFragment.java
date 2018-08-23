package com.perisaimobile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.perisaimobile.activities.ScanningResultActivity;
import com.perisaimobile.adapter.ResultAdapter;
import com.perisaimobile.iface.IProblem;
import com.perisaimobile.iface.IResultItemSelectedListener;
import com.perisaimobile.model.AppProblem;
import com.perisaimobile.model.Application;
import com.perisaimobile.model.MenacesCacheSet;
import com.perisaimobile.model.UserWhiteList;
import com.perisaimobile.util.TypeFaceUttils;
import com.studioninja.locker.R;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplicationFragment extends Fragment {
    private ScanningResultActivity activity;
    ResultAdapter adapter;
    @BindView(R.id.framelayout_skip_all)
    View framelayout_skip_all;
    @BindView(R.id.rv_scan_result)
    RecyclerView rv_scan_result;
    @BindView(R.id.tv_num_of_issues)
    TextView tv_num_of_issues;
    @BindView(R.id.tv_skip_all)
    TextView tv_skip_all;

    private void customFont() {
        TypeFaceUttils.setNomal(getActivity(), this.tv_num_of_issues);
        TypeFaceUttils.setNomal(getActivity(), this.tv_skip_all);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application, container, false);
        ButterKnife.bind(this, view);
        customFont();
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = (ScanningResultActivity) getActivity();
        if (this.activity.getMonitorShieldService() == null) {
            getActivity().getSupportFragmentManager().popBackStack();
            return;
        }
        this.tv_num_of_issues.setText(getResources().getString(R.string.found) + " " + this.activity.getMonitorShieldService().getMenacesCacheSet().getItemCount() + " " + getResources().getString(R.string.issues));
        this.adapter = new ResultAdapter(getActivity(), new ArrayList(this.activity.getMonitorShieldService().getMenacesCacheSet().getSet()));
        this.rv_scan_result.setAdapter(this.adapter);
        this.rv_scan_result.setLayoutManager(new StickyHeaderLayoutManager());
        this.adapter.setResultItemSelectedStateChangedListener(new IResultItemSelectedListener() {
            public void onItemSelected(IProblem bpdw, ImageView iv_icon_app, Context c) {
                ApplicationFragment.this.activity.setComu(bpdw);
                ApplicationFragment.this.activity.slideInFragment(ScanningResultActivity.PROBLEM_DETAIL_FRAGMENT_TAG);
            }
        });
        this.framelayout_skip_all.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ApplicationFragment.this.activity.setApp_skip_all(true);
                ApplicationFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                ApplicationFragment.this.activity.refresh();
            }
        });
    }

    public void deleteItem(final IProblem iProblem, final int type) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                MenacesCacheSet menacesCacheSet = ApplicationFragment.this.activity.getMonitorShieldService().getMenacesCacheSet();
                switch (type) {
                    case 1 /*1*/:
                        AppProblem appProblem = (AppProblem) iProblem;
                        UserWhiteList userWhiteList = ApplicationFragment.this.activity.getMonitorShieldService().getUserWhiteList();
                        userWhiteList.addItem( appProblem);
                        userWhiteList.writeToJSON();
                        menacesCacheSet.removeItem(appProblem);
                        menacesCacheSet.writeToJSON();
                        ApplicationFragment.this.adapter.remove(iProblem);
                        break;
                    case 2/*2*/:
                        UserWhiteList _userWhiteList = ApplicationFragment.this.activity.getMonitorShieldService().getUserWhiteList();
                        _userWhiteList.addItem(iProblem);
                        _userWhiteList.writeToJSON();
                        MenacesCacheSet menaceCacheSet = ApplicationFragment.this.activity.getMonitorShieldService().getMenacesCacheSet();
                        menaceCacheSet.removeItem(iProblem);
                        menaceCacheSet.writeToJSON();
                        ApplicationFragment.this.adapter.remove(iProblem);
                        break;
                    case 3 /*3*/:
                        AppProblem mAppProblem = (AppProblem) iProblem;
                        menacesCacheSet.removeItem(mAppProblem);
                        menacesCacheSet.writeToJSON();
                        Application application = new Application();
                        application.setPackageName(mAppProblem.getPackageName());
                        ApplicationFragment.this.activity.getMonitorShieldService().getRunningApplications().remove(application);
                        break;
                    case 4 /*4*/:
                        menacesCacheSet.removeItem( iProblem);
                        menacesCacheSet.writeToJSON();
                        ApplicationFragment.this.adapter.remove(iProblem);
                        break;
                }
                ApplicationFragment.this.tv_num_of_issues.setText(ApplicationFragment.this.getResources().getString(R.string.found) + " " + ApplicationFragment.this.activity.getMonitorShieldService().getMenacesCacheSet().getItemCount() + " " + ApplicationFragment.this.getResources().getString(R.string.issues));
                if (ApplicationFragment.this.activity.getMonitorShieldService().getMenacesCacheSet().getItemCount() == 0) {
                    ApplicationFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                    ApplicationFragment.this.activity.refresh();
                }
            }
        }, 300);
    }
}
