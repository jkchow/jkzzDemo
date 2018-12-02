package com.maimaicn.goods.domain;

public class OrderSkuParams {
    private Integer skuId ;
    private Integer num ;

    public OrderSkuParams(Integer skuId, Integer num) {
        this.skuId = skuId;
        this.num = num;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
