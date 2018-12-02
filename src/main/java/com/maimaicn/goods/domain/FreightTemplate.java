package com.maimaicn.goods.domain;


public class FreightTemplate {
    public static final String FRANK_YES = "yes";//是否包邮
    public static final String FRANK_NO = "no";//是否包邮
    public static final String PRICETYPE_PIECE = "piece";//计价类型
    public static final String PRICETYPE_WEIGHT = "weight";//计价类型 重量
    public static final String PRICETYPE_BULK = "bulk";//计价类型,体积
    public static final String FREECONDITIONS_YES = "yes";//是否设置条件免邮
    public static final String FREECONDITIONS_NO = "no";//是否设置条件免邮

  private Integer templateId;
  private String templateName;
  private Integer locationId;
  private Integer deliveryTime;
  private String frank;
  private String priceType;
  private String freeConditions;
  private Long memberId;


  public Integer getTemplateId() {
    return templateId;
  }

  public void setTemplateId(Integer templateId) {
    this.templateId = templateId;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public Integer getLocationId() {
    return locationId;
  }

  public void setLocationId(Integer locationId) {
    this.locationId = locationId;
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

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }
}
