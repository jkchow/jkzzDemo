package com.maimaicn.goods.domain;


import java.util.Date;

public class Goods{
  public static final String STATUS_UP = "up";
  public static final String STATUS_DOWN = "down";
  public static final String STATUS_DELETE = "delete";

  private Integer goodsId;
  private Integer brandId;
  private Integer categoryId;
  private Long memberId;
  private String title;
  private String mainImg;
  private String whiteImg;
  private String albumImgs;
  private String promiseServiceIds;
  private Integer freightId;
  private Integer wuliuParam;
  private Integer subStockType;
  private Integer startType;
  private Date startTime;
  private Date uploadTime;
  private String status;


  public Date getUploadTime() {
    return uploadTime;
  }

  public void setUploadTime(Date uploadTime) {
    this.uploadTime = uploadTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getBrandId() {
    return brandId;
  }

  public void setBrandId(Integer brandId) {
    this.brandId = brandId;
  }

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public String getPromiseServiceIds() {
    return promiseServiceIds;
  }

  public void setPromiseServiceIds(String promiseServiceIds) {
    this.promiseServiceIds = promiseServiceIds;
  }

  public Integer getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(Integer goodsId) {
    this.goodsId = goodsId;
  }

  public Integer getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
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

  public String getWhiteImg() {
    return whiteImg;
  }

  public void setWhiteImg(String whiteImg) {
    this.whiteImg = whiteImg;
  }

  public String getAlbumImgs() {
    return albumImgs;
  }

  public void setAlbumImgs(String albumImgs) {
    this.albumImgs = albumImgs;
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

  public Integer getSubStockType() {
    return subStockType;
  }

  public void setSubStockType(Integer subStockType) {
    this.subStockType = subStockType;
  }

  public Integer getStartType() {
    return startType;
  }

  public void setStartType(Integer startType) {
    this.startType = startType;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }
}
