package com.aile.www.basesdk.utils;

import android.content.Context;

import java.lang.reflect.Field;

public final class DimensionUtils {
    /**
     * dp转换px
     *
     * @param context
     * @param dip
     * @return
     */
    public static int dpToPx(Context context, float dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);
        return valuePixels;
    }
	
	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static float px2sp(float pxValue, float fontScale){
		// 注意返回float不是int防止产生误差
		return pxValue / fontScale + 0.5f;
	}

	// 获取手机状态栏高度
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

//	/**
//	 * 获取屏幕的宽度 pixel
//	 */
//	public static int getScreenWidth() {
//		DisplayMetrics dm = C.getAppContext().getResources().getDisplayMetrics();
//		return dm.widthPixels;
//	}
//
//	/**
//	 * 获取屏幕的高度 pixel
//	 */
//	public static int getScreenHeight() {
//		DisplayMetrics dm = C.getAppContext().getResources().getDisplayMetrics();
//		return dm.heightPixels;
//	}
//
//	/**
//	 * 获取状态栏的高度 pixel
//	 */
//	public static int getStatusBarHeight() {
//		int statusBar = getStatusBarHeight(C.getAppContext());
//		if (statusBar == 0) {
//			statusBar = (int) (25 * C.getAppContext().getResources().getDisplayMetrics().density + 0.5);
//		}
//		return statusBar;
//	}
}
