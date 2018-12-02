package com.maimaicn.goods.domain;


/**
 * @类描述 
 * @创建人  张进
 * @2017-11-6 15:21:51
 */
public class SalePropForVerity {

	private Integer propertyId;//
	private String propertyName;//销售属性名称
	private String isRemark;//该属性的值是否需要设置备注，yes-可以设置备注，no-不需要设置备注
	private String canCustomValue;//是否可以自定义属性值，'no','yes'
	private String needPropertyPic;//是否需要商品规格属性图片，yes-需要，no-不需要
	private String isRequired;//是否必须填写，yes-必填，no-非必填

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

	public String getIsRemark() {
		return isRemark;
	}

	public void setIsRemark(String isRemark) {
		this.isRemark = isRemark;
	}

	public String getCanCustomValue() {
		return canCustomValue;
	}

	public void setCanCustomValue(String canCustomValue) {
		this.canCustomValue = canCustomValue;
	}

	public String getNeedPropertyPic() {
		return needPropertyPic;
	}

	public void setNeedPropertyPic(String needPropertyPic) {
		this.needPropertyPic = needPropertyPic;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
}