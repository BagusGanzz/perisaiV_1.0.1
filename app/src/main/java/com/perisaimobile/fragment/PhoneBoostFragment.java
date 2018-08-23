package com.perisaimobile.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.perisaimobile.activities.ScanningResultActivity;
import com.perisaimobile.adapter.ApplicationsAdapter;
import com.perisaimobile.dialogs.EnableAccessbilityDialog;
import com.perisaimobile.model.Application;
import com.perisaimobile.service.BoosterService;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneBoostFragment extends Fragment implements OnClickListener {
    private ScanningResultActivity activity;
    private ApplicationsAdapter adapter;
    private BoosterService boosterService;
    boolean bound = false;
    @BindView(R.id.framelayout_boost)
    View framelayout_boost;
    @BindView(R.id.rv_application)
    RecyclerView rv_application;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            PhoneBoostFragment.this.bound = true;
            PhoneBoostFragment.this.boosterService = ((BoosterService.BoosterBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            PhoneBoostFragment.this.boosterService = null;
            PhoneBoostFragment.this.bound = false;
        }
    };
    private int totalBoost;
    @BindView(R.id.tv_boost)
    TextView tv_boost;
    @BindView(R.id.tv_count_running_app)
    TextView tv_count_running_app;
    @BindView(R.id.tv_freeable)
    TextView tv_freeable;
    @BindView(R.id.tv_mb)
    TextView tv_mb;
    @BindView(R.id.tv_memory_boost)
    TextView tv_memory_boost;

    class RemoveApp extends AsyncTask<List<Application>, Application, Void> {
        private int total;

        public RemoveApp() {
            this.total = PhoneBoostFragment.this.totalBoost;
        }

        protected Void doInBackground(List<Application>... params) {
            for (Application removeApp : params[0]) {
                Utils.killBackgroundProcesses(PhoneBoostFragment.this.getActivity(), removeApp.getPackageName());
                publishProgress(new Application[]{removeApp});
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Application... values) {
            super.onProgressUpdate(values);
            PhoneBoostFragment.this.adapter.notifyItemRemoved(PhoneBoostFragment.this.activity.getMonitorShieldService().getRunningApplications().indexOf(values[0]));
            PhoneBoostFragment.this.activity.getMonitorShieldService().getRunningApplications().remove(values[0]);
            this.total = (int) (((long) this.total) - (values[0].getSize() / 1024));
            PhoneBoostFragment.this.tv_memory_boost.setText(String.valueOf(this.total));
            PhoneBoostFragment.this.tv_boost.setText(PhoneBoostFragment.this.getResources().getString(R.string.boost) + " " + PhoneBoostFragment.this.totalBoost + "MB");
            PhoneBoostFragment.this.tv_count_running_app.setText(PhoneBoostFragment.this.activity.getMonitorShieldService().getRunningApplications().size() + " " + PhoneBoostFragment.this.getResources().getString(R.string.apps_running));
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PhoneBoostFragment.this.activity.getMonitorShieldService().getRunningApplications().clear();
            PhoneBoostFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            PhoneBoostFragment.this.activity.refresh();
        }
    }

    private void customFont() {
        TypeFaceUttils.setNomal(getActivity(), this.tv_memory_boost);
        TypeFaceUttils.setNomal(getActivity(), this.tv_mb);
        TypeFaceUttils.setNomal(getActivity(), this.tv_freeable);
        TypeFaceUttils.setNomal(getActivity(), this.tv_count_running_app);
        TypeFaceUttils.setNomal(getActivity(), this.tv_boost);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().startService(new Intent(getActivity(), BoosterService.class));
        getActivity().bindService(new Intent(getActivity(), BoosterService.class), this.serviceConnection, 1);
        View view = inflater.inflate(R.layout.fragment_phone_boost, container, false);
        ButterKnife.bind(this, view);
        customFont();
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = (ScanningResultActivity) getActivity();
        if (this.activity.getMonitorShieldService() == null) {
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            initView();
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onStop() {
        super.onStop();
        if (this.bound && this.boosterService != null) {
            getActivity().unbindService(this.serviceConnection);
            this.bound = false;
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        int total = 0;
        for (Application application : this.activity.getMonitorShieldService().getRunningApplications()) {
            total = (int) (((long) total) + (application.getSize() / 1024));
            application.setChoose(true);
        }
        this.tv_memory_boost.setText(String.valueOf(total));
        this.tv_count_running_app.setText(this.activity.getMonitorShieldService().getRunningApplications().size() + " " + getResources().getString(R.string.apps_running));
        this.rv_application.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.rv_application.setHasFixedSize(true);
        this.adapter = new ApplicationsAdapter(getActivity(), this.activity.getMonitorShieldService().getRunningApplications());
        this.rv_application.setAdapter(this.adapter);
   //     this.rv_application.setItemAnimator(new SlideInOutRightItemAnimator(this.rv_application));
        this.totalBoost = total;
        updateBoostView();
        this.adapter.setOnItemClickListener(new ApplicationsAdapter.OnItemClickListener() {
            public void onItemClick(View itemView, int position) {
                CheckBox checkBox = (CheckBox) itemView;
                if (checkBox.isChecked()) {
                    checkBox.setChecked(true);
                    ((Application) PhoneBoostFragment.this.activity.getMonitorShieldService().getRunningApplications().get(position)).setChoose(true);
                    PhoneBoostFragment.this.totalBoost = (int) (((long) PhoneBoostFragment.this.totalBoost) + (((Application) PhoneBoostFragment.this.activity.getMonitorShieldService().getRunningApplications().get(position)).getSize() / 1024));
                } else {
                    checkBox.setChecked(false);
                    ((Application) PhoneBoostFragment.this.activity.getMonitorShieldService().getRunningApplications().get(position)).setChoose(false);
                    PhoneBoostFragment.this.totalBoost = (int) (((long) PhoneBoostFragment.this.totalBoost) - (((Application) PhoneBoostFragment.this.activity.getMonitorShieldService().getRunningApplications().get(position)).getSize() / 1024));
                }
                PhoneBoostFragment.this.updateBoostView();
            }
        });
        this.framelayout_boost.setOnClickListener(this);
    }

    private void updateBoostView() {
        if (this.totalBoost != 0) {
            this.tv_boost.setText(getResources().getString(R.string.boost) + " " + this.totalBoost + "MB");
            this.framelayout_boost.setVisibility(0);
            return;
        }
        this.tv_boost.setText(getResources().getString(R.string.boost) + " " + this.totalBoost + "MB");
        this.framelayout_boost.setVisibility(8);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.framelayout_boost) {
            final List<Application> removeApps = new ArrayList();
            for (Application application : this.activity.getMonitorShieldService().getRunningApplications()) {
                if (application.isChoose()) {
                    removeApps.add(application);
                }
            }
            if (VERSION.SDK_INT < 18) {
                new RemoveApp().execute(new List[]{removeApps});
            } else if (VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(getActivity())) {
                startActivity(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getActivity().getPackageName())));
            } else if (Utils.isAccessibilitySettingsOn(getActivity())) {
                this.activity.getMonitorShieldService().getRunningApplications().clear();
                getActivity().getSupportFragmentManager().popBackStack();
                this.activity.refresh();
                this.activity.setShowAd(false);
                this.boosterService.boost(removeApps);
            } else {
                EnableAccessbilityDialog dialog = new EnableAccessbilityDialog(getActivity());
                dialog.setCallBack(new EnableAccessbilityDialog.CallBack() {
                    public void execute() {
                        new RemoveApp().execute(new List[]{removeApps});
                    }
                });
                dialog.show();
            }
        }
    }
}
