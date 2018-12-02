package com.maimaicn.goods.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maimaicn.goods.domain.GoodsProperty;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.dao.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @类描述 
 * @创建人  张进
 * @2017-9-28 13:36:46
 */
@Repository
public class GoodsPropertyDao extends BaseDao<GoodsProperty> {
	private static final String ns = "mappers.GoodsPropertyMapper.";

	public GoodsPropertyDao(){
		this.setTableName("goods_property");
		this.setTableId("propertyId");
	}

	@Autowired
	private SqlSession sqlSession;


	/**
	 * @方法描述 根据分类id获取该分类下的所有属性列表
	 * @author 张进
	 * @2017年9月28日 下午1:41:06
	 * @param categoryId 类目id
	 * @return
	 * @throws Exception
	 */
	public PageVO<Map<String,Object>> getPropertyPage(Integer pageNo,Integer pageSize,Integer categoryId,Integer brandId,Integer valueId,String propertyName)throws Exception{
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("startNo",(pageNo-1)*pageSize);
		params.put("pageSize",pageSize);
		params.put("categoryId", categoryId);
		params.put("brandId", brandId);
		params.put("valueId", valueId);
		params.put("propertyName", propertyName);

		List<Map<String,Object>> list = sqlSession.selectList(ns + "getPropertyPage", params);
		int count = sqlSession.selectOne(ns + "getPropertyPageCount", params);
		return new PageVO<Map<String,Object>>(pageNo,pageSize,count,list);
	}

	/**
	 * 根据分类id、品牌id，属性值id分别拿对应的商品属性，属性值集合，不能同时取，单次只能有一个id查询
	 * @param catId
	 * @param brandId
	 * @param valueId
	 * @return
	 */
    public List<Map<String,Object>> getCatGoodsProps(Integer catId, Integer brandId, Integer valueId) {
		Map<String,Object> params = new HashMap<String, Object>();
		if(catId != null){
			params.put("categoryId",catId);
		}else if(brandId != null){
			params.put("brandId",brandId);
		}else if(valueId != null){
			params.put("valueId",valueId);
		}else {
			return null;
		}

		return sqlSession.selectList(ns+"getCatGoodsProps",params);
    }
}