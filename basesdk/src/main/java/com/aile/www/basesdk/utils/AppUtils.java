package com.aile.www.basesdk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.os.StatFs;

import com.aile.www.basesdk.R;

import java.io.File;
import java.security.MessageDigest;

/**
 * @Description 常用函数
 */
public class AppUtils {
	private static final String TAG = "elife.AppUtils";

	/** 获取当前应用的版本号 */
	public static String getVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			versionName = packInfo.versionName;
		} catch (NameNotFoundException e) {
		}
		return versionName;
	}

	/** 获取字符串的MD5值 */
	public static String toMD5(String str) {
		try {
			return toMD5(str.getBytes("UTF-8"));
		} catch (Exception e) {
			return "";
		}
	}

	private static String toMD5(byte[] bytes) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytes);
			return toHexString(algorithm.digest(), "");
		} catch (Exception e) {
		}
		return "";
	}

	private static String toHexString(byte[] bytes, String separator) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xFF & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex).append(separator);
		}
		return hexString.toString();
	}

	/** 将bitmap转为圆角的图片 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff313131;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = pixels;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);

			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}

			return output;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}

		return null;
	}

	/** sd卡是否可用 */
	public static boolean isSDCardAviliable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/** 获取sd卡剩余存储空间 , 单位M */
	public static long getSDCardFreeSpace() {
		try {
			if (isSDCardAviliable()) {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				long freeSize = availableBlocks * blockSize / 1024 / 1024;
				return freeSize;
			} else {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	/** 获取手机内部剩余存储空间, 单位M */
	public static long getInternalFreeSpace() {
		try {
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			long freeSize = availableBlocks * blockSize / 1024 / 1024;
			return freeSize;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * 简单的手机信息
	 * 
	 * @return
	 */
	public static String getSimpleMobileInfo() {
		StringBuffer sb = new StringBuffer();
		sb.append(android.os.Build.VERSION.RELEASE);
		sb.append(" ");
		sb.append(android.os.Build.MODEL);
		sb.append(" ");
		sb.append(android.os.Build.BRAND);
		sb.append(" ");
		sb.append(android.os.Build.MANUFACTURER);
		sb.append(" ");
		sb.append(android.os.Build.FINGERPRINT);
		return sb.toString();
	}

	/**
	 * 获取状态栏高度，如果获取失败返回25dp对应像素值
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
			// 如果获取失败  返回默认值  25dp对应像素值
			return context.getResources().getDimensionPixelSize(R.dimen.status_bar_default_height);
		}
	}
}
