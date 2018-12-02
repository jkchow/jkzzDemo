package com.maimaicn.goods.domain;

public class SkuFreightParamsPojo {
    //skuId，freightId，wuliuParam,num,price，deliveryTime,frank,priceType,freeConditions
    private Integer skuId;
    private Integer freightId;
    private Integer wuliuParam;
    private Integer num;
    private Double price;
    private Integer deliveryTime;
    private String frank; //是否包邮
    private String priceType; //运费模板计价类型
    private String freeConditions; //是否有条件包邮
    private Double freight;//该订单sku最终运费

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getFreightId() {
        return freightId;
    }

    public void setFreightId(Integer freightId) {
        this.freightId = freightId;
    }

    public Integer getWuliuParam() {
        return wuliuParam;
    }

    public void setWuliuParam(Integer wuliuParam) {
        this.wuliuParam = wuliuParam;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getFrank() {
        return frank;
    }

    public void setFrank(String frank) {
        this.frank = frank;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getFreeConditions() {
        return freeConditions;
    }

    public void setFreeConditions(String freeConditions) {
        this.freeConditions = freeConditions;
    }
}
