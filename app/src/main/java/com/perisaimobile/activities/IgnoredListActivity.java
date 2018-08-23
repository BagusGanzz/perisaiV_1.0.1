package com.perisaimobile.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.perisaimobile.adapter.IgnoredAppsAdapter;
import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.iface.IProblem;
import com.perisaimobile.model.AppProblem;
import com.perisaimobile.model.SystemProblem;
import com.perisaimobile.model.UserWhiteList;
import com.perisaimobile.service.MonitorShieldService;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import java.util.ArrayList;
import java.util.List;

public class IgnoredListActivity extends BaseToolbarActivity {
    private boolean bound;
    private MonitorShieldService monitorShieldService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            IgnoredListActivity.this.bound = true;
            IgnoredListActivity.this.monitorShieldService = ((MonitorShieldService.MonitorShieldLocalBinder) service).getServiceInstance();
            IgnoredListActivity.this.init();
        }

        public void onServiceDisconnected(ComponentName name) {
            IgnoredListActivity.this.bound = false;
            IgnoredListActivity.this.monitorShieldService = null;
        }
    };

    public int getLayoutId() {
        return R.layout.activity_ignored_list;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
        bindService(new Intent(this, MonitorShieldService.class), this.serviceConnection, 1);
    }

    private void init() {
        RecyclerView rvIgnoredApps = (RecyclerView) findViewById(R.id.rv_ignored_apps);
        rvIgnoredApps.setLayoutManager(new LinearLayoutManager(this));
        UserWhiteList userWhiteList = this.monitorShieldService.getUserWhiteList();
        final List<IProblem> iProblems = new ArrayList();
        iProblems.addAll(userWhiteList.getSet());
        final IgnoredAppsAdapter adapter = new IgnoredAppsAdapter(this, iProblems, this.monitorShieldService);
        rvIgnoredApps.setAdapter(adapter);
        final TextView tvCountApps = (TextView) findViewById(R.id.tv_count_apps);
        TypeFaceUttils.setNomal((Context) this, tvCountApps);
        tvCountApps.setText(userWhiteList.getItemCount() + " " + getResources().getString(R.string.apps_ignored));
        adapter.setOnItemClickListener(new IgnoredAppsAdapter.OnItemClickListener() {
            public void onItemClick(View itemView, final int position) {
                String dialogMessage;
                IProblem iProblem = (IProblem) iProblems.get(position);
                Builder builder = new Builder(IgnoredListActivity.this, R.style.MyAlertDialogStyle);
                builder.setTitle(IgnoredListActivity.this.getString(R.string.warning)).setPositiveButton(IgnoredListActivity.this.getString(R.string.accept), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.removeItem(position);
                        tvCountApps.setText(new UserWhiteList(IgnoredListActivity.this).getItemCount() + " " + IgnoredListActivity.this.getResources().getString(R.string.apps_ignored));
                    }
                }).setNegativeButton(IgnoredListActivity.this.getString(R.string.cancel), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                if (iProblem.getType() == IProblem.ProblemType.AppProblem) {
                    dialogMessage = IgnoredListActivity.this.getString(R.string.remove_ignored_app_message) + " " + Utils.getAppNameFromPackage(IgnoredListActivity.this, ((AppProblem) iProblem).getPackageName());
                } else {
                    dialogMessage = ((SystemProblem) iProblem).getWhiteListOnRemoveDescription(IgnoredListActivity.this);
                }
                builder.setMessage(dialogMessage);
                builder.show();
            }
        });
    }

    protected void onStop() {
        super.onStop();
        if (this.bound && this.monitorShieldService != null) {
            unbindService(this.serviceConnection);
            this.bound = false;
        }
    }
}
