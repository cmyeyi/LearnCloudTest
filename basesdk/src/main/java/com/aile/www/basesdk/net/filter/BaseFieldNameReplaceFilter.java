package com.aile.www.basesdk.net.filter;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BaseFieldNameReplaceFilter extends BaseResponseFilter {
	private transient Gson gson = new Gson();
	private transient List<ReplaceValue> list = null;

	@Override
	public String filterJsonString(String str) {
		initData();
		String json = str.trim();
		for (ReplaceValue replaceValue : list) {
			json = json.replaceAll(replaceValue.target + ":",
					replaceValue.replacement + ":");
			json = json.replaceAll("\"" + replaceValue.target + "\":",
					"\"" +replaceValue.replacement + "\":");
		}
		return json;
	}

	private void initData() {
		if (list == null) {
			list = new ArrayList<ReplaceValue>();
			try {
				String json = gson.toJson(this);
				json = json.substring(1, json.length() - 1);
				if (TextUtils.isEmpty(json)) {
					return;
				}

				String[] kvs = json.split(",");
				for (String kv : kvs) {
					String[] ss = kv.split(":");
					list.add(new ReplaceValue(ss[1], ss[0]));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public static class ReplaceValue {
		public ReplaceValue(String target, String replacement) {
			this.target = target;
			this.replacement = replacement;
		}

		public String target;
		public String replacement;
	}
}
