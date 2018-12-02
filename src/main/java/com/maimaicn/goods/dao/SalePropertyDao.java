package com.maimaicn.goods.dao;

import com.maimaicn.goods.domain.SaleProperty;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.dao.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-11-6 11:40:50
 */
@Repository
public class SalePropertyDao extends BaseDao<SaleProperty> {
	private static final String ns = "mappers.SalePropertyMapper.";

	public SalePropertyDao(){
		this.setTableName("sale_property");
		this.setTableId("propertyId");
	}

	@Autowired
	private SqlSession sqlSession;

	/**
	 * 获取销售属性后台所需分页数据
	 * @param pageNo
	 * @param pageSize
	 * @param saleProperty
	 * @return
	 */
    public PageVO<Map<String,Object>> getPageVO(Integer pageNo, Integer pageSize, SaleProperty saleProperty) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("startNo",(pageNo-1)*pageSize);
		params.put("pageSize",pageSize);
		params.put("propertyName",saleProperty.getPropertyName());
		params.put("propertyType",saleProperty.getPropertyType());
		params.put("categoryId",saleProperty.getCategoryId());

		List<Map<String,Object>> list = sqlSession.selectList(ns + "getPropertyPage", params);
		int count = sqlSession.selectOne(ns + "getPropertyPageCount", params);
		return new PageVO<Map<String,Object>>(pageNo,pageSize,count,list);
    }

	/**
	 * 覆盖默认删除方法,删除销售属性，并且同时删掉属性标签和对应的属性值
	 * @param id 属性id
	 */
	@Override
	public void delete(Serializable id) {
		sqlSession.delete(ns+"deleteSaleProperty",id);
	}

	/**
	 * 根据分类id获取销售sku属性信息，用于添加商品sku时用
	 * @param catId 分类id
	 * @return
	 */
	public List<Map<String,Object>> getByCatId(Integer catId) {
		return sqlSession.selectList(ns+"getByCatId",catId);
	}

	/**
	 * 获取该分类下颜色类型的销售属性个数
	 * @param categoryId
	 * @return
	 */
    public int getColorTypeLength(Integer categoryId) {
    	return sqlSession.selectOne(ns+"getColorTypeLength",categoryId);
    }
}