package com.maimaicn.goods.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maimaicn.goods.domain.GoodsPropertyLabel;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.dao.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-11-2 16:38:18
 */
@Repository
public class GoodsPropertyLabelDao extends BaseDao<GoodsPropertyLabel> {
	private static final String ns = "mappers.GoodsPropertyLabelMapper.";

	public GoodsPropertyLabelDao(){
		this.setTableName("goods_property_label");
		this.setTableId("labelId");
	}

	@Autowired
	private SqlSession sqlSession;

    /**
     * 获取商品属性标签的pagevo一页数据
     * @param categoryId
     * @param labelName
     * @return
     */
    public PageVO<Map<String,Object>> indexData(Integer pageNo,Integer pageSize,Integer categoryId, String labelName) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("startNo",(pageNo - 1)*pageSize);
        params.put("pageSize",pageSize);
        params.put("categoryId",categoryId);
        params.put("labelName",labelName);

        List<Map<String,Object>> list = sqlSession.selectList(ns + "getPageVO", params);
        int count = sqlSession.selectOne(ns + "getPageVOCount", params);

        return new PageVO<Map<String,Object>>(pageNo,pageSize,count,list);
    }


	/**
	 * @方法描述 根据分类id获取属性标签的下拉框数据，包含该分类的所有上级的所有属性标签
	 * @author 张进
	 * @2017年11月2日 下午5:36:19
	 * @param categoryId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getComboboxData(Integer categoryId)throws Exception{
		return sqlSession.selectList(ns+"getComboboxData",categoryId);
	}


}