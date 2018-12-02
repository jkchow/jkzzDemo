package com.maimaicn.goods.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maimaicn.goods.domain.Category;
import com.maimaicn.utils.dao.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-9-26 14:10:44
 */
@Repository
public class CategoryDao extends BaseDao<Category> {
	private static final String ns = "mappers.CategoryMapper.";
	
	public CategoryDao(){
		this.setTableName("category");
		this.setTableId("categoryId");
	}

	@Autowired
	private SqlSession sqlSession;

	/**
	 * @方法描述 获取商品类目后台主页数据
	 * @author 张进
	 * @2017年9月26日 下午3:35:56
	 * @param parentId 父id
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getIndexData(Integer parentId)throws Exception{
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		return sqlSession.selectList(ns+"getIndexData", params);
	}

	/**
	 * 获取分类下拉框树形列表
	 * @param parentId
	 * @return
	 */
    public List<Map<String,Object>> combotree(Integer parentId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("parentId", parentId);
		return sqlSession.selectList(ns+"combotree", params);
    }

	/**
	 * 搜索时用
	 * 现在已有分类id catId，根据分类id获取，该分类下的品牌、分类下的子分类（叶子分类就获取兄弟分类）、该分类下的可搜索参数名，参数所有值
	 * @param categoryId
	 * @return
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Map<String,Object> getSearchNavigate(Integer categoryId) {
		//获取这个分类下的品牌
		List brands = sqlSession.selectList(this.ns+"getBrandForSearch",categoryId);
		//获取这个分类的子分类，（叶子分类就获取兄弟分类）
		List catList = sqlSession.selectList(this.ns+"getSonCatForSearch",categoryId);
		if(catList.size() == 0 ){//没有子分类，就获取兄弟分类
			catList = sqlSession.selectList(this.ns+"getSiblingCatForSearch",categoryId);
		}

		//获取这个分类下的可搜索的商品属性，及属性参数值
		List params = sqlSession.selectList(this.ns+"getParamsForSearch",categoryId);

		Map<String,Object> result = new HashMap<>();
		result.put("brands",brands);
		result.put("catList",catList);
		result.put("params",params);

		return result;
    }

	/**
	 * 上传商品，选择分类需要的数据
	 * @param pId
	 * @return
	 */
	public List<Map<String,Object>> getByPId(Integer pId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("parentId", pId);
		return sqlSession.selectList(this.ns+"getByPId",params);
    }

    //获取所有父分类
	public Integer getAllCategory(Integer categoryId) {
		Map<String,Object> params = new HashMap<>();
		params.put("categoryId", categoryId);
		return sqlSession.selectOne(this.ns+"getAllCategory",params);
	}

	/**
	 * 通过叶子节点，获取该分类的所有父级名称
	 * @param cateId
	 * @return
	 */
	public List<Category> getCategoryParents(Integer cateId) {
		return sqlSession.selectList(this.ns+"getCategoryParents",cateId);
	}
}