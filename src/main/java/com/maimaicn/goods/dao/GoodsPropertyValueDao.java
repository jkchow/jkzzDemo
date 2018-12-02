package com.maimaicn.goods.dao;

import java.util.List;
import java.util.Map;

import com.maimaicn.goods.domain.GoodsPropertyValue;
import com.maimaicn.utils.dao.BaseDao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-10-25 15:35:54
 */
@Repository
public class GoodsPropertyValueDao extends BaseDao<GoodsPropertyValue> {
	private static final String ns = "mappers.GoodsPropertyValueMapper.";

	public GoodsPropertyValueDao(){
		this.setTableName("goods_property_value");
		this.setTableId("valueId");
	}

	@Autowired
	private SqlSession sqlSession;

	/**
	 * @方法描述 获取分类属性值集合，根据属性id
	 * @author 张进
	 * @2017年10月25日 下午3:46:35
	 * @param propertyId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getValuesByPropertyId(Integer propertyId)throws Exception{
		return sqlSession.selectList(ns+"getValuesByPropertyId",propertyId);
	}
}