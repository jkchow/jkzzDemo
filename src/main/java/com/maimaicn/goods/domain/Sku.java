package com.maimaicn.goods.domain;


public class Sku {
  public static final String STATUS_NORMAL = "normal";
  public static final String STATUS_DELETE = "delete";

  private Integer skuId;
  private Integer goodsId;
  private String skuProperties;
  private Double price;
  private Double shareRebate;
  private Double redDeduction;
  private Double sellerRedDeduction;
  private Integer stock;
  private String outerId;
  private String barcode;
  private boolean status;

  public Sku(){}
  public Sku(String skuProperties, double price, Integer stock, String outerId, String barcode,double shareRebate,double redDeduction,double sellerRedDeduction) {
    this.skuProperties = skuProperties;
    this.price = price;
    this.stock = stock;
    this.outerId = outerId;
    this.barcode = barcode;
    this.shareRebate = shareRebate;
    this.redDeduction = redDeduction;
    this.sellerRedDeduction = sellerRedDeduction;
  }

  public Double getRedDeduction() {
    return redDeduction;
  }

  public void setRedDeduction(Double redDeduction) {
    this.redDeduction = redDeduction;
  }

  public Double getSellerRedDeduction() {
    return sellerRedDeduction;
  }

  public void setSellerRedDeduction(Double sellerRedDeduction) {
    this.sellerRedDeduction = sellerRedDeduction;
  }

  public Double getShareRebate() {
    return shareRebate;
  }

  public void setShareRebate(Double shareRebate) {
    this.shareRebate = shareRebate;
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

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public String getOuterId() {
    return outerId;
  }

  public void setOuterId(String outerId) {
    this.outerId = outerId;
  }

  public String getBarcode() {
    return barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }
}
