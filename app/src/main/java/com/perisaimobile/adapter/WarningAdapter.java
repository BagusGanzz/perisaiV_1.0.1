package com.perisaimobile.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.perisaimobile.iface.IProblem;
import com.perisaimobile.iface.IProblem.ProblemType;
import com.perisaimobile.model.AppProblem;
import com.perisaimobile.model.PermissionData;
import com.perisaimobile.model.SystemProblem;
import com.perisaimobile.model.WarningData;
import com.perisaimobile.util.TypeFaceUttils;
import com.studioninja.locker.R;

import org.zakariya.stickyheaders.BuildConfig;

import java.util.ArrayList;

public class WarningAdapter extends Adapter<WarningAdapter.WarningHolder> {
    Context c;
    IProblem problem;
    ArrayList<WarningData> warningDatas = new ArrayList();

    public class WarningHolder extends ViewHolder {
        public ImageView iv_warning_icon;
        public TextView tv_warning_detail;
        public TextView tv_warning_title;

        public WarningHolder(View view) {
            super(view);
            this.iv_warning_icon = (ImageView) view.findViewById(R.id.iv_warning_icon);
            this.tv_warning_title = (TextView) view.findViewById(R.id.tv_warning_title);
            this.tv_warning_detail = (TextView) view.findViewById(R.id.tv_warning_detail);
            TypeFaceUttils.setNomal(WarningAdapter.this.c, this.tv_warning_title);
            TypeFaceUttils.setNomal(WarningAdapter.this.c, this.tv_warning_detail);
        }
    }

    public WarningAdapter(Context c, IProblem problem) {
        this.c = c;
        this.problem = problem;
        WarningData wd;
        if (problem.getType() == ProblemType.AppProblem) {
            AppProblem bp = (AppProblem) problem;
            for (PermissionData ad : bp.getPermissionData()) {
                wd = new WarningData();
                wd.icon = ContextCompat.getDrawable(c, setPermissionIcon(ad.getPermissionName()));
                wd.title = setPermissionTitle(ad.getPermissionName());
                wd.text = setPermissionMessage(ad.getPermissionName());
                this.warningDatas.add(wd);
            }
            if (!bp.getInstalledThroughGooglePlay()) {
                wd = new WarningData();
                wd.icon = ContextCompat.getDrawable(c, R.mipmap.information);
                wd.title = c.getResources().getString(R.string.title_installedGPlay);
                wd.text = c.getResources().getString(R.string.installedGPlay_message);
                this.warningDatas.add(wd);
                return;
            }
            return;
        }
        SystemProblem bp2 = (SystemProblem) problem;
        Context context = c;
        wd = new WarningData();
        wd.icon = bp2.getSubIcon(context);
        wd.title = bp2.getSubTitle(context);
        wd.text = bp2.getDescription(context);
        this.warningDatas.add(wd);
    }

    public WarningHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WarningHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_warning, parent, false));
    }

    public void onBindViewHolder(WarningHolder holder, int position) {
        WarningData warningData = (WarningData) this.warningDatas.get(position);
        holder.iv_warning_icon.setImageDrawable(warningData.icon);
        holder.tv_warning_title.setText(warningData.title);
        holder.tv_warning_detail.setText(warningData.text);
    }

    public int getItemCount() {
        return this.warningDatas.size();
    }

    public String setPermissionMessage(String permissionName) {
        String message = BuildConfig.FLAVOR;
        Resources resources = this.c.getResources();
        if (permissionName.contains("READ_PHONE_STATE")) {
            return resources.getString(R.string.read_phone_message);
        }
        if (permissionName.contains("ACCESS_FINE_LOCATION")) {
            return resources.getString(R.string.access_fine_message);
        }
        if (permissionName.contains("READ_SMS")) {
            return resources.getString(R.string.read_sms_message);
        }
        if (permissionName.contains("WRITE_SMS")) {
            return resources.getString(R.string.write_sms_message);
        }
        if (permissionName.contains("SEND_SMS")) {
            return resources.getString(R.string.send_sms_message);
        }
        if (permissionName.contains("READ_HISTORY_BOOKMARKS")) {
            return resources.getString(R.string.read_history_message);
        }
        if (permissionName.contains("WRITE_HISTORY_BOOKMARKS")) {
            return resources.getString(R.string.write_history_message);
        }
        if (permissionName.contains("CALL_PHONE")) {
            return resources.getString(R.string.call_phone_message);
        }
        if (permissionName.contains("PROCESS_OUTGOING_CALLS")) {
            return resources.getString(R.string.outgoing_phone_message);
        }
        if (permissionName.contains("RECORD_AUDIO")) {
            return resources.getString(R.string.record_audio_message);
        }
        if (permissionName.contains("CAMERA")) {
            return resources.getString(R.string.camera_message);
        }
        return message;
    }

    public String setPermissionTitle(String permissionName) {
        String message = BuildConfig.FLAVOR;
        Resources resources = this.c.getResources();
        if (permissionName.contains("READ_PHONE_STATE")) {
            return resources.getString(R.string.phone_data_shared);
        }
        if (permissionName.contains("ACCESS_FINE_LOCATION")) {
            return resources.getString(R.string.location_shared);
        }
        if (permissionName.contains("READ_SMS")) {
            return resources.getString(R.string.read_your_sms);
        }
        if (permissionName.contains("WRITE_SMS")) {
            return resources.getString(R.string.write_sms_title);
        }
        if (permissionName.contains("SEND_SMS")) {
            return resources.getString(R.string.send_sms_title);
        }
        if (permissionName.contains("READ_HISTORY_BOOKMARKS")) {
            return resources.getString(R.string.read_history_bookmark_title);
        }
        if (permissionName.contains("WRITE_HISTORY_BOOKMARKS")) {
            return resources.getString(R.string.write_history_bookmark_title);
        }
        if (permissionName.contains("CALL_PHONE")) {
            return resources.getString(R.string.can_make_call_title);
        }
        if (permissionName.contains("PROCESS_OUTGOING_CALLS")) {
            return resources.getString(R.string.outgoing_calls_title);
        }
        if (permissionName.contains("RECORD_AUDIO")) {
            return resources.getString(R.string.record_audio_title);
        }
        if (permissionName.contains("CAMERA")) {
            return resources.getString(R.string.access_camera_title);
        }
        return message;
    }

    public int setPermissionIcon(String permissionName) {
        if (permissionName.contains("READ_PHONE_STATE")) {
            return R.mipmap.phone_icon;
        }
        if (permissionName.contains("ACCESS_FINE_LOCATION")) {
            return R.mipmap.fine_location_icon;
        }
        if (permissionName.contains("READ_SMS")) {
            return R.mipmap.read_sms;
        }
        if (permissionName.contains("WRITE_SMS")) {
            return R.mipmap.send_sms;
        }
        if (permissionName.contains("SEND_SMS")) {
            return R.mipmap.send_sms;
        }
        if (permissionName.contains("READ_HISTORY_BOOKMARKS")) {
            return R.mipmap.history_icon;
        }
        if (permissionName.contains("WRITE_HISTORY_BOOKMARKS")) {
            return R.mipmap.history_icon;
        }
        if (permissionName.contains("CALL_PHONE")) {
            return R.mipmap.phone_icon;
        }
        if (permissionName.contains("PROCESS_OUTGOING_CALLS")) {
            return R.mipmap.phone_icon;
        }
        if (permissionName.contains("RECORD_AUDIO")) {
            return R.mipmap.record_audio_icon;
        }
        if (permissionName.contains("CAMERA")) {
            return R.mipmap.camera_icon;
        }
        return 0;
    }
}
