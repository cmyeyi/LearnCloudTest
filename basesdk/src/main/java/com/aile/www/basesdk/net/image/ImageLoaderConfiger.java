package com.aile.www.basesdk.net.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

/**
 * @author RP
 *         <p>
 *         2015年6月1日-下午1:51:35
 */
public class ImageLoaderConfiger {

    public static final int DEFAULT_DOWNLOADER_CONNECT_TIMEOUT = 5 * 1000;
    public static final int DEFAULT_DOWNLOADER_READ_TIMEOUT = 30 * 1000;

    private static final ImageLoaderConfiger instance = new ImageLoaderConfiger();

    private Context mContext;
    private DisplayImageOptions mOptions = null;
    private DisplayImageOptions mBackgroundOptions = null;
    private ImageLoaderConfiguration mConfig = null;
    private BaseImageDownloader mImageDownloader;

    private ImageLoaderConfiger() {
    }

    ;

    public ImageLoaderConfiger init(Context context, BaseImageDownloader imageDownloader) {
        mContext = context.getApplicationContext();
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true).cacheOnDisk(true);// 默认是false,我们设置为true
        builder.displayer(new FadeInBitmapDisplayer(300)).
                considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2); // 设置图片以如何的编码方式显示
        mOptions = builder.build();
        mBackgroundOptions = builder.displayer(new BitmapDisplayer() {

            @Override
            public void display(Bitmap bitmap, ImageAware view, LoadedFrom from) {
                view.getWrappedView().setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        }).build();
//        mImageDownloader = new TencentCloudAccelerateImageDownloader(mContext,DEFAULT_DOWNLOADER_CONNECT_TIMEOUT , DEFAULT_DOWNLOADER_READ_TIMEOUT);
        mImageDownloader = imageDownloader;
        mConfig = new ImageLoaderConfiguration.Builder(mContext).imageDownloader(mImageDownloader)
                .diskCacheSize(100 * 1024 * 1024)
                .memoryCacheSizePercentage(60)
                .defaultDisplayImageOptions(mOptions)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new UsingFreqLimitedMemoryCache(8 * 1024 * 1024)).build();
        return instance;
    }

    public static ImageLoaderConfiger getInstance() {
        return instance;
    }

    public DisplayImageOptions.Builder getDefaultOptionsBuilder() {
        if (mOptions == null) {
            throw new IllegalStateException("call init first!!!!");
        }
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cloneFrom(mOptions);
        return builder;
    }

    public DisplayImageOptions getDefaultOption() {
        if (mOptions == null) {
            throw new IllegalStateException("call init first!!!!");
        }
        return mOptions;
    }

    public ImageLoaderConfiguration getDefaultConfiguration() {
        if (mConfig == null) {
            throw new IllegalStateException("call init first!!!!");
        }
        return mConfig;
    }

    public DisplayImageOptions getDisplayImageOptions(int defaultImgRes) {
        DisplayImageOptions.Builder builder = getDefaultOptionsBuilder();
        builder.showImageForEmptyUri(defaultImgRes).showImageOnFail(defaultImgRes).showImageOnLoading(defaultImgRes);
        return builder.build();
    }

    public DisplayImageOptions getDisplayImageOptions(Drawable defaultDrawable) {
        DisplayImageOptions.Builder builder = getDefaultOptionsBuilder();
        builder.showImageForEmptyUri(defaultDrawable).showImageOnFail(defaultDrawable).showImageOnLoading(defaultDrawable);
        return builder.build();
    }

    /**
     * 加载圆角图片
     *
     * @param defaultImgRes 默认图
     * @param cornerRadius  圆角弧度
     * @return
     */
    public DisplayImageOptions getRoundCornerDisplayImageOptions(int defaultImgRes, int cornerRadius) {
        DisplayImageOptions.Builder builder = getDefaultOptionsBuilder();
        builder.displayer(new RoundedBitmapDisplayer(cornerRadius));
        builder.showImageForEmptyUri(defaultImgRes).showImageOnFail(defaultImgRes).showImageOnLoading(defaultImgRes);
        return builder.build();
    }

}
