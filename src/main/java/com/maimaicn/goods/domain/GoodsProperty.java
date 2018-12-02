package com.maimaicn.goods.domain;


/**
 * @类描述 
 * @创建人  张进
 * @2017-9-28 13:36:46
 */
public class GoodsProperty{
	public static final String BELONG_CATEGORY = "category";//该商品是属于分类下的属性，还是品牌下定义的属性，1-分类下的，2-品牌下的，3-属性值的属性
	public static final String BELONG_BRAND = "brand";//该商品是属于分类下的属性，还是品牌下定义的属性，1-分类下的，2-品牌下的，3-属性值的属性
	public static final String BELONG_PROPERTYVALUE = "propValue";//该商品是属于分类下的属性，还是品牌下定义的属性，1-分类下的，2-品牌下的，3-属性值的属性
	public static final String INPUTTYPE_INPUT = "input";
	public static final String INPUTTYPE_SELECT = "select";
	public static final String INPUTTYPE_CHECKBOX = "checkbox";
	public static final String CUSTOM_YES = "yes";
	public static final String CUSTOM_NO = "no";
	public static final String DATATYPE_TEXT = "text";
	public static final String DATATYPE_INT = "int";
	public static final String DATATYPE_DOUBLE2 = "double2";
	public static final String DATATYPE_DATE_YM = "date_ym";
	public static final String DATATYPE_DATE_YMD = "date_ymd";
	public static final String DATATYPE_DATE_RANGE = "date_range";
	public static final String ISREQUIRED_YES = "yes";
	public static final String ISREQUIRED_NO = "no";
	public static final String ISSEARCH_YES = "yes";
	public static final String ISSEARCH_NO = "no";

	private Integer propertyId;//
	private Integer categoryId;//类目id
	private String propertyName;//属性名称
	private String inputType;//输入类型，'input','select','checkbox',''
	private String dataType;//属性值类型：1-文本，2-整数，3-两位小数（12.25），4-日期（年-月，2017-05）
	private String belong;//该商品是属于分类下的属性，还是品牌下定义的属性，1-分类下的，2-品牌下的，3-属性值的属性
	private String isRequired;//是否是必填属性：0-非必填，1-必填
	private String isSearch;//是加入搜索，0-不参加搜索，1-加入搜索字段
	private String custom;//是否允许用户自定义属性值，yes-允许，no-不允许
	private String unit;//属性单位
	private String placeholder;//input输入框提示，当inputType为input时有效
	private String tip;//属性提示
	private Integer labelId;//属性标签id，标示该属性属性那个标记组
	private Integer brandId;//品牌id，当belong为2时，该条属性的品牌id不能为空
	private Integer valueId;//属性值id，当belong为3时，该条属性的属性值id不能为空
	private Integer sortValue;//排序值，值越小，排序越靠前

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public Integer getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getIsSearch() {
		return isSearch;
	}

	public void setIsSearch(String isSearch) {
		this.isSearch = isSearch;
	}

	public Integer getLabelId() {
		return labelId;
	}

	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Integer getValueId() {
		return valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}

	public Integer getSortValue() {
		return sortValue;
	}

	public void setSortValue(Integer sortValue) {
		this.sortValue = sortValue;
	}
}