package com.perisaimobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.perisaimobile.model.Application;
import com.perisaimobile.util.TypeFaceUttils;
import com.studioninja.locker.R;

import java.util.List;


public class ApplicationsAdapter extends Adapter<ApplicationsAdapter.ViewHolder> {
    private static OnItemClickListener listener;
    private List<Application> applications;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public ImageView imgIconApp;
        public TextView tvAppName;
        public TextView tvSizeApp;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvAppName = (TextView) itemView.findViewById(R.id.tv_application_name);
            this.imgIconApp = (ImageView) itemView.findViewById(R.id.img_icon_app);
            this.tvSizeApp = (TextView) itemView.findViewById(R.id.tv_size_application);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_application);
        }
    }

    public List<Application> getApplications() {
        return this.applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ApplicationsAdapter(Context context, List<Application> applications) {
        this.context = context;
        this.applications = applications;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        Application application = (Application) this.applications.get(position);
        TextView tvAppName = holder.tvAppName;
        ImageView imgIconApp = holder.imgIconApp;
        TextView tvSizeApp = holder.tvSizeApp;
        final CheckBox checkBox = holder.checkBox;
        TypeFaceUttils.setNomal(this.context, tvAppName);
        TypeFaceUttils.setNomal(this.context, tvSizeApp);
        tvAppName.setText(application.getName());
        imgIconApp.setImageDrawable(application.getIcon());
        tvSizeApp.setText(String.valueOf(((int) (application.getSize() / 1024)) + "MB"));
        checkBox.setChecked(((Application) this.applications.get(position)).isChoose());
        checkBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ApplicationsAdapter.listener != null) {
                    ApplicationsAdapter.listener.onItemClick(checkBox, position);
                }
            }
        });
    }

    public int getItemCount() {
        return this.applications.size();
    }
}
