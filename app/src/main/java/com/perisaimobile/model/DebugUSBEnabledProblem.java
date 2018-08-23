package com.perisaimobile.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;


public class DebugUSBEnabledProblem extends SystemProblem {
    public static final String kSerializationType = "usb";
    final boolean kUSBIsDangerousMenace = false;
    final int kUsbDescriptionId = R.string.usb_message;
    final int kUsbIconResId = R.mipmap.ic_usb;
    final int kUsbTitleId = R.string.system_app_usb_menace_title;
    final int kWhiteListAddText = R.string.usb_add_whitelist_message;
    final int kWhiteListRemoveText = R.string.usb_remove_whitelist_message;

    public ProblemType getType() {
        return ProblemType.SystemProblem;
    }

    public String getSerializationTypeString() {
        return kSerializationType;
    }

    public String getWhiteListOnAddDescription(Context context) {
        return context.getString(R.string.usb_add_whitelist_message);
    }

    public String getWhiteListOnRemoveDescription(Context context) {
        return context.getString(R.string.usb_remove_whitelist_message);
    }

    public String getTitle(Context context) {
        return context.getString(R.string.system_app_usb_menace_title);
    }

    public String getSubTitle(Context context) {
        return context.getString(R.string.usb_title);
    }

    public String getDescription(Context context) {
        return context.getString(R.string.usb_message);
    }

    public Drawable getIcon(Context context) {
        return ContextCompat.getDrawable(context, R.mipmap.ic_usb);
    }

    public Drawable getSubIcon(Context context) {
        return ContextCompat.getDrawable(context, R.mipmap.ic_usb);
    }

    public void doAction(Context context) {
        Utils.openDeveloperSettings(context);
    }

    public boolean isDangerous() {
        return false;
    }

    public boolean problemExists(Context context) {
        return Utils.checkIfUSBDebugIsEnabled(context);
    }

    public String getPackageName(){
        return null;
    }
}
