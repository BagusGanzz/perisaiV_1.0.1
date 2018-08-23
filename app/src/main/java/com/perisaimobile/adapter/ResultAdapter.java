package com.perisaimobile.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perisaimobile.iface.IProblem;
import com.perisaimobile.iface.IProblem.ProblemType;
import com.perisaimobile.iface.IResultItemSelectedListener;
import com.perisaimobile.model.AppProblem;
import com.perisaimobile.model.SystemProblem;
import com.perisaimobile.util.ProblemsDataSetTools;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;

public class ResultAdapter extends SectioningAdapter {
    private IResultItemSelectedListener _onItemChangedStateListener = null;
    ArrayList<IProblem> appProblems;
    Context c;
    ArrayList<IProblem> problemArrayList;
    ResultsAdapterProblemItem ri;
    ArrayList<IProblem> systemProblems;

    public class HeaderViewHolder extends org.zakariya.stickyheaders.SectioningAdapter.HeaderViewHolder {
        TextView tv_header;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.tv_header = (TextView) itemView.findViewById(R.id.tv_header);
            TypeFaceUttils.setNomal(ResultAdapter.this.c, this.tv_header);
        }
    }

    public class ResultViewHolder extends ItemViewHolder {
        public ImageView iv_item_icon_app;
        public LinearLayout rl_item_view;
        public TextView tv_item_result_detail;
        public TextView tv_item_result_title;

        public ResultViewHolder(View view) {
            super(view);
            this.iv_item_icon_app = (ImageView) view.findViewById(R.id.iv_item_icon_app);
            this.tv_item_result_title = (TextView) view.findViewById(R.id.tv_item_result_title);
            this.tv_item_result_detail = (TextView) view.findViewById(R.id.tv_item_result_detail);
            this.rl_item_view = (LinearLayout) view.findViewById(R.id.rl_item_view);
            TypeFaceUttils.setNomal(ResultAdapter.this.c, this.tv_item_result_title);
            TypeFaceUttils.setNomal(ResultAdapter.this.c, this.tv_item_result_detail);
            TypeFaceUttils.setNomal(ResultAdapter.this.c, this.tv_item_result_detail);
        }
    }

    public void setResultItemSelectedStateChangedListener(IResultItemSelectedListener listemer) {
        this._onItemChangedStateListener = listemer;
    }

    public ResultAdapter(Context context, ArrayList<IProblem> problemArrayList) {
        this.c = context;
        this.problemArrayList = problemArrayList;
        this.appProblems = new ArrayList();
        ProblemsDataSetTools.getAppProblems(problemArrayList, this.appProblems);
        this.systemProblems = new ArrayList();
        ProblemsDataSetTools.getSystemProblems(problemArrayList, this.systemProblems);
    }

    public int getNumberOfSections() {
        return 2;
    }

    public int getNumberOfItemsInSection(int sectionIndex) {
        switch (sectionIndex) {
            case 0 /*0*/:
                return this.appProblems.size();
            case 1 /*1*/:
                return this.systemProblems.size();
            default:
                return 0;
        }
    }

    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    public org.zakariya.stickyheaders.SectioningAdapter.HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int type) {
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false));
    }

    public ResultViewHolder onCreateItemViewHolder(ViewGroup parent, int type) {
        return new ResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_result, parent, false));
    }

    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int type) {
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        switch (sectionIndex) {
            case 0 /*0*/:
                hvh.tv_header.setText(this.c.getString(R.string.application));
                return;
            case 1 /*1*/:
                hvh.tv_header.setText(this.c.getString(R.string.system));
                return;
            default:
                return;
        }
    }

    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int type) {
        final ResultViewHolder holder = (ResultViewHolder) viewHolder;
        switch (sectionIndex) {
            case org.zakariya.stickyheaders.R.styleable.RecyclerView_android_orientation /*0*/:
                this.ri = new ResultsAdapterProblemItem((IProblem) this.appProblems.get(itemIndex));
                final AppProblem ap = this.ri.getAppProblem();
                holder.tv_item_result_title.setText(Utils.getAppNameFromPackage(this.c, ap.getPackageName()));
                holder.iv_item_icon_app.setImageDrawable(Utils.getIconFromPackage(ap.getPackageName(), this.c));
                if (ap.isDangerous()) {
                    holder.tv_item_result_detail.setTextColor(ContextCompat.getColor(this.c, R.color.HighRiskColor));
                    holder.tv_item_result_detail.setText(R.string.high_risk);
                } else {
                    holder.tv_item_result_detail.setTextColor(ContextCompat.getColor(this.c, R.color.MediumRiskColor));
                    holder.tv_item_result_detail.setText(R.string.medium_risk);
                }
                holder.rl_item_view.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (ResultAdapter.this._onItemChangedStateListener != null) {
                            ResultAdapter.this._onItemChangedStateListener.onItemSelected(ap, holder.iv_item_icon_app, ResultAdapter.this.c);
                        }
                    }
                });
                return;
            case 1 /*1*/:
                final SystemProblem sp = new ResultsAdapterProblemItem((IProblem) this.systemProblems.get(itemIndex)).getSystemProblem();
                holder.tv_item_result_title.setText(sp.getTitle(this.c));
                holder.iv_item_icon_app.setImageDrawable(sp.getIcon(this.c));
                if (sp.isDangerous()) {
                    holder.tv_item_result_detail.setTextColor(ContextCompat.getColor(this.c, R.color.HighRiskColor));
                    holder.tv_item_result_detail.setText(R.string.high_risk);
                } else {
                    holder.tv_item_result_detail.setTextColor(ContextCompat.getColor(this.c, R.color.MediumRiskColor));
                    holder.tv_item_result_detail.setText(R.string.medium_risk);
                }
                holder.rl_item_view.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (ResultAdapter.this._onItemChangedStateListener != null) {
                            ResultAdapter.this._onItemChangedStateListener.onItemSelected(sp, holder.iv_item_icon_app, ResultAdapter.this.c);
                        }
                    }
                });
                return;
            default:
                return;
        }
    }

    public void remove(IProblem iProblem) {
        if (iProblem.getType() == ProblemType.AppProblem) {
            int index = this.appProblems.indexOf(iProblem);
            this.appProblems.remove(iProblem);
            notifySectionItemRemoved(0, index);
        }
        if (iProblem.getType() == ProblemType.SystemProblem) {
            int index = this.systemProblems.indexOf(iProblem);
            this.systemProblems.remove(iProblem);
            notifySectionItemRemoved(1, index);
        }
    }
}
