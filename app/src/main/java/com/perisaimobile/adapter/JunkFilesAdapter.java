package com.perisaimobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.perisaimobile.model.JunkOfApplication;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import java.util.Collections;
import java.util.List;

public class JunkFilesAdapter extends Adapter<JunkFilesAdapter.ViewHolder> {
    private Context context;
    private List<JunkOfApplication> junkOfApplications;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ImageView imgIconApp;
        public TextView tvAppCacheSize;
        public TextView tvAppName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvAppName = (TextView) itemView.findViewById(R.id.tv_app_name);
            this.tvAppCacheSize = (TextView) itemView.findViewById(R.id.tv_app_cache_size);
            this.imgIconApp = (ImageView) itemView.findViewById(R.id.img_app_icon);
        }
    }

    public JunkFilesAdapter(Context context, List<JunkOfApplication> junkOfApplications) {
        this.context = context;
        this.junkOfApplications = junkOfApplications;
        Collections.sort(junkOfApplications, Collections.reverseOrder());
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_junk_files, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        JunkOfApplication junkOfApplication = (JunkOfApplication) this.junkOfApplications.get(position);
        TextView tvAppName = holder.tvAppName;
        TypeFaceUttils.setNomal(this.context, tvAppName);
        tvAppName.setText(Utils.getAppNameFromPackage(this.context, junkOfApplication.getPackageName()));
        TextView tvAppCacheSize = holder.tvAppCacheSize;
        TypeFaceUttils.setNomal(this.context, tvAppCacheSize);
        tvAppCacheSize.setText(Utils.convertFileSizeToString(junkOfApplication.getCacheSize()));
        holder.imgIconApp.setImageDrawable(Utils.getIconFromPackage(junkOfApplication.getPackageName(), this.context));
    }

    public int getItemCount() {
        return this.junkOfApplications.size();
    }
}
