package com.aile.www.basesdk.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 网络请求日志输出
 */

public final class SDKLogger {

    private static boolean DEBUG = false;

    public static void debug(boolean debug) {
        DEBUG = debug;
    }

    private static final String TAG_NETWORK = ":network";

    private static final String TAG_HTTP = ":wyhttp";

    public static boolean DEBUG() {
        return DEBUG;
    }

    /**
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(tag)) {
            return;
        }

        if (DEBUG()) {
            Log.e(tag, msg);
        }
    }

    /**
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(tag)) {
            return;
        }

        if (DEBUG()) {
            Log.d(tag, msg);
        }
    }

    /**
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(tag)) {
            return;
        }

        if (DEBUG()) {
            Log.v(tag, msg);
        }
    }

    /**
     * @param msg
     */
    public static void network(String msg) {
        d(TAG_NETWORK, msg);
    }

    /**
     * @param msg
     */
    public static void http(String msg) {
        d(TAG_HTTP, msg);
    }
}
