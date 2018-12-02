package com.maimaicn.goods.dao;

import java.util.List;
import java.util.Map;

import com.maimaicn.goods.domain.SalePropertyValueLabel;
import com.maimaicn.utils.dao.BaseDao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-11-6 11:43:17
 */
@Repository
public class SalePropertyValueLabelDao extends BaseDao<SalePropertyValueLabel> {

	private static final String ns = "mappers.SalePropertyValueLabelMapper.";

	public SalePropertyValueLabelDao(){
		this.setTableName("sale_property_value_label");
		this.setTableId("labelId");
	}

	@Autowired
	private SqlSession sqlSession;

	/**
	 * @方法描述  获取销售属性主页的数据
	 * @author 张进
	 * @2017年11月7日 下午2:55:43
	 * @param propertyId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllByPropertyId(Integer propertyId)throws Exception{
		return sqlSession.selectList(ns+"getAllByPropertyId",propertyId);
	}
	
	/**
	 * @方法描述 获取下拉框的数据
	 * @author 张进
	 * @2017年11月7日 下午5:37:27
	 * @param propertyId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getComboboxData(Integer propertyId)throws Exception{
		return sqlSession.selectList(ns+"getComboboxData",propertyId);
	}
}