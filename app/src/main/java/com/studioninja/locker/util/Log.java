package com.studioninja.locker.util;


import com.studioninja.locker.BuildConfig;

public class Log {

    /*Writing information to the java console takes time and
    resources, and should not be done unless necessary so do
    not print any Log for Prod build*/

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG_MODE) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (BuildConfig.DEBUG_MODE) {
            android.util.Log.e(tag, msg, throwable);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG_MODE) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG_MODE) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG_MODE) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG_MODE) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg,Exception e) {
        if (BuildConfig.DEBUG_MODE) {
            android.util.Log.w(tag, msg,e);
        }
    }
}
