package com.maimaicn.goods.domain;


/**
 * @类描述 
 * @创建人  张进
 * @2017-10-25 15:35:54
 */
public class GoodsPropertyValue{
	private Integer valueId;//商品分类普通属性值Id
	private String valueName;//属性值名称
	private Integer sortValue;//排序值，越小越靠前
	private Integer propertyId;//该值所属属性的id

	public Integer getValueId() {
		return valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	public String getValueName(){
		return valueName;
	}

	public Integer getSortValue() {
		return sortValue;
	}

	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}

	public Integer getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}
}