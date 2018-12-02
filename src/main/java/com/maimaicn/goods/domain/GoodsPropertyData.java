package com.maimaicn.goods.domain;

/**
 * 商品属性最终数据保存表
 */
public class GoodsPropertyData {

  private Integer propertyId;
  private String propertyName;
  private String valueName;
  private String valueJson;
  private Integer goodsId;

  public GoodsPropertyData(Integer propertyId, String propertyName, String valueName, String valueJson) {
    this.propertyId = propertyId;
    this.propertyName = propertyName;
    this.valueName = valueName;
    this.valueJson = valueJson;
  }

  public Integer getPropertyId() {
    return propertyId;
  }

  public void setPropertyId(Integer propertyId) {
    this.propertyId = propertyId;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public String getValueName() {
    return valueName;
  }

  public void setValueName(String valueName) {
    this.valueName = valueName;
  }

  public String getValueJson() {
    return valueJson;
  }

  public void setValueJson(String valueJson) {
    this.valueJson = valueJson;
  }

  public Integer getGoodsId() {
    return goodsId;
  }

  public void setGoodsId(Integer goodsId) {
    this.goodsId = goodsId;
  }
}
