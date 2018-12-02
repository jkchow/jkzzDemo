package com.maimaicn.goods.domain;

/**
 * 商品服务承诺
 */
public class PromiseService {

  private Long promiseId;
  private String serviceName;
  private String serviceIcon;
  private String description;
  private String detailUrl;
  private String required;
  private String needSubscibe;
  private String scope;//该服务适用的范围,appoint：该服务承诺只适用指定分类，all：该服务适用于所有分类（该情况下指定分类id就无效了）


  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public Long getPromiseId() {
    return promiseId;
  }

  public void setPromiseId(Long promiseId) {
    this.promiseId = promiseId;
  }


  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }


  public String getServiceIcon() {
    return serviceIcon;
  }

  public void setServiceIcon(String serviceIcon) {
    this.serviceIcon = serviceIcon;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getDetailUrl() {
    return detailUrl;
  }

  public void setDetailUrl(String detailUrl) {
    this.detailUrl = detailUrl;
  }


  public String getRequired() {
    return required;
  }

  public void setRequired(String required) {
    this.required = required;
  }


  public String getNeedSubscibe() {
    return needSubscibe;
  }

  public void setNeedSubscibe(String needSubscibe) {
    this.needSubscibe = needSubscibe;
  }

}
