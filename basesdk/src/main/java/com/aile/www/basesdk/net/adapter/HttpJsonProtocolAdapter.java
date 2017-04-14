package com.aile.www.basesdk.net.adapter;

import android.text.TextUtils;

import com.aile.www.basesdk.net.filter.BaseResponseFilter;
import com.aile.www.basesdk.utils.BeanStrGetUtils;
import com.google.gson.Gson;

public class HttpJsonProtocolAdapter<T> extends AbsHttpTextProtocolAdapter {
	private static final String TAG = "HttpJsonProtocolAdapter";
	private static final char CHAR_DOT = '.';
	private static final String REPLACE_EXCEPTION = "Expected BEGIN_OBJECT but was BEGIN_ARRAY";
	private Gson gson = new Gson();

	public HttpJsonProtocolAdapter(Object request, Class<T> valueType, BaseResponseFilter filter) {
		super(request, valueType, filter);
	}

	@Override
	public String beanToString(Object request) {
		String str = gson.toJson(request);
		return str;
	}

	private boolean isNeedReplace(String error) {
		if (-1 != error.indexOf(REPLACE_EXCEPTION)) {
			return true;
		}
		return false;
	}

	private String getNeedReplaceFieldName(String s) {

		try {
			int length = s.length();
			for (int i = length - 1; i >= 0; i--) {
				if (CHAR_DOT == (s.charAt(i))) {
					return "\"" + s.substring(i + 1) + "\"";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected T stringToBean(String str, Class valueType) {
		T result = null;

		try {
			result = gson.fromJson(formatJsonStr(str), (Class<T>) valueType);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if (isNeedReplace(e.toString())) {
				result = stringToBeanRelpace(str, valueType, e.toString());
			}

		}
		
		return result;
	}

	private T stringToBeanRelpace(String str, Class valueType, String eStr) {

		String fieldName = getNeedReplaceFieldName(eStr.toString());
		if (TextUtils.isEmpty(fieldName)) {
			return null;
		}
		String jSonStr = str.trim().replaceAll(fieldName + ":\\[\\]", fieldName + ":\\{\\}");
		if(str.trim().equals(jSonStr)){
			return null;
		}
		T result = null;
		try {
			result = gson.fromJson(jSonStr, (Class<T>) valueType);

		} catch (Exception e) {
			e.printStackTrace();
			
			if (isNeedReplace(e.toString())) {
				result = stringToBeanRelpace(jSonStr, valueType, e.toString());
			}
		}

		return result;
	}

	private String formatJsonStr(String jsonStr) {
		// String revJson = jsonStr.replace("\\", "");
		// todo 这里以后必须改 坑爹的 腾讯json格式
		return BeanStrGetUtils.getJsonString(jsonStr);

		// String revJson = jsonStr;
		// return revJson.substring(revJson.indexOf("{"),
		// revJson.lastIndexOf("}") + 1);
	}

}
