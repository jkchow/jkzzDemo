package com.maimaicn.goods.domain;


/**
 * @类描述 
 * @创建人  张进
 * @2017-11-6 15:21:51
 */
public class SaleProperty{
	public static final String PROPERTYTYPE_COLOR = "color";//属性类型，'checkbox','color'
	public static final String PROPERTYTYPE_CHECKBOX = "checkbox";//属性类型，'checkbox','color'
	public static final String ISREMARK_NO = "no";//该属性的值是否需要设置备注，yes-可以设置备注，no-不需要设置备注
	public static final String ISREMARK_YES = "yes";//该属性的值是否需要设置备注，yes-可以设置备注，no-不需要设置备注
	public static final String CANCUSTOMVALUE_YES = "yes";//是否可以自定义属性值，'no','yes'
	public static final String CANCUSTOMVALUE_NO = "no";//是否可以自定义属性值，'no','yes'
	public static final String NEEDPROPERTYPIC_NO = "no";//是否需要商品规格属性图片，yes-需要，no-不需要
	public static final String NEEDPROPERTYPIC_YES = "yes";//是否需要商品规格属性图片，yes-需要，no-不需要
	public static final String STATUS_DISABLED = "disabled";//属性状态，disabled-禁用，enabled-可用
	public static final String STATUS_ENABLED = "enabled";//属性状态，disabled-禁用，enabled-可用
	public static final String ISREQUIRED_YES = "yes";//是否必须填写，yes-必填，no-非必填
	public static final String ISREQUIRED_NO = "no";//是否必须填写，yes-必填，no-非必填

	private Integer propertyId;//
	private String propertyName;//销售属性名称
	private Integer categoryId;//分类id
	private String remark;//属性备注，当属性名称一样时，可以使用别名来区分
	private String propertyType;//属性类型，'checkbox','color'
	private String isRemark;//该属性的值是否需要设置备注，yes-可以设置备注，no-不需要设置备注
	private String canCustomValue;//是否可以自定义属性值，'no','yes'
	private String needPropertyPic;//是否需要商品规格属性图片，yes-需要，no-不需要
	private String isRequired;//是否必须填写，yes-必填，no-非必填
	private String status;//属性状态，disabled-禁用，enabled-可用
	private Integer sortValue;//属性排序
	private String tip;//属性提示

	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}
	public Integer getPropertyId(){
		return propertyId;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getPropertyName(){
		return propertyName;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark(){
		return remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getPropertyType(){
		return propertyType;
	}
	public void setIsRemark(String isRemark) {
		this.isRemark = isRemark;
	}
	public String getIsRemark(){
		return isRemark;
	}
	public void setCanCustomValue(String canCustomValue) {
		this.canCustomValue = canCustomValue;
	}
	public String getCanCustomValue(){
		return canCustomValue;
	}
	public void setNeedPropertyPic(String needPropertyPic) {
		this.needPropertyPic = needPropertyPic;
	}
	public String getNeedPropertyPic(){
		return needPropertyPic;
	}
	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}
	public Integer getSortValue(){
		return sortValue;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getTip(){
		return tip;
	}

}