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

import com.perisaimobile.model.AppLock;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import java.util.List;


public class ResultAppLockApdater extends Adapter<ResultAppLockApdater.ViewHolder> {
    private static OnItemClickListener listener;
    private List<AppLock> appLocks;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public ImageView imgIconApp;
        public TextView tvAppName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvAppName = (TextView) itemView.findViewById(R.id.tv_application_name);
            this.imgIconApp = (ImageView) itemView.findViewById(R.id.img_icon_app);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ResultAppLockApdater(Context context, List<AppLock> appLocks) {
        this.context = context;
        this.appLocks = appLocks;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_app_lock, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        AppLock appLock = (AppLock) this.appLocks.get(position);
        holder.imgIconApp.setImageDrawable(Utils.getIconFromPackage(appLock.getPackageName(), this.context));
        holder.tvAppName.setText(appLock.getName());
        holder.checkBox.setChecked(appLock.isLock());
        holder.checkBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ResultAppLockApdater.listener != null) {
                    ResultAppLockApdater.listener.onItemClick(holder.checkBox, position);
                }
            }
        });
        TypeFaceUttils.setNomal(this.context, holder.tvAppName);
    }

    public int getItemCount() {
        return this.appLocks.size();
    }
}
