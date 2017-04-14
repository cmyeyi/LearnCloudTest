package com.aile.www.basesdk.utils;

import android.util.Log;

public class LogUtils {

    public static void showLog(String log) {
        showLog("####YY", log);
    }

    public static void showLog(String tag, String log) {
        Log.i(tag, log);
    }
}
