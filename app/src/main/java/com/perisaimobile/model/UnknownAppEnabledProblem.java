package com.perisaimobile.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.perisaimobile.util.Utils;
import com.studioninja.locker.R;


public class UnknownAppEnabledProblem extends SystemProblem {
    public static final String kSerializationType = "unknownApp";
    final int kUnknownAppDescriptionId = R.string.unknownApp_message;
    final int kUnknownAppIconResId = R.mipmap.ic_information;
    final int kUnknownAppTitleId = R.string.system_app_unknown_app_menace_title;
    final int kWhiteListAddText = R.string.unknownApp_add_whitelist_message;
    final int kWhiteListRemoveText = R.string.unknownApp_remove_whitelist_message;

    public ProblemType getType() {
        return ProblemType.SystemProblem;
    }

    public String getSerializationTypeString() {
        return kSerializationType;
    }

    public String getWhiteListOnAddDescription(Context context) {
        return context.getString(R.string.unknownApp_add_whitelist_message);
    }

    public String getWhiteListOnRemoveDescription(Context context) {
        return context.getString(R.string.unknownApp_remove_whitelist_message);
    }

    public String getTitle(Context context) {
        return context.getString(R.string.system_app_unknown_app_menace_title);
    }

    public String getSubTitle(Context context) {
        return context.getString(R.string.usb_title);
    }

    public String getDescription(Context context) {
        return context.getString(R.string.unknownApp_message);
    }

    public Drawable getIcon(Context context) {
        return ContextCompat.getDrawable(context, R.mipmap.ic_information);
    }

    public Drawable getSubIcon(Context context) {
        return ContextCompat.getDrawable(context, R.mipmap.ic_information);
    }

    public void doAction(Context context) {
        Utils.openSecuritySettings(context);
    }

    public boolean isDangerous() {
        return false;
    }

    public boolean problemExists(Context context) {
        return Utils.checkIfUnknownAppIsEnabled(context);
    }

    public String getPackageName(){
        return null;
    }
}
