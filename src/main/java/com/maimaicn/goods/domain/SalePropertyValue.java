package com.maimaicn.goods.domain;


/**
 * @类描述 
 * @创建人  张进
 * @2017-11-6 11:41:24
 */
public class SalePropertyValue{
	private Integer valueId;//销售属性值id
	private String name;//销售属性值名称
	private String alias;//属性值别名，例如，属性类型为颜色时，该值存的就是颜色值
	private Integer propertyId;//销售属性id
	private Integer labelId;//该销售属性所属的标签id
	private Integer sortValue;//排序值，越小越靠前

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}
	public Integer getValueId(){
		return valueId;
	}
	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}
	public Integer getPropertyId(){
		return propertyId;
	}
	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}
	public Integer getLabelId(){
		return labelId;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSortValue() {
		return sortValue;
	}
	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}
}