package com.aile.cloud.net.utils;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * 渠道号工具类
 */
public class ChannelUtils {
    private static final String DEFAULT_CHANNEL = "3430000021";

    private static String sChannelId = DEFAULT_CHANNEL;

    /**
     * 获取渠道号
     */
    @NonNull
    public static String getChannelId(@NonNull Context context) {
        return sChannelId;
    }
}
