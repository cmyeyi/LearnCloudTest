package com.aile.www.basesdk.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.util.Map;

/**
 * 1.url转换成uri
 * 2.uri 通过Builder 动态添加path
 * 3.paths参数必须按照规定顺序组装
 */

public final class UriBuilder {

    public static String build(String url, Map<String, String> parameters) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        if (SDKLogger.DEBUG()) {
            SDKLogger.http(url);
        }

        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();

        if (null != parameters && !parameters.isEmpty()) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (!TextUtils.isEmpty(entry.getValue()) && !TextUtils.isEmpty(entry.getValue())) {
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        if (SDKLogger.DEBUG()) {
            SDKLogger.http(builder.toString());
        }

        return builder.build().toString();
    }
}
