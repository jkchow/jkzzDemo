package com.maimaicn.goods.domain;


/**
 * @类描述 
 * @创建人  张进
 * @2017-11-2 16:38:18
 */
public class GoodsPropertyLabel{
	private Integer labelId;//
	private String labelName;//标签名称
	private Integer categoryId;//这个标签所属的分类id
	private Integer sortValue;//排序值，越小越靠前

	public Integer getSortValue() {
		return sortValue;
	}
	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}
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
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getCategoryId(){
		return categoryId;
	}
}