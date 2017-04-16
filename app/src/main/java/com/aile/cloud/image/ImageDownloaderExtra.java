package com.aile.cloud.image;

/**
 * 在调用Imageloader加载图片时，向下载器提供下载时的参数，如超时时间
 * Created by RP on 2015/8/27.
 */
public class ImageDownloaderExtra {

    private int connectTimeout = ImageLoaderConfiger.DEFAULT_DOWNLOADER_CONNECT_TIMEOUT;
    private int readTimeout = ImageLoaderConfiger.DEFAULT_DOWNLOADER_READ_TIMEOUT;

    public ImageDownloaderExtra(int connectTimeout, int readTimeout) {
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    @Override
    public String toString() {
        return "ImageDownloaderExtra{" +
                "connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                '}';
    }
}
