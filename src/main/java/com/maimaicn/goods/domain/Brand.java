package com.maimaicn.goods.domain;


/**
 * @类描述 
 * @创建人  张进
 * @2017-9-26 16:58:04
 */
public class Brand {
	private Integer brandId;//品牌id
	private String chName;//品牌名称
	private String enName;//品牌英文名称
	private String logo;//品牌LOGO
	private String intro;//品牌简介

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public Integer getBrandId(){
		return brandId;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
}