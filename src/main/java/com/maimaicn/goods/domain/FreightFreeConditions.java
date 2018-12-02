package com.maimaicn.goods.domain;


public class FreightFreeConditions {

  public static final String PRICETYPEVALUE_PRICETYPE = "priceType";//'priceType','amount','mix'
  public static final String PRICETYPEVALUE_AMOUNT = "amount";//'priceType','amount','mix'
  public static final String PRICETYPEVALUE_MIX = "mix";//'priceType','amount','mix'
  public static final String ISDEFAULT_YES = "yes";//是否是默认
  public static final String ISDEFAULT_NO = "no";//是否是默认

  private Integer conditionsId;
  private Integer templateId;
  private String conditionsType;
  private String locationIds;
  private String isDefault;
  private Integer priceTypeValue;
  private Double amount;


  public String getLocationIds() {
    return locationIds;
  }

  public void setLocationIds(String locationIds) {
    this.locationIds = locationIds;
  }

  public Integer getConditionsId() {
    return conditionsId;
  }

  public void setConditionsId(Integer conditionsId) {
    this.conditionsId = conditionsId;
  }

  public Integer getTemplateId() {
    return templateId;
  }

  public void setTemplateId(Integer templateId) {
    this.templateId = templateId;
  }

  public String getConditionsType() {
    return conditionsType;
  }

  public void setConditionsType(String conditionsType) {
    this.conditionsType = conditionsType;
  }

  public Integer getPriceTypeValue() {
    return priceTypeValue;
  }

  public void setPriceTypeValue(Integer priceTypeValue) {
    this.priceTypeValue = priceTypeValue;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(String isDefault) {
    this.isDefault = isDefault;
  }
}
