package com.perisaimobile.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perisaimobile.activities.ScanningResultActivity;
import com.perisaimobile.adapter.WarningAdapter;
import com.perisaimobile.iface.IProblem;
import com.perisaimobile.iface.IProblem.ProblemType;
import com.perisaimobile.model.AppProblem;
import com.perisaimobile.model.MenacesCacheSet;
import com.perisaimobile.model.SystemProblem;
import com.perisaimobile.util.ProblemsDataSetTools;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProblemDetailFragment extends Fragment {
    IProblem _problem = null;
    private boolean _uninstallingPackage = false;
    private ScanningResultActivity activity;
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
    @BindView(R.id.rv_warning_problem)
    RecyclerView rv_warning_problem;
    @BindView(R.id.tv_app_name)
    TextView tv_app_name;

    private void customFont() {
        TypeFaceUttils.setNomal(getActivity(), this.tv_app_name);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_detail, container, false);
        ButterKnife.bind(this, view);
        customFont();
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = (ScanningResultActivity) getActivity();
        this._problem = this.activity.getComu();
        if (this._problem != null) {
            this.rv_warning_problem.setAdapter(new WarningAdapter(getActivity(), this._problem));
            this.rv_warning_problem.setLayoutManager(new LinearLayoutManager(getActivity()));
            init();
            return;
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void onResume() {
        super.onResume();
        initForResume();
    }

    private void init() {
        if (this._problem.getType() == ProblemType.AppProblem) {
            this.ll_layout_for_app.setVisibility(0);
            this.ll_layout_for_system.setVisibility(8);
            final AppProblem appProblem = (AppProblem) this._problem;
            this.bt_uninstall_app.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ProblemDetailFragment.this._uninstallingPackage = true;
                    ProblemDetailFragment.this.startActivity(new Intent("android.intent.action.DELETE", Uri.fromParts("package", appProblem.getPackageName(), null)));
                }
            });
            this.bt_trust_app.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    new Builder(ProblemDetailFragment.this.getActivity(), R.style.MyAlertDialogStyle).setTitle(ProblemDetailFragment.this.getString(R.string.warning)).setMessage(ProblemDetailFragment.this.getString(R.string.dialog_trust_app)).setPositiveButton(ProblemDetailFragment.this.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ProblemDetailFragment.this.sendResult(ProblemDetailFragment.this._problem, 1);
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create().show();
                }
            });
            Drawable s = Utils.getIconFromPackage(appProblem.getPackageName(), getActivity());
            getActivity().setTitle(Utils.getAppNameFromPackage(getActivity(), appProblem.getPackageName()));
            this.iv_icon_app.setImageDrawable(s);
            this.tv_app_name.setText(Utils.getAppNameFromPackage(getActivity(), appProblem.getPackageName()));
            return;
        }
        this.ll_layout_for_app.setVisibility(8);
        this.ll_layout_for_system.setVisibility(0);
        final SystemProblem systemProblem = (SystemProblem) this._problem;
        this.iv_icon_app.setImageDrawable(systemProblem.getIcon(getActivity()));
        this.tv_app_name.setText(systemProblem.getTitle(getActivity()));
        this.bt_open_setting.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                systemProblem.doAction(ProblemDetailFragment.this.getActivity());
            }
        });
        this.bt_ignore_setting.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ProblemDetailFragment.this.sendResult(ProblemDetailFragment.this._problem, 2);
            }
        });
    }

    private void initForResume() {
        if (this._uninstallingPackage) {
            if (this._problem != null) {
                if (!Utils.isPackageInstalled(getActivity(), this._problem.getPackageName())) {
                    sendResult(this._problem, 3);
                    this._uninstallingPackage = false;
                }
            }
        } else if (this._problem.getType() == ProblemType.AppProblem) {
            AppProblem appProblem = (AppProblem) this._problem;
            if (!ProblemsDataSetTools.checkIfPackageInCollection(appProblem.getPackageName(), this.activity.getMonitorShieldService().getMenacesCacheSet().getSet()) && !Utils.isPackageInstalled(getActivity(), appProblem.getPackageName())) {
                MenacesCacheSet menacesCacheSet = this.activity.getMonitorShieldService().getMenacesCacheSet();
                menacesCacheSet.removeItem( appProblem);
                menacesCacheSet.writeToJSON();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        } else if (this._problem.getType() == ProblemType.SystemProblem && !this._problem.problemExists(getActivity())) {
            sendResult(this._problem, 4);
        }
    }

    private void sendResult(IProblem iProblem, int type) {
        this.activity.respond(iProblem, type);
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
