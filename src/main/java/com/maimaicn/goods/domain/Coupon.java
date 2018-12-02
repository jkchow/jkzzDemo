package com.maimaicn.goods.domain;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Coupon {

  private Integer couponId;
  private String name;
  private String type;
  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private Date startTime;
  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private Date endTime;
  private Date createTime;
  private Long memberId;

  public Integer getCouponId() {
    return couponId;
  }

  public void setCouponId(Integer couponId) {
    this.couponId = couponId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }
}
