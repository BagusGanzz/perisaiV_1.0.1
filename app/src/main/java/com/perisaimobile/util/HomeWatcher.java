package com.perisaimobile.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HomeWatcher {
    private Context mContext;
    private IntentFilter mFilter = new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS");
    private OnHomePressedListener mListener;
    private InnerRecevier mRecevier;

    public interface OnHomePressedListener {
        void onHomeLongPressed();

        void onHomePressed();
    }

    class InnerRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        InnerRecevier() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
                String reason = intent.getStringExtra("reason");
                if (reason != null && HomeWatcher.this.mListener != null) {
                    if (reason.equals("homekey")) {
                        HomeWatcher.this.mListener.onHomePressed();
                    } else if (reason.equals("recentapps")) {
                        HomeWatcher.this.mListener.onHomeLongPressed();
                    }
                }
            }
        }
    }

    public HomeWatcher(Context context) {
        this.mContext = context;
    }

    public void setOnHomePressedListener(OnHomePressedListener listener) {
        this.mListener = listener;
        this.mRecevier = new InnerRecevier();
    }

    public void startWatch() {
        if (this.mRecevier != null) {
            this.mContext.registerReceiver(this.mRecevier, this.mFilter);
        }
    }

    public void stopWatch() {
        if (this.mRecevier != null) {
            this.mContext.unregisterReceiver(this.mRecevier);
        }
    }
}
