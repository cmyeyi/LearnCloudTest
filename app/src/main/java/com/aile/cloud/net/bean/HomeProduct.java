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
 */

public class HomeProduct implements Serializable, UnProguardable {
    public HomeProductResult result;

    public String getCode() {
        return result.getCode();
    }

    public List getProduct() {
        Gson gson = new Gson();
        String json = gson.toJson(result.getData());
        return fromJsonList(json, Product.class);
    }

    private <T extends Product> ArrayList fromJsonList(String json, Class<T> cls) {
        if (json == null || TextUtils.isEmpty(json)) {
            return null;
        }
        ArrayList mList = null;
        try {
            mList = new ArrayList();
            Gson gson = new Gson();

            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                Product product = gson.fromJson(elem, cls);
                mList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mList;
    }

    public class HomeProductResult {
        public String code;
        public List<Product> data;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setData(List<Product> data) {
            this.data = data;
        }

        public List<Product> getData() {
            return data;
        }
    }

    public static class Product implements Serializable, UnProguardable {
        /**
         * icon_url": "http://img.emotao.com/20170324/1de0m9jsigi08q39t1c7ilb8v9.png",
         * "outPrice": 1.0000,
         * "total": 9988,
         * "batch_number": "170324002624",
         * "sellTotal": 1908,
         * "name": "[预售] iPhone 7 Plus 256G 红色特别版",
         * "model": 1,
         * "id": 29
         */
        private String icon_url;
        private String outPrice;
        private int total;
        private String batch_number;
        private int sellTotal;
        private String name;
        private int model;
        private String id;

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

        public void setBatch_number(String batch_number) {
            this.batch_number = batch_number;
        }

        public String getBatch_number() {
            return batch_number;
        }

        public void setOutPrice(String outPrice) {
            this.outPrice = outPrice;
        }

        public String getOutPrice() {
            return outPrice;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }

        public String getIcon_url() {
            return icon_url;
        }

        public void setSellTotal(int sellTotal) {
            this.sellTotal = sellTotal;
        }

        public int getSellTotal() {
            return sellTotal;
        }

        public void setModel(int model) {
            this.model = model;
        }

        public int getModel() {
            return model;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }


}
