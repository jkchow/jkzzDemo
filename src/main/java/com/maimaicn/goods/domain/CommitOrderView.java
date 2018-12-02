package com.maimaicn.goods.domain;

import java.util.List;

public class CommitOrderView {
    private Integer skuId;
    private Integer goodsId;
    private String title;
    private String mainImg;
    private Integer deliveryTime;
    private List<String> skuName;
    private Integer num;
    private Double price;//商品价格，成交价
//    private Double marketPrice;//市场价，
    private Double redDeduction;//红包抵扣金额
    private Double sellerRedDeduction;//红包抵扣金额
    private Double subtotal;//小计，单价price*num
    private Double freight;//该商品需要的运费
    private boolean status;//该sku的状态，false为不可用，失效的


    public CommitOrderView(Integer skuId, Integer goodsId, String title, String mainImg, Integer deliveryTime, List<String> skuName, Integer num, Double price, Double redDeduction,Double sellerRedDeduction, Double subtotal, Double freight, boolean status) {
        this.skuId = skuId;
        this.goodsId = goodsId;
        this.title = title;
        this.mainImg = mainImg;
        this.deliveryTime = deliveryTime;
        this.skuName = skuName;
        this.num = num;
        this.price = price;
        this.redDeduction = redDeduction;
        this.subtotal = subtotal;
        this.freight = freight;
        this.status = status;
        this.sellerRedDeduction = sellerRedDeduction;
    }

    public Double getSellerRedDeduction() {
        return sellerRedDeduction;
    }

    public void setSellerRedDeduction(Double sellerRedDeduction) {
        this.sellerRedDeduction = sellerRedDeduction;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public List<String> getSkuName() {
        return skuName;
    }

    public void setSkuName(List<String> skuName) {
        this.skuName = skuName;
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

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public Double getRedDeduction() {
        return redDeduction;
    }

    public void setRedDeduction(Double redDeduction) {
        this.redDeduction = redDeduction;
    }
}
