package com.aile.www.basesdk.transport;

import com.aile.www.basesdk.UnProguardable;

import java.util.Map;

public abstract class BaseParam implements UnProguardable {

    private byte[] httpContent;
    protected String url;

    public String url(){
        return url;
    }

    public BaseParam url(String url){
        this.url = url;
        return this;
    }

    public byte[] httpContent(){
        return httpContent;
    }

    public BaseParam httpContent(byte[] httpContent){
        this.httpContent = httpContent;
        return this;
    }

    /**
     * http 请求方法类型
     * @return
     */
    public abstract HttpType httpType();

    /**
     * @return
     */
    public boolean isGzip() {
        return false;
    }

    public abstract Map<String,String> params();

    public abstract Map<String,String> headers();
}
