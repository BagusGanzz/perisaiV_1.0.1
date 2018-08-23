package com.perisaimobile.service;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;

import java.util.List;

public class MyAccessibilityService extends AccessibilityService {
    public static final String BROADCAST_BACK_TAP = "BROADCAST_BACK_TAP";
    public static final String BROADCAST_FORCE_STOP = "BROADCAST_FORCE_STOP";
    public static final String FORCE_STOP = "FORCE_STOP";
    private BroadcastReceiver backTapBroadcast = new BroadcastReceiver() {
        @RequiresApi(api = 16)
        public void onReceive(Context context, Intent intent) {
            MyAccessibilityService.this.performGlobalAction(1);
            MyAccessibilityService.this.performGlobalAction(1);
        }
    };
    private int countStep;
    private BroadcastReceiver forceStopBroadcast = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            MyAccessibilityService.this.force_stop = intent.getBooleanExtra(MyAccessibilityService.FORCE_STOP, false);
        }
    };
    private boolean force_stop;
    Handler handler = new Handler();

    static /* synthetic */ int access$204(MyAccessibilityService x0) {
        int i = x0.countStep + 1;
        x0.countStep = i;
        return i;
    }

    protected void onServiceConnected() {
        super.onServiceConnected();
        registerReceiver(this.forceStopBroadcast, new IntentFilter(BROADCAST_FORCE_STOP));
        registerReceiver(this.backTapBroadcast, new IntentFilter(BROADCAST_BACK_TAP));
    }

    @RequiresApi(api = 18)
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (this.force_stop) {
            if (Utils.isServiceRunning(this, LockService.class)) {
                stopService(new Intent(this, LockService.class));
            }
            if (event.getSource() == null) {
                return;
            }
            if (event.getPackageName().equals("com.android.settings")) {
                final AccessibilityNodeInfo accessibilityNodeInfo = event.getSource();
                this.handler.postDelayed(new Runnable() {
                    public void run() {
                        List<AccessibilityNodeInfo> stop_nodes = MyAccessibilityService.this.getNodeInfo(accessibilityNodeInfo);
                        if (stop_nodes != null && !stop_nodes.isEmpty()) {
                            for (int i = 0; i < stop_nodes.size(); i++) {
                                AccessibilityNodeInfo node = (AccessibilityNodeInfo) stop_nodes.get(i);
                                if (node.getClassName().equals("android.widget.Button")) {
                                    if (!node.isEnabled()) {
                                        MyAccessibilityService.this.countStep = 0;
                                        MyAccessibilityService.this.performGlobalAction(1);
                                        MyAccessibilityService.this.sendBroadcast(new Intent().setAction(BoosterService.BROADCAST_ACCESSIBILITY).putExtra(BoosterService.STOPPED, true));
                                    } else if (MyAccessibilityService.this.countStep >= 2) {
                                        MyAccessibilityService.this.countStep = 0;
                                        MyAccessibilityService.this.performGlobalAction(1);
                                        MyAccessibilityService.this.sendBroadcast(new Intent().setAction(BoosterService.BROADCAST_ACCESSIBILITY).putExtra(BoosterService.STOPPED, true));
                                    } else {
                                        node.performAction(16);
                                        MyAccessibilityService.access$204(MyAccessibilityService.this);
                                    }
                                    node.recycle();
                                }
                            }
                        }
                    }
                }, 1000);
                List<AccessibilityNodeInfo> ok_nodes = null;
                if (event.getText() != null && event.getText().size() == 4) {
                    ok_nodes = event.getSource().findAccessibilityNodeInfosByText(((CharSequence) event.getText().get(3)).toString());
                }
                if (ok_nodes != null && !ok_nodes.isEmpty()) {
                    for (int i = 0; i < ok_nodes.size(); i++) {
                        AccessibilityNodeInfo node = (AccessibilityNodeInfo) ok_nodes.get(i);
                        if (node.getClassName().equals("android.widget.Button")) {
                            node.performAction(16);
                            this.countStep++;
                        }
                        node.recycle();
                    }
                }
            } else if (!event.getPackageName().equals(getPackageName()) && !event.getPackageName().equals("com.android.systemui")) {
                performGlobalAction(1);
                sendBroadcast(new Intent().setAction(BoosterService.BROADCAST_ACCESSIBILITY).putExtra(BoosterService.STOPPED, true));
            }
        }
    }

    public void onInterrupt() {
    }

    @RequiresApi(api = 16)
    protected boolean onKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 3 && event.getAction() == 0) {
            if (Utils.isServiceRunning(this, BoosterService.class)) {
                stopService(new Intent(this, BoosterService.class));
            }
            if (Utils.isServiceRunning(this, LockService.class)) {
                startActivity(Utils.getHomeIntent());
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        MyAccessibilityService.this.stopService(new Intent(MyAccessibilityService.this, LockService.class));
                    }
                }, 500);
            }
        }
        if (event.getKeyCode() == 4 && event.getAction() == 0 && Utils.isServiceRunning(this, LockService.class)) {
            startActivity(Utils.getHomeIntent());
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MyAccessibilityService.this.stopService(new Intent(MyAccessibilityService.this, LockService.class));
                }
            }, 500);
        }
        if (event.getKeyCode() == 187 && event.getAction() == 0 && Utils.isServiceRunning(this, LockService.class)) {
            stopService(new Intent(this, LockService.class));
        }
        return super.onKeyEvent(event);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.forceStopBroadcast);
        unregisterReceiver(this.backTapBroadcast);
    }

    @RequiresApi(api = 18)
    private List<AccessibilityNodeInfo> getNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        List<AccessibilityNodeInfo> stop_nodes = accessibilityNodeInfo.findAccessibilityNodeInfosByText(getString(R.string.force_stop));
        if (stop_nodes.size() == 0) {
            stop_nodes = accessibilityNodeInfo.findAccessibilityNodeInfosByText("Bu\u1ed9c \u0111\u00f3ng");
        }
        if (stop_nodes.size() == 0) {
            stop_nodes = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.android.settings:id/left_button");
        }
        if (stop_nodes.size() == 0) {
            stop_nodes = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.android.settings:id/force_stop_button");
        }
        if (stop_nodes.size() == 0) {
            stop_nodes = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("miui:id/v5_icon_menu_bar_primary_item");
        }
        if (stop_nodes.size() == 0) {
            return accessibilityNodeInfo.findAccessibilityNodeInfosByViewId("com.zui.appsmanager:id/force_stop");
        }
        return stop_nodes;
    }
}
