package com.aile.www.basesdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

/***
 * 封装ImageLoader.Disk
 * 
 */
public class DiskCache {

	private BitmapFactory.Options mDefaultOptions = new BitmapFactory.Options();
	private static DiskCache mInstance;

	public static DiskCache getInstance() {
		if (mInstance == null) {
			synchronized (DiskCache.class) {
				if (mInstance == null) {
					mInstance = new DiskCache();
				}
			}

		}

		return mInstance;
	}

	private DiskCache() {
		mDefaultOptions.inJustDecodeBounds = false;
		mDefaultOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		mDefaultOptions.inPurgeable = true;
		mDefaultOptions.inInputShareable = true;
	}

	/**
	 * 保存文件~~~这里线程安全
	 * 
	 * @param url
	 * @param bitmap
	 * @return
	 */
	public synchronized boolean saveBitmapToDisk(String url, Bitmap bitmap) {
		if (TextUtils.isEmpty(url) || bitmap == null) {
			return false;
		}

		try {
			return ImageLoader.getInstance().getDiskCache().save(url, bitmap);
		} catch (IOException e) {
			// TODO: handle exception
		}
		return false;
	}

	public Bitmap getBitmapFromDisk(String url) {
		return getBitmapFromDisk(url, null);
	}

	public Bitmap getBitmapFromDisk(String url, Options opts) {

		File file = ImageLoader.getInstance().getDiskCache().get(url);
		if (file == null) {
			return null;

		}
		if (opts == null) {
			return getBitmapFromDiskInternal(file, mDefaultOptions);
		} else {
			return getBitmapFromDiskInternal(file, opts);
		}

	}

	public static String getDiskAbsolutePath() {
		return ImageLoader.getInstance().getDiskCache().getDirectory()
				.getAbsolutePath();
	}

	private static Bitmap getBitmapFromDiskInternal(File f, Options opts) {

		if (f == null) {
			return null;
		}

		try {
			if (opts != null) {
				return BitmapFactory.decodeFile(f.getAbsolutePath(), opts);
			} else {
				return BitmapFactory.decodeFile(f.getAbsolutePath());
			}
		} catch (Exception exp) {
		}

		return null;
	}
}
