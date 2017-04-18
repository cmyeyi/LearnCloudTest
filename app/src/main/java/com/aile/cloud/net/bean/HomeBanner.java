package com.aile.cloud.net.bean;

import android.text.TextUtils;

import com.aile.cloud.ad.IADBanner;
import com.aile.www.basesdk.UnProguardable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2015/8/27.
 */
public class HomeBanner implements UnProguardable {

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

    public static class Result implements Serializable, UnProguardable {
        private String msg;
        private String code;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
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

    public static class ADBanner implements IADBanner, Serializable, UnProguardable {
        private String imgUrl;
        private String description;
        private String rank;
        private String usrId;
        private String id;
        private String bigImgUrl;
        private String samllImgUrl;
        private String url;
        private int status;

        @Override
        public String getImgUrl() {
            return this.imgUrl;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public String getRank() {
            return this.rank;
        }

        @Override
        public String getUsrId() {
            return this.usrId;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getBigImgUrl() {
            return this.bigImgUrl;
        }

        @Override
        public String getSamllImgUrl() {
            return this.samllImgUrl;
        }

        @Override
        public String getUrl() {
            return this.url;
        }

        @Override
        public int getStatus() {
            return this.status;
        }
    }

}
