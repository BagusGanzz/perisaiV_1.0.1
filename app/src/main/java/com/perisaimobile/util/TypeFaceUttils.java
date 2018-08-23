package com.perisaimobile.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.RadioButton;
import android.widget.TextView;

public class TypeFaceUttils {
    private static final String bold = "utm_avo_bold.ttf";
    private static final String bold_italic = "utm_avo_bold_italic.ttf";
    private static final String italic = "utm_avo_italic.ttf";
    private static final String nomal = "utm_avo.ttf";

    public static void setNomal(Context context, TextView textView) {
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), nomal));
    }

    public static void setNomal(Context context, RadioButton radioButton) {
        radioButton.setTypeface(Typeface.createFromAsset(context.getAssets(), nomal));
    }

    public static void setBold(Context context, TextView textView) {
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), bold));
    }

    public static void setItalic(Context context, TextView textView) {
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), italic));
    }

    public static void setBoldItalic(Context context, TextView textView) {
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), bold_italic));
    }
}
