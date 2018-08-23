package com.perisaimobile.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.perisaimobile.activities.AppLockCreatePasswordActivity;
import com.perisaimobile.activities.ResultAppLockCreatePasswordActivity;
import com.perisaimobile.activities.ScanningResultActivity;
import com.perisaimobile.adapter.ResultAppLockApdater;
import com.perisaimobile.model.AppLock;
import com.perisaimobile.model.AppsLocked;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultAppLockFragment extends Fragment {
    private ScanningResultActivity activity;
    private ResultAppLockApdater adapter;
    private List<AppLock> appLocks;
    private AppsLocked appsLocked;
    private int protectApps;
    @BindView(R.id.rv_app_lock)
    RecyclerView rv_app_lock;
    @BindView(R.id.tv_decription)
    TextView tv_decription;
    @BindView(R.id.tv_protect)
    TextView tv_protect;
    @BindView(R.id.tv_title)
    TextView tv_title;

    static /* synthetic */ int access$104(ResultAppLockFragment x0) {
        int i = x0.protectApps + 1;
        x0.protectApps = i;
        return i;
    }

    static /* synthetic */ int access$106(ResultAppLockFragment x0) {
        int i = x0.protectApps - 1;
        x0.protectApps = i;
        return i;
    }

    private void customFont() {
        TypeFaceUttils.setNomal(getActivity(), this.tv_title);
        TypeFaceUttils.setNomal(getActivity(), this.tv_decription);
        TypeFaceUttils.setNomal(getActivity(), this.tv_protect);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_app_lock, container, false);
        ButterKnife.bind(this, view);
        customFont();
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = (ScanningResultActivity) getActivity();
        this.activity.setBackground(R.drawable.settings_background);
        if (this.activity.getMonitorShieldService() == null) {
            getActivity().getSupportFragmentManager().popBackStack();
            this.activity.setBackground(R.drawable.background_danger);
            return;
        }
        this.appsLocked = new AppsLocked(getActivity());
        initView();
    }

    private void initView() {
        for (AppLock appLock : this.activity.getMonitorShieldService().getAppLock()) {
            if (appLock.isRecommend()) {
                this.protectApps++;
                appLock.setLock(true);
            }
        }
        String number = "<font color='#cc0000'>" + this.protectApps + "</font>";
        this.tv_title.setText(Html.fromHtml(number + ("<font color='#0066cc'> " + getResources().getString(R.string.apps_with_privacy_issues) + "</font>")));
        this.appLocks = this.activity.getMonitorShieldService().getAppLock();
        Collections.sort(this.appLocks);
        this.rv_app_lock.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.rv_app_lock.setHasFixedSize(true);
        this.adapter = new ResultAppLockApdater(getActivity(), this.appLocks);
        this.rv_app_lock.setAdapter(this.adapter);
        updateProtectApps();
        this.adapter.setOnItemClickListener(new ResultAppLockApdater.OnItemClickListener() {
            public void onItemClick(View itemView, int position) {
                CheckBox checkBox = (CheckBox) itemView;
                if (checkBox.isChecked()) {
                    checkBox.setChecked(true);
                    ((AppLock) ResultAppLockFragment.this.appLocks.get(position)).setLock(true);
                    ResultAppLockFragment.access$104(ResultAppLockFragment.this);
                } else {
                    checkBox.setChecked(false);
                    ((AppLock) ResultAppLockFragment.this.appLocks.get(position)).setLock(false);
                    ResultAppLockFragment.access$106(ResultAppLockFragment.this);
                }
                ResultAppLockFragment.this.updateProtectApps();
            }
        });
        this.tv_protect.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ResultAppLockFragment.this.getActivity().getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0).getString(AppLockCreatePasswordActivity.KEY_PASSWORD, null) != null) {
                    List<AppLock> remove = new ArrayList();
                    for (AppLock appLock : ResultAppLockFragment.this.appLocks) {
                        if (appLock.isLock()) {
                            ResultAppLockFragment.this.appsLocked.add(appLock);
                            remove.add(appLock);
                        }
                    }
                    ResultAppLockFragment.this.appLocks.removeAll(remove);
                    ResultAppLockFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                    ResultAppLockFragment.this.activity.setBackground(R.drawable.background_danger);
                    ResultAppLockFragment.this.activity.refresh();
                } else if (VERSION.SDK_INT >= 22 && !Utils.isUsageAccessEnabled(ResultAppLockFragment.this.getActivity())) {
                    Utils.openUsageAccessSetings(ResultAppLockFragment.this.getActivity());
                } else if (VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(ResultAppLockFragment.this.getActivity())) {
                    ResultAppLockFragment.this.startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + ResultAppLockFragment.this.getActivity().getPackageName())));
                } else if (Utils.isAccessibilitySettingsOn(ResultAppLockFragment.this.getActivity())) {
                    ResultAppLockFragment.this.startActivity(new Intent(ResultAppLockFragment.this.getActivity(), ResultAppLockCreatePasswordActivity.class));
                } else {
                    ResultAppLockFragment.this.startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                    Dialog dialog = new Dialog(ResultAppLockFragment.this.getActivity());
                    dialog.getWindow().setType(2003);
                    dialog.requestWindowFeature(1);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.setContentView(R.layout.dialog_guide_accessibility);
                    dialog.show();
                }
            }
        });
    }

    private void updateProtectApps() {
        if (this.protectApps != 0) {
            this.tv_protect.setVisibility(0);
            this.tv_protect.setText(getResources().getString(R.string.protect) + "(" + this.protectApps + ")");
            return;
        }
        this.tv_protect.setVisibility(8);
    }
}
