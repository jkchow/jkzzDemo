package com.maimaicn.goods.domain;


/**
 * @类描述 
 * @创建人  张进
 * @2017-9-26 14:10:44
 */
public class Category {
	private Integer categoryId;//商品类目id
	private String categoryName;//商品类目名称
	private Integer parentId;//父级类目id

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getCategoryId(){
		return categoryId;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryName(){
		return categoryName;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getParentId(){
		return parentId;
	}
}