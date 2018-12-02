package com.maimaicn.goods.dao;

import com.maimaicn.goods.domain.PromiseService;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.dao.BaseDao;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-9-26 16:58:04
 */
@Repository
public class PromiseServiceDao extends BaseDao<PromiseService> {
	private static final String ns = "mappers.PromiseServiceMapper.";

	public PromiseServiceDao(){
		this.setTableName("promise_service");
		this.setTableId("promiseId");
	}

	@Autowired
	private SqlSession sqlSession;


	/**
	 * 获取服务承诺后台的主页数据
	 * @param pageNo
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public PageVO<Map<String,Object>> getIndexData(Integer pageNo, Integer pageSize, Map<String, Object> params) {
		params.put("startNo",(pageNo-1)*pageSize);
		params.put("pageSize",pageSize);
		List<Map<String,Object>> list = sqlSession.selectList(this.ns + "getIndexData",params);
		int count = sqlSession.selectOne(this.ns + "getIndexDataCount",params);
		return new PageVO<>(pageNo,pageSize,count,list);
	}

	/**
	 * 添加服务承诺适用的范围
	 * @param promiseId
	 * @param categoryId
	 */
	public void addScope(Integer promiseId, Integer categoryId) {
		Map<String,Object> params = new HashMap<>();
		params.put("promiseId",promiseId);
		params.put("categoryId",categoryId);

		sqlSession.insert(this.ns + "addScopeCate",params);
	}

	/**
	 * 获取指定服务适用的范围分类列表
	 * @param pageNo
	 * @param pageSize
	 * @param promiseId
	 * @return
	 */
	public PageVO<Map<String,Object>> listScopeCat(Integer pageNo, Integer pageSize, Integer promiseId) {
		Map<String,Object> params = new HashMap<>();
		params.put("startNo",(pageNo - 1)* pageSize);
		params.put("pageSize",pageSize);
		params.put("promiseId",promiseId);

		List<Map<String,Object>> list = sqlSession.selectList(this.ns + "listScopeCat", params);
		int count = sqlSession.selectOne(this.ns+"listScopeCatCount",params);
		return new PageVO<Map<String,Object>>(pageNo,pageSize,count,list);
	}

	/**
	 * 撤销服务承诺所适用的分类
	 * @param promiseId
	 * @param categoryId
	 */
	public void deleteScopeCat(Integer promiseId, Integer categoryId) {
		Map<String,Object> params = new HashMap<>();
		params.put("categoryId",categoryId);
		params.put("promiseId",promiseId);

		sqlSession.delete(this.ns + "deleteScopeCat",params);
	}

	/**
	 * 在发布商品页，获取该分类下，该会员的服务承诺列表
	 * 1.适用所有类目的服务、
	 * 2.自己加入的服务
	 * 3、该分类下的共有服务
	 * @param categoryId
	 * @param memberId
	 * @return
	 */
	public List<Map<String,Object>> catService(Integer categoryId, Long memberId) {
		Map<String,Object> params = new HashMap<>();
		params.put("categoryId",categoryId);
		params.put("memberId",memberId);

		return sqlSession.selectList(this.ns + "catService",params);
	}

	/**
	 * 获取这个商品已加入的服务，和应有的服务。
	 * 1.首先获取该商品分类必有的服务，该会员必须的服务
	 * 2.在加上该商品选择的的可选服务
	 * 最后返回服务id的字符串，以，号隔开。最后直接存入商品基本信息表
	 * @param promSereIds
	 * @param categoryId
	 * @param loginMemberId
	 * @return
	 */
    public String getThisGoodsPromiseIds(Integer[] promSereIds,Integer categoryId, Long loginMemberId) {
    	Map<String,Object> params = new HashMap<>();
    	params.put("categoryId",categoryId);
    	params.put("memberId",loginMemberId);
    	List<Integer> mustSelect = sqlSession.selectList(this.ns+"mustSelectPromiseIds",params);

		if(promSereIds != null && promSereIds.length != 0){
			params.put("selected",promSereIds);
			//这个步骤主要是检验一遍，过滤掉不属于这个会员的服务，正常情况下是不存在这样的主要针对非法提交的数据，这里的数据由2部分组成
			//1.选择的无需订阅的服务id
			//2.需要订阅的服务（校验是不是该用户订阅的）
			List<Integer> selected = sqlSession.selectList(this.ns+"hasSelected",params);
			if(selected.size() != 0){
				mustSelect.addAll(selected);
			}
		}

		return StringUtils.strip(mustSelect.toString(),"[]");
    }
}