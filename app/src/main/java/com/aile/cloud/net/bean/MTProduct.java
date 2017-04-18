package com.aile.cloud.net.bean;

import com.aile.www.basesdk.UnProguardable;

import java.io.Serializable;

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
public class MTProduct implements Serializable, UnProguardable {

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
