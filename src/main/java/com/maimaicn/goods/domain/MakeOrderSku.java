package com.maimaicn.goods.domain;

import java.util.List;

/**
 * 下单和订单确认页需要的sku信息实体封装
 */
public class MakeOrderSku {
    private Integer skuId;
    private Integer num;
    private Integer goodsId;
    private Long shopMId;
    private Integer freightId;
    private Double freight;//这个sku的最终运费
    private Double wuliuParam;
    private Integer deliveryTime;
    private String frank;//是否包邮,yes,no
    private String priceType;//计价方式，piece:按件，weight-按重量，bulk-按体积
    private String freeConditions;//是否设置了免邮条件,yes,no
    private String title;
    private String mainImg;
    private String status;
    private String goodsStatus;
    private String skuProperties;
    private List<String> skuName;
    private Double price;
    private Integer stock;
    private Double subtotal;

    private Double sellerRedDeduction;//商家红包抵扣金额
    private Double redDeduction;//平台红包抵扣，
    private Double shareRebate;//分享返利
    private Double b2bPrice;


    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getSellerRedDeduction() {
        return sellerRedDeduction;
    }

    public void setSellerRedDeduction(Double sellerRedDeduction) {
        this.sellerRedDeduction = sellerRedDeduction;
    }

    public Double getRedDeduction() {
        return redDeduction;
    }

    public void setRedDeduction(Double redDeduction) {
        this.redDeduction = redDeduction;
    }

    public Double getShareRebate() {
        return shareRebate;
    }

    public void setShareRebate(Double shareRebate) {
        this.shareRebate = shareRebate;
    }


    public Double getB2bPrice() {
        return b2bPrice;
    }

    public void setB2bPrice(Double b2bPrice) {
        this.b2bPrice = b2bPrice;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public List<String> getSkuName() {
        return skuName;
    }

    public void setSkuName(List<String> skuName) {
        this.skuName = skuName;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Long getShopMId() {
        return shopMId;
    }

    public void setShopMId(Long shopMId) {
        this.shopMId = shopMId;
    }

    public Integer getFreightId() {
        return freightId;
    }

    public void setFreightId(Integer freightId) {
        this.freightId = freightId;
    }

    public Double getWuliuParam() {
        return wuliuParam;
    }

    public void setWuliuParam(Double wuliuParam) {
        this.wuliuParam = wuliuParam;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getSkuProperties() {
        return skuProperties;
    }

    public void setSkuProperties(String skuProperties) {
        this.skuProperties = skuProperties;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
