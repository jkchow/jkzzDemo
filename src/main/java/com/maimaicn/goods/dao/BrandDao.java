package com.maimaicn.goods.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maimaicn.goods.domain.Brand;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.dao.BaseDao;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @类描述
 * @创建人  张进
 * @2017-9-26 16:58:04
 */
@Repository
public class BrandDao extends BaseDao<Brand> {
	private static final String ns = "mappers.BrandMapper.";

	public BrandDao(){
		this.setTableName("brand");
		this.setTableId("brandId");
	}

	@Autowired
	private SqlSession sqlSession;

	/**
	 * @方法描述 获取后台品牌首页的数据
	 * @author 张进
	 * @2017年9月26日 下午5:21:15
	 * @param pageNo
	 * @param pageSize
	 * @param params 查询的参数
	 * @return
	 * @throws Exception
	 */
	public PageVO<Map<String,Object>> getIndexData(Integer pageNo, Integer pageSize, Map<String, Object> params)throws Exception{
		params.put("startNo", (pageNo-1)*pageSize);
		params.put("pageSize", pageSize);
		List<Map<String,Object>> list = sqlSession.selectList(ns+"getIndexData", params);
		int count = sqlSession.selectOne(ns+"getIndexDataCount",params);
		return new PageVO<Map<String,Object>>(pageNo, pageSize, count, list);
	}

	/**
	 * 根据分类id获取该分类下所有的品牌下拉列表
	 * @param categoryId
	 * @return
	 */
	public List<Map<String,Object>> combobox(Integer categoryId) {
		List<Map<String,Object>> list = sqlSession.selectList(ns+"combobox",categoryId);
		for(Map<String,Object> map : list){
			if(map.get("enName") != null){
				map.put("brandName",map.get("chName") + "/" + map.get("enName"));
			}else {
				map.put("brandName",map.get("chName"));
			}
			map.remove("chName");
			map.remove("enName");
		}
		return list;
	}

	/**
	 * 给品牌添加经营类目
	 * @param brandId
	 * @param categoryId
	 */
	public void addBusinessCat(Integer brandId, Integer categoryId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("brandId",brandId);
		params.put("categoryId",categoryId);
		sqlSession.insert(ns+"addBusinessCat",params);
	}

	/**
	 * 删除品牌经营类目
	 * @param brandId
	 * @param categoryId
	 */
	public void deleteBusinessCat(Integer brandId, Integer categoryId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("brandId",brandId);
		params.put("categoryId",categoryId);
		sqlSession.delete(ns+"deleteBusinessCat",params);
	}

	/**
	 * 获取该品牌经营的类目列表
	 * @param brandId
	 * @return
	 */
	public List<Map<String,Object>> getBusinessCatList(Integer brandId) {
		return sqlSession.selectList(ns+"getBusinessCatList",brandId);
	}

	/**
	 * 获取品牌的简单信息，包括名称、logo图片
	 * @param brandId
	 * @return
	 */
    public List<Map<String,Object>> getBrandSimpleInfo(Integer[] brandId) {
		List<Map<String,Object>> list = sqlSession.selectList(ns+"getBrandSimpleInfo",Arrays.asList(brandId));
		for(Map<String,Object> map: list){
			if(map.get("logo") == null || StringUtils.isEmpty((String) map.get("logo"))){
				map.put("logo","");
			}
		}

		return list;
    }
}