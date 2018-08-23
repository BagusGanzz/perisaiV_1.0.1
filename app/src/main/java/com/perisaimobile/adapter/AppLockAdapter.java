package com.perisaimobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.perisaimobile.model.AppLock;
import com.perisaimobile.util.TypeFaceUttils;
import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppLockAdapter extends SectioningAdapter {
    private static OnItemClickListener listener;
    List<AppLock> appLocks = new ArrayList();
    List<AppLock> apps;
    private Context c;
    List<AppLock> recommendApps = new ArrayList();

    public interface OnItemClickListener {
        void onItemClick(ToggleButton toggleButton, int i, int i2, int i3);
    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView tv_header;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.tv_header = (TextView) itemView.findViewById(R.id.tv_header);
            TypeFaceUttils.setNomal(AppLockAdapter.this.c, this.tv_header);
        }
    }

    public class ResultViewHolder extends ItemViewHolder {
        public ImageView imgIconApp;
        private View la_item;
        public ToggleButton toggle_lock;
        public TextView tvAppName;

        public ResultViewHolder(View view) {
            super(view);
            this.la_item = view.findViewById(R.id.la_item);
            this.tvAppName = (TextView) view.findViewById(R.id.tv_application_name);
            this.imgIconApp = (ImageView) view.findViewById(R.id.img_icon_app);
            this.toggle_lock = (ToggleButton) view.findViewById(R.id.toggle_lock);
            TypeFaceUttils.setNomal(AppLockAdapter.this.c, this.tvAppName);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AppLockAdapter(Context c, List<AppLock> apps) {
        this.c = c;
        this.apps = apps;
        getSectionList();
    }

    public int getNumberOfSections() {
        return 2;
    }

    public int getNumberOfItemsInSection(int sectionIndex) {
        switch (sectionIndex) {
            case 0 /*0*/:
                return this.recommendApps.size();
            case 1 /*1*/:
                return this.appLocks.size();
            default:
                return 0;
        }
    }

    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    public SectioningAdapter.HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false));
    }

    public ResultViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        return new ResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_lock, parent, false));
    }


    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        switch (sectionIndex) {
            case 0 /*0*/:
                hvh.tv_header.setText(this.c.getResources().getString(R.string.recommend_to_lock));
                return;
            case 1 /*1*/:
                hvh.tv_header.setText(this.c.getResources().getString(R.string.other));
                return;
            default:
                return;
        }
    }

    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, final int itemIndex,  int itemUserType) {
        final ResultViewHolder holder = (ResultViewHolder) viewHolder;
        final AppLock appLock;
        switch (sectionIndex) {
            case 0 /*0*/:
                appLock = (AppLock) this.recommendApps.get(itemIndex);
                holder.tvAppName.setText(appLock.getName());
                holder.imgIconApp.setImageDrawable(Utils.getIconFromPackage(appLock.getPackageName(), this.c));
                holder.toggle_lock.setChecked(appLock.isLock());
                holder.la_item.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (AppLockAdapter.listener != null) {
                            AppLockAdapter.listener.onItemClick(holder.toggle_lock, AppLockAdapter.this.apps.indexOf(appLock), 0, itemIndex);
                        }
                    }
                });
                holder.toggle_lock.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (holder.toggle_lock.isChecked()) {
                            holder.toggle_lock.setChecked(false);
                        } else {
                            holder.toggle_lock.setChecked(true);
                        }
                        if (AppLockAdapter.listener != null) {
                            AppLockAdapter.listener.onItemClick(holder.toggle_lock, AppLockAdapter.this.apps.indexOf(appLock), 0, itemIndex);
                        }
                    }
                });
                return;
            case 1 /*1*/:
                appLock = (AppLock) this.appLocks.get(itemIndex);
                holder.tvAppName.setText(appLock.getName());
                holder.imgIconApp.setImageDrawable(Utils.getIconFromPackage(appLock.getPackageName(), this.c));
                holder.toggle_lock.setChecked(appLock.isLock());
                holder.la_item.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (AppLockAdapter.listener != null) {
                            AppLockAdapter.listener.onItemClick(holder.toggle_lock, AppLockAdapter.this.apps.indexOf(appLock), 1, itemIndex);
                        }
                    }
                });
                holder.toggle_lock.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (holder.toggle_lock.isChecked()) {
                            holder.toggle_lock.setChecked(false);
                        } else {
                            holder.toggle_lock.setChecked(true);
                        }
                        if (AppLockAdapter.listener != null) {
                            AppLockAdapter.listener.onItemClick(holder.toggle_lock, AppLockAdapter.this.apps.indexOf(appLock), 1, itemIndex);
                        }
                    }
                });
                return;
            default:
                return;
        }
    }

    public void notifyAllSectionsDataSetChanged() {
        getSectionList();
        super.notifyAllSectionsDataSetChanged();
    }

    private void getSectionList() {
        this.recommendApps.clear();
        this.appLocks.clear();
        for (AppLock appLock : this.apps) {
            if (Utils.isRecommendAppLock(appLock.getPackageName())) {
                this.recommendApps.add(appLock);
            } else {
                this.appLocks.add(appLock);
            }
        }
        Collections.sort(this.recommendApps);
        Collections.sort(this.appLocks);
    }

    public void notifySectionItemChanged(int sectionIndex, int itemIndex) {
        boolean z = true;
        boolean z2;
        switch (sectionIndex) {
            case 0 /*0*/:
                AppLock appLock = (AppLock) this.recommendApps.get(itemIndex);
                if (appLock.isLock()) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                appLock.setLock(z2);
                AppLock app = (AppLock) this.apps.get(this.apps.indexOf(appLock));
                if (app.isLock()) {
                    z = false;
                }
                app.setLock(z);
                break;
            case 1 /*1*/:
                AppLock appLock_1 = (AppLock) this.appLocks.get(itemIndex);
                if (appLock_1.isLock()) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                appLock_1.setLock(z2);
                AppLock app_1 = (AppLock) this.apps.get(this.apps.indexOf(appLock_1));
                if (app_1.isLock()) {
                    z = false;
                }
                app_1.setLock(z);
                break;
        }
        super.notifySectionItemChanged(sectionIndex, itemIndex);
    }
}
