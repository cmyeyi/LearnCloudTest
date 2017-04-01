package com.aile.cloud.config;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by yeyi on 17/4/1.
 */

public class AppConfig {

    public static void setDebugLogEnabled(boolean isDebug) {
        AVOSCloud.setDebugLogEnabled(isDebug);
    }
}
