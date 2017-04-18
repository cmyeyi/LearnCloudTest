package com.aile.cloud.net.bean;

import java.util.List;

/**
 * Created by yeyi on 17/4/18.
 */

public class PageProducts {
    private List<MTProduct> productPopularity;
    private List<MTProduct> productSchedule;
    private List<MTProduct> productLatest;
    private List<MTProduct> productTotal;

    public void setProductPopularity(List<MTProduct> productPopularity) {
        this.productPopularity = productPopularity;
    }

    public List<MTProduct> getProductPopularity() {
        return productPopularity;
    }

    public void setProductLatest(List<MTProduct> productLatest) {
        this.productLatest = productLatest;
    }

    public List<MTProduct> getProductLatest() {
        return productLatest;
    }

    public void setProductSchedule(List<MTProduct> productSchedule) {
        this.productSchedule = productSchedule;
    }

    public List<MTProduct> getProductSchedule() {
        return productSchedule;
    }

    public void setProductTotal(List<MTProduct> productTotal) {
        this.productTotal = productTotal;
    }

    public List<MTProduct> getProductTotal() {
        return productTotal;
    }
}
