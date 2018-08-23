package com.perisaimobile.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.perisaimobile.adapter.AppLockAdapter;
import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.model.AppLock;
import com.perisaimobile.model.AppsLocked;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class AppLockHomeActivity extends BaseToolbarActivity {
    private AppLockAdapter adapter;
    private List<AppLock> appLocks;
    private AppsLocked appsLocked;
    @BindView(R.id.rv_app_lock)
    RecyclerView rv_app_lock;
    @BindView(R.id.title)
    TextView title;

    private class LoadAppTask extends AsyncTask<Void, Void, Void> {
        private LoadAppTask() {
        }

        protected Void doInBackground(Void... params) {
            AppLockHomeActivity.this.appsLocked = new AppsLocked(AppLockHomeActivity.this);
            AppLockHomeActivity.this.appLocks.addAll(AppLockHomeActivity.this.getApps());
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AppLockHomeActivity.this.adapter.notifyAllSectionsDataSetChanged();
        }
    }

    public int getLayoutId() {
        return R.layout.activity_app_lock_home;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("app sizes"," test");


        initView();
        new LoadAppTask().execute(new Void[0]);
    }

    private void initView() {


        TypeFaceUttils.setNomal((Context) this, this.title);
        this.rv_app_lock.setLayoutManager(new LinearLayoutManager(this));
        this.rv_app_lock.setHasFixedSize(true);
        this.appLocks = new ArrayList();
        this.adapter = new AppLockAdapter(this, this.appLocks);
        this.rv_app_lock.setAdapter(this.adapter);


        this.adapter.setOnItemClickListener(new AppLockAdapter.OnItemClickListener() {
            public void onItemClick(ToggleButton toggleButton, int position, int sectionIndex, int itemIndex) {
                if (toggleButton.isChecked()) {
                    ((AppLock) AppLockHomeActivity.this.appLocks.get(position)).setLock(false);
                    AppLockHomeActivity.this.appsLocked.remove((AppLock) AppLockHomeActivity.this.appLocks.get(position));
                } else {
                    ((AppLock) AppLockHomeActivity.this.appLocks.get(position)).setLock(true);
                    AppLockHomeActivity.this.appsLocked.add((AppLock) AppLockHomeActivity.this.appLocks.get(position));
                }
                AppLockHomeActivity.this.adapter.notifySectionItemChanged(sectionIndex, itemIndex);
            }
        });
    }

    private List<AppLock> getApps() {
        List<AppLock> apps = new ArrayList();
        PackageManager packageManager = getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        for (ResolveInfo ri : packageManager.queryIntentActivities(mainIntent, 0)) {
            AppLock appLock = new AppLock(Utils.getAppNameFromPackage(this, ri.activityInfo.packageName), ri.activityInfo.packageName, false);
            if (this.appsLocked.isAppLocked(appLock.getPackageName())) {
                appLock.setLock(true);
            }
            apps.add(appLock);
        }


        return apps;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_lock_home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings /*2131820951*/:
                startActivity(new Intent(this, AppLockSettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
