/**
 * Baidu.com Inc.
 * Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.aile.www.basesdk.net.adapter;

import com.aile.www.basesdk.net.filter.BaseResponseFilter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 网络数据适配器，将程序中的bean转化为和server定好的接口形式。该接口形式是以文本形式定义的
 * 
 */
public abstract class AbsHttpTextProtocolAdapter<T> extends
		AbsHttpProtocolAdapter<T> {

	public AbsHttpTextProtocolAdapter(Object bean, Class<T> valueType,
									  BaseResponseFilter responseFilter) {
		super(bean, valueType, responseFilter);

	}

	public Map beanToMap() {
		if (getBean() == null) {
			return null;
		}

		return toMap(new Gson().toJsonTree(getBean()).getAsJsonObject());
	}

	/**
	 * 将JSONObjec对象转换成Map-List集合
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, Object> toMap(JsonObject json) {
		if (json == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Set<Entry<String, JsonElement>> entrySet = json.entrySet();
		if(entrySet != null) {
			for (Iterator<Entry<String, JsonElement>> iter = entrySet.iterator(); iter
					.hasNext();) {
				Entry<String, JsonElement> entry = iter.next();
				String key = entry.getKey();
				JsonElement value = entry.getValue();
				if (value instanceof JsonArray) {
					map.put((String) key, toList((JsonArray) value));
				} else if (value instanceof JsonObject) {
					map.put((String) key, toMap((JsonObject) value));
				} else {
					map.put((String) key, value.getAsString());
				}
			}
		}
		return map;
	}

	/**
	 * 将JSONArray对象转换成List集合
	 * 
	 * @param json
	 * @return
	 */
	public static List<Object> toList(JsonArray json) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < json.size(); i++) {
			Object value = json.get(i);
			if (value instanceof JsonArray) {
				list.add(toList((JsonArray) value));
			} else if (value instanceof JsonObject) {
				list.add(toMap((JsonObject) value));
			} else {
				list.add(value);
			}
		}
		return list;
	}

}
