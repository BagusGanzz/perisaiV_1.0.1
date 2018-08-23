package com.perisaimobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.perisaimobile.iface.IPackageChangesListener;

public class PackageBroadcastReceiver extends BroadcastReceiver {
    static IPackageChangesListener _listener;

    public static void setPackageBroadcastListener(IPackageChangesListener listener) {
        _listener = listener;
    }

    public void onReceive(Context ctx, Intent intent) {
        if (_listener != null && "android.intent.action.PACKAGE_ADDED".equals(intent.getAction())) {
            _listener.OnPackageAdded(intent);
        }
        if (_listener != null && "android.intent.action.PACKAGE_REMOVED".equals(intent.getAction())) {
            _listener.OnPackageRemoved(intent);
        }
    }
}
