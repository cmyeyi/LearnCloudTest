package com.aile.cloud.net.bean;

import android.text.TextUtils;

import com.aile.www.basesdk.UnProguardable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeyi on 17/4/17.
 * 人气
 */

public class ProductPopularity implements Serializable, UnProguardable {
    public HomeProductResult result;

    public class HomeProductResult {
        public String code;
        public List<MTProduct> data;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setData(List<MTProduct> data) {
            this.data = data;
        }

        public List<MTProduct> getData() {
            Gson gson = new Gson();
            String json = gson.toJson(data);
            return fromJsonList(json, MTProduct.class);
        }

        private <T extends MTProduct> ArrayList fromJsonList(String json, Class<T> cls) {
            if (json == null || TextUtils.isEmpty(json)) {
                return null;
            }
            ArrayList mList = null;
            try {
                mList = new ArrayList();
                Gson gson = new Gson();

                JsonArray array = new JsonParser().parse(json).getAsJsonArray();
                for (final JsonElement elem : array) {
                    MTProduct product = gson.fromJson(elem, cls);
                    mList.add(product);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mList;
        }
    }

}
