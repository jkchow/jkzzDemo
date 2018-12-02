package com.maimaicn.goods.dao;

import java.util.List;
import java.util.Map;

import com.maimaicn.goods.domain.SalePropertyValue;
import com.maimaicn.utils.dao.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-11-6 11:41:24
 */
@Repository
public class SalePropertyValueDao extends BaseDao<SalePropertyValue> {
	private static final String ns = "mappers.SalePropertyValueMapper.";

	public SalePropertyValueDao(){
		this.setTableName("sale_property_value");
		this.setTableId("valueId");
	}

	@Autowired
	private SqlSession sqlSession;

	/**
	 * @方法描述 根据销售属性id获取该属性下所有的值
	 * @author 张进
	 * @2017年11月7日 下午4:24:47
	 * @param propertyId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllByPropertyId(Integer propertyId)throws Exception{
		return sqlSession.selectList(ns+"getAllByPropertyId", propertyId);
	}
	
	/**
	 * @方法描述 修改销售属性值，由于需要值标签需要设置为null的情况，所以需要自己实现
	 * @author 张进
	 * @2017年11月7日 下午6:04:47
	 * @param spv
	 * @return
	 * @throws Exception
	 */
	public int updateNameAndLabel(SalePropertyValue spv)throws Exception{
		return sqlSession.update(ns+"updateNameAndLabel", spv);
	}
}