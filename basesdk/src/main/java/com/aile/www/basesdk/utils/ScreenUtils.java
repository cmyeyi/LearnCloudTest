package com.aile.www.basesdk.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 获取屏幕宽、高、密度.以及像素与dp的相互转换
 * Created by xiejiuwei on 16/3/31.
 */
public class ScreenUtils {
    private static int screenW;
    private static int screenH;
    private static float screenDensity;

    public static void init(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        screenDensity = metric.density;
    }

    public static int getScreenW(Context context) {
        if (0 == screenW) {
            init(context);
        }
        return screenW;
    }

    public static int getScreenH(Context context) {
        if (0 == screenH) {
            init(context);
        }
        return screenH;
    }

    public static float getScreenDensity(Context context) {
        if (0 == screenDensity) {
            init(context);
        }
        return screenDensity;
    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

}
