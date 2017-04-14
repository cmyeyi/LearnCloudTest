package com.aile.www.basesdk;

import android.content.Context;

/**
 * sdk 初始化基本配置
 */

public class SDKConfig {

    private static Context context;

    public static void init(Context ctx){
        context = ctx.getApplicationContext();
    }

    public static Context context(){
        return context;
    }
}
