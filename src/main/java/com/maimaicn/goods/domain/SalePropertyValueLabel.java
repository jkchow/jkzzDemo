package com.maimaicn.goods.domain;


/**
 * @类描述 
 * @创建人  张进
 * @2017-11-6 11:43:17
 */
public class SalePropertyValueLabel{
	private Integer labelId;//销售属性标签id
	private String labelName;//销售属性标签名称
	private String alias;//标签别名，例如，属性类型为颜色时，该值存的就是颜色值
	private Integer propertyId;//该标签所属的属性id
	private Integer sortValue;//排序值，越小越靠前

	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}
	public Integer getLabelId(){
		return labelId;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getLabelName(){
		return labelName;
	}
	public Integer getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getSortValue() {
		return sortValue;
	}

	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}
}