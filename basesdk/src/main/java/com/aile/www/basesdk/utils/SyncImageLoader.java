package com.aile.www.basesdk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;

/**
 * @author zijianlu
 * @version 创建时间：2012-5-10
 * @Description 封装了adapterListView 中的图片异步下载和缓存, 适用于ListView，GridView等控件中的图片显示
 */
public class SyncImageLoader implements OnScrollListener {
	private static final String TAG = "elife.SyncImageLoader";
	private Object mLock = new Object();

	private volatile boolean isAllowLoad = true;
	private volatile boolean isFirstLoad = true;
	private volatile boolean isSetImgBackGround = false;
	private volatile int mBeginPos = 0;
	private volatile int mEndPos = 0;
	BitmapFactory.Options mDefaultOptions = new BitmapFactory.Options();

	// private ImageCache mImageCache;
	private AbsListView mAbsListView = null;

	/** 构造函数 */
	public SyncImageLoader(AbsListView absListView) {
		mAbsListView = absListView;
		if (mAbsListView == null) {
			throw new RuntimeException(
					"AbsListView is null in SyncImageLoader(AbsListView absListView)");
		}

		init();
	}

	/** 构造函数 */
	public SyncImageLoader() {
		init();
	}

	/**
	 * 保存文件~~~这里线程安全
	 * @param url
	 * @param bitmap
	 * @return
	 */
	public synchronized boolean saveBitmapToDisk(String url , Bitmap bitmap){
		if(TextUtils.isEmpty(url)|| bitmap == null){
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
			Log.e(TAG, "getBitmapFromDiskCache:" + exp.toString());
		} catch (OutOfMemoryError err) {
			Log.e(TAG, "getBitmapFromDiskCache:" + err.toString());
		}

		return null;
	}

	private void init() {
		mDefaultOptions.inJustDecodeBounds = false;
		mDefaultOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		mDefaultOptions.inPurgeable = true;
		mDefaultOptions.inInputShareable = true;
		// mImageCache = ImageCache.getInstance();
	}

	public synchronized void setLoadLimit(int beginPos, int endPos) {
		if (beginPos > endPos) {
			return;
		}
		mBeginPos = beginPos - 2;
		mEndPos = endPos + 2;
		SDKLogger.d(TAG, "setLoadLimit:" + beginPos + " " + endPos);
	}

	/** 将图片设为imageView的背景还是src */
	public void setImageMode(boolean isSetBackground) {
		isSetImgBackGround = isSetBackground;
	}

	public synchronized void lock() {
		isAllowLoad = false;
		isFirstLoad = false;
	}

	public void unlock() {
		isAllowLoad = true;
		synchronized (mLock) {
			mLock.notifyAll();
		}
	}

	/** 图片文件缓存的有效期，单位毫秒 */
	public void setBitmapCacheTime(long cacheTimeMills) {
		if (cacheTimeMills < 0) {
			cacheTimeMills = 0;
		}
	}

	/** 重置 */
	public void reset() {
		synchronized (mLock) {
			mLock.notifyAll();
		}

		synchronized (this) {
			isAllowLoad = true;
			isFirstLoad = true;
			mBeginPos = 0;
			mEndPos = 0;
		}

		System.gc();
	}

	/** 清空缓存图片 */
	public void clear() {
		synchronized (this) {
			isAllowLoad = false;
			mBeginPos = -1;
			mEndPos = -1;
		}

		synchronized (mLock) {
			mLock.notifyAll();
		}

		mAbsListView = null;
		System.gc();
	}

	/** 加载图片 */
	public void loadImage(final String imageUrl, final ImageView view,
						  final int position) {
		ImageLoader.getInstance().displayImage(imageUrl, view);
	}

	private boolean mShowAnim = true;

	public void setShowAnim(boolean anim) {
		mShowAnim = anim;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
			lock();
			break;
		case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
			if (mAbsListView != null) {
				int start = mAbsListView.getFirstVisiblePosition();
				int end = mAbsListView.getLastVisiblePosition();
				setLoadLimit(start, end);
			}
			unlock();
			break;
		case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			unlock();
			break;

		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {

	}

	private ProcessBitmapListener mProcessLisener;

	/** 设置对图片进行处理的回调函数 */
	public void setProcessBitmapListener(ProcessBitmapListener processLisener) {
		mProcessLisener = processLisener;
	}

	public static interface ProcessBitmapListener {
		Bitmap processBitmap(Bitmap bmp, String url);
	}

}
