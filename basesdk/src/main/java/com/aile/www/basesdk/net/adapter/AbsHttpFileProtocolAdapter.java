package com.aile.www.basesdk.net.adapter;

import android.support.v4.util.ArrayMap;

import com.aile.www.basesdk.net.file.UploadFile;
import com.aile.www.basesdk.net.filter.BaseResponseFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class AbsHttpFileProtocolAdapter<T> extends
		AbsHttpProtocolAdapter<T> {

	private ArrayMap<String, String> uploadFileMap = new ArrayMap<String, String>();

	public AbsHttpFileProtocolAdapter(Object bean, Class<T> valueType,
									  BaseResponseFilter responseFilter) {
		super(bean, valueType, responseFilter);
		initUploadFileMap(bean);
	}

	private void initUploadFileMap(Object bean) {

		Field[] fields = bean.getClass().getDeclaredFields();

		for (Field field : fields) {
			String name = field.getName();
			String type = field.getGenericType().toString();
			if (!"com.aile.www.basesdk.net.file.UploadFile".equals(type)) {
				continue;
			}
			field.setAccessible(true);
			try {
				UploadFile uploadFile = (UploadFile) field.get(bean);
				uploadFileMap.put(name, uploadFile.getFilePath());
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	public Map beanToMap() {
		if (getBean() == null) {
			return null;
		}
		return toMap(parseJson(beanToString()), uploadFileMap);
	}

	/**
	 * 将JSONObjec对象转换成Map-List集合
	 * 
	 * @param json
	 * @return
	 */
	public static Map<String, Object> toMap(JsonObject json,
											ArrayMap<String, String> uploadFileMap) {
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
					map.put((String) key, toList((JsonArray) value, uploadFileMap));
				} else if (value instanceof JsonObject) {
					if (uploadFileMap.containsKey(key)) {
						String filePath = uploadFileMap.get(key);
						map.put((String) key, new File(filePath));
					} else {
						map.put((String) key, toMap((JsonObject) value, uploadFileMap));
					}
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
	public static List<Object> toList(JsonArray json,
									  ArrayMap<String, String> uploadFileMap) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < json.size(); i++) {
			Object value = json.get(i);
			if (value instanceof JsonArray) {
				list.add(toList((JsonArray) value, uploadFileMap));
			} else if (value instanceof JsonObject) {
				list.add(toMap((JsonObject) value, uploadFileMap));
			} else {
				list.add(value);
			}
		}
		return list;
	}

}
