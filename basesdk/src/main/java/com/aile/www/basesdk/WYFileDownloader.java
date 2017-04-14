package com.aile.www.basesdk;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 文件下载器 带缓存功能
 * 封装了一下xutil  并且带上一个文件缓存机制
 * <p/>
 */
public class WYFileDownloader {
    private static WYFileDownloader mInstance;
    private HttpUtils mHttpUtils;
    private String mDiskCacheFilePath;
    private Map<String, String> mMemeryCache = new ArrayMap<String, String>();
    private static Md5FileNameGenerator mMd5;

    private WYFileDownloader() {
        // 默认线程池3个  超时设置15秒
        mHttpUtils = new HttpUtils();
    }

    public void init(Context context) {
        mDiskCacheFilePath = StorageUtils.getCacheDirectory(context).getPath() + "/";
    }

    public synchronized static WYFileDownloader getInstance() {
        if (mInstance == null) {
            mInstance = new WYFileDownloader();
            mMd5 = new Md5FileNameGenerator();
        }
        return mInstance;
    }


    public static interface FileLoadingListener {
        public void onSuccess(String path);

        public void onFailure(String s);

        public void onStart();

        public void onCancelled();

        public void onLoading(long total, long current);
    }

    private static String generateKey(String url) {
        String path = URLEncoder.encode(url);
        String suffix = path.substring(path.lastIndexOf("."));
        return mMd5.generate(path) + suffix;
    }

    private void checkInit() {
        if (TextUtils.isEmpty(mDiskCacheFilePath)) {
            throw new Error("WYFileDownloader is not init");
        }

    }

    public String getDiskCacheDir(){
        checkInit();
        return  mDiskCacheFilePath;
    }

    public String getFilePathFromCache(String url) {
        checkInit();
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        final String key = generateKey(url);
        if (mMemeryCache.containsKey(key)) {
            return mMemeryCache.get(key);
        }
        String path = mDiskCacheFilePath + key;
        if (new File(path).exists()) {
            mMemeryCache.put(key, path);
            return path;
        }
        return "";
    }


    public void load(String url, final FileLoadingListener listener) {
        checkInit();
        if (TextUtils.isEmpty(url)) {
            if (listener != null) {
                listener.onFailure("url empty");
            }
            return;
        }

        final String key = generateKey(url);

        // 内存中已经存在
        if (mMemeryCache.containsKey(key)) {
            if (listener != null) {
                listener.onSuccess(mMemeryCache.get(key));
            }
            return;
        }

        // sd卡中存在
        String path = mDiskCacheFilePath + key;
        if (new File(path).exists()) {
            mMemeryCache.put(key, path);
            if (listener != null) {
                listener.onSuccess(path);
            }
            return;
        }

        //网络下载
        mHttpUtils.download(url, path, false, false, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                mMemeryCache.put(key, responseInfo.result.getPath());
                if (listener != null) {
                    listener.onSuccess(responseInfo.result.getPath());
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            public void onCancelled() {
                super.onCancelled();
                if (listener != null) {
                    listener.onCancelled();
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                if (listener != null) {
                    listener.onLoading(total, current);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (listener != null) {
                    listener.onFailure(s);
                }
            }
        });


    }


}
