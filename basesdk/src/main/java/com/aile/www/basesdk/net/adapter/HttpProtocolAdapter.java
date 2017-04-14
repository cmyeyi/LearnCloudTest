package com.aile.www.basesdk.net.adapter;

import com.aile.www.basesdk.net.filter.BaseResponseFilter;
import com.google.gson.Gson;

public class HttpProtocolAdapter<T> extends AbsHttpTextProtocolAdapter {

    private Gson gson = new Gson();

    public HttpProtocolAdapter(Object bean, Class<T> valueType,
                               BaseResponseFilter responseFilter) {
        super(bean, valueType, responseFilter);

    }

    @Override
    public String beanToString(Object bean) {
        String str = gson.toJson(bean);
        return str;
    }

    @Override
    protected T stringToBean(String str, Class valueType) {
        T result = null;

        try {
            result = gson.fromJson((str), (Class<T>) valueType);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int getPostType() {
        return TYPE_TEXT;
    }
}
