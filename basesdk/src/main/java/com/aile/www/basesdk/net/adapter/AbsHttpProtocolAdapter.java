package com.aile.www.basesdk.net.adapter;

import android.text.TextUtils;

import com.aile.www.basesdk.net.filter.BaseResponseFilter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

public abstract class AbsHttpProtocolAdapter<T> {
	private Object bean;
	private Class<T> valueType;
	private String charset = "utf-8";
	private BaseResponseFilter responseFilter;
	public final static int TYPE_KV = 0;
	public final static int TYPE_TEXT = 1;

	public AbsHttpProtocolAdapter(Object bean, Class<T> valueType,
								  BaseResponseFilter responseFilter) {
		this.bean = bean;
		this.valueType = valueType;
		this.responseFilter = responseFilter;
	}

	public void setCharSet(String charset) {
		this.charset = charset;
	}

	public Object getBean() {
		return bean;
	}

	/**
	 * 将bean转化为网络传输的字符串
	 * 
	 * @return
	 */
	public String beanToString() {
		if (bean == null) {
			return null;
		}
		return beanToString(bean);
	}

	public abstract Map beanToMap();

	/**
	 * 获取JsonObject
	 * 
	 * @param json
	 * @return
	 */
	public static JsonObject parseJson(String json) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		JsonParser parser = new JsonParser();
		JsonObject jsonObj = parser.parse(json).getAsJsonObject();
		return jsonObj;
	}

	/**
	 * 将网络获取的字符串转化为bean
	 * 
	 * @param str
	 * @return
	 */
	public T stringToBean(String str) {
		if (responseFilter != null) {
			return stringToBean(responseFilter.filterJsonString(str), valueType);
		}
		return stringToBean(str, valueType);
	}

	public String getCharset() {
		return charset;
	}

	/**
	 * 将bean转化为网络传输的字符串
	 * 
	 * @param bean
	 * @return
	 */
	public abstract String beanToString(Object bean);

	/**
	 * 将网络获取的字符串转化为bean
	 * 
	 * @param str
	 * @param valueType
	 * @return
	 */
	protected abstract T stringToBean(String str, Class<T> valueType);

	public int getPostType() {
		return TYPE_KV;
	}

}
