package com.maimaicn.goods.domain;

/**
 * 运费模板详情
 */
public class FreightDetail {
  public static final String ISDEFAULT_YES = "yes";//是否是默认运费
  public static final String ISDEFAULT_NO = "no";//是否是默认运费

  private Integer detailId;
  private Integer templateId;
  private String isDefault;
  private String locationIds;
  private Double first;
  private Double firstPrice;
  private Double subsequent;
  private Double subsequentPrice;


  public Integer getDetailId() {
    return detailId;
  }

  public void setDetailId(Integer detailId) {
    this.detailId = detailId;
  }

  public Integer getTemplateId() {
    return templateId;
  }

  public void setTemplateId(Integer templateId) {
    this.templateId = templateId;
  }

  public String getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(String isDefault) {
    this.isDefault = isDefault;
  }


  public String getLocationIds() {
    return locationIds;
  }

  public void setLocationIds(String locationIds) {
    this.locationIds = locationIds;
  }


  public Double getFirst() {
    return first;
  }

  public void setFirst(Double first) {
    this.first = first;
  }


  public Double getFirstPrice() {
    return firstPrice;
  }

  public void setFirstPrice(Double firstPrice) {
    this.firstPrice = firstPrice;
  }


  public Double getSubsequent() {
    return subsequent;
  }

  public void setSubsequent(Double subsequent) {
    this.subsequent = subsequent;
  }


  public Double getSubsequentPrice() {
    return subsequentPrice;
  }

  public void setSubsequentPrice(Double subsequentPrice) {
    this.subsequentPrice = subsequentPrice;
  }

}
