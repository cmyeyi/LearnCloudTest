package com.aile.cloud;

import android.app.Application;

import com.aile.cloud.config.AppConfig;
import com.avos.avoscloud.AVOSCloud;

/**
 * Created by yeyi on 17/4/1.
 */

public class MyLeanCloudApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"9ThylQduJJxQhqOEpqwooFUN-gzGzoHsz","JPNipjTp6Qan66INHQ67BE7Q");
        AppConfig.setDebugLogEnabled(true);
    }
}
