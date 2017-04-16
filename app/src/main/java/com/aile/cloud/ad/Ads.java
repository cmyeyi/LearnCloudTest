package com.aile.cloud.ad;

import android.text.TextUtils;

import com.aile.cloud.net.bean.ADBanner;
import com.aile.cloud.net.bean.Result;
import com.aile.www.basesdk.UnProguardable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2015/8/27.
 */
public class Ads implements UnProguardable {

    public Result result;
    public Body body;

    public List getAds() {
        Gson gson = new Gson();
        String json = gson.toJson(body.getRows());
        return fromJsonList(json, ADBanner.class);
    }

    private <T extends IADBanner> ArrayList fromJsonList(String json, Class<T> cls) {
        if (json == null || TextUtils.isEmpty(json)) {
            return null;
        }
        ArrayList mList = null;
        try {
            mList = new ArrayList();
            Gson gson = new Gson();

            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                IADBanner banner = gson.fromJson(elem, cls);
                mList.add(banner);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;

    }

    @Override
    public String toString() {
        return "result{" + "banners=" + body + '}';
    }

    public static class Body {
        private int total;
        private List<ADBanner> rows;

        public void setRows(List<ADBanner> rows) {
            this.rows = rows;
        }

        public List<ADBanner> getRows() {
            return rows;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
