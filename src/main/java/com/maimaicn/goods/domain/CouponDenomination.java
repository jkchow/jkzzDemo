package com.maimaicn.goods.domain;


public class CouponDenomination {
  public static final String STATUS_WAIT = "wait";
  public static final String STATUS_GETTING = "getting";
  public static final String STATUS_END = "end";

  private Integer cardId;
  private Integer couponId;
  private Double amount;
  private Double threshold;
  private Integer circulation;
  private Integer haveGet;
  private Integer limitGet;
  private String status;

  public CouponDenomination(Double amount, Double threshold, Integer circulation, Integer limitGet, String status) {
    this.amount = amount;
    this.threshold = threshold;
    this.circulation = circulation;
    this.limitGet = limitGet;
    this.status = status;
  }

  public Integer getCardId() {
    return cardId;
  }

  public void setCardId(Integer cardId) {
    this.cardId = cardId;
  }

  public Integer getCouponId() {
    return couponId;
  }

  public void setCouponId(Integer couponId) {
    this.couponId = couponId;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Double getThreshold() {
    return threshold;
  }

  public void setThreshold(Double threshold) {
    this.threshold = threshold;
  }

  public Integer getCirculation() {
    return circulation;
  }

  public void setCirculation(Integer circulation) {
    this.circulation = circulation;
  }

  public Integer getHaveGet() {
    return haveGet;
  }

  public void setHaveGet(Integer haveGet) {
    this.haveGet = haveGet;
  }

  public Integer getLimitGet() {
    return limitGet;
  }

  public void setLimitGet(Integer limitGet) {
    this.limitGet = limitGet;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
