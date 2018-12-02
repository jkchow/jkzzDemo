package com.maimaicn.goods.controller;

import java.util.List;
import java.util.Map;
import com.maimaicn.goods.dao.CategoryDao;
import com.maimaicn.goods.domain.Category;
import com.maimaicn.utils.controller.BaseController;
import com.maimaicn.utils.permission.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-9-26 14:10:44
 */
@RestController
public class CategoryController extends BaseController {
	@Autowired
	CategoryDao categoryDao;


	@RequiresPermissions("category:index")
	@RequestMapping("/category/indexData")
	public String getIndexData(HttpServletRequest request, Integer id){
		try {
			List<Map<String, Object>> all = categoryDao.getIndexData(id);
			return jsonData(request, 0,all);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}

	/**
	 * 获取分类下拉框树形列表
	 * @param request
	 * @param id
	 * @return
	 */
	@RequiresPermissions("category:index")
	@RequestMapping("/category/combotree")
	public String combotree(HttpServletRequest request, Integer id){
		try {
			List<Map<String, Object>> all = categoryDao.combotree(id);
			return jsonData(request, 0,all);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 新增分类
	 * @author 张进
	 * @2017年9月26日 下午3:30:44
	 * @param request
	 * @param gc
	 * @return
	 */
	@RequiresPermissions("category:add")
	@RequestMapping("/category/add")
	public String add(HttpServletRequest request,Category gc){
		try {
			if(gc.getCategoryName() == null || "".equals(gc.getCategoryName())){
				return jsonData(request,1, "必要参数不能为空");
			}
			
			categoryDao.save(gc);
			return jsonData(request,0,"新增分类成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 修改商品分类
	 * @author 张进
	 * @2017年9月26日 下午3:30:58
	 * @param request
	 * @return
	 */
	@RequiresPermissions("category:update")
	@RequestMapping("/category/update")
	public String update(HttpServletRequest request,Category gc){
		try {
			if(gc.getCategoryId() == null || gc.getCategoryName() == null || "".equals(gc.getCategoryName())){
				return jsonData(request, 1,"必要参数不能为空");
			}
			categoryDao.update(gc);
			return jsonData(request,0,"修改分类成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	
	/**
	 * @方法描述 删除商品类目
	 * @author 张进
	 * @2017年9月26日 下午3:33:11
	 * @param request
	 * @param categoryId
	 * @return
	 */
	@RequiresPermissions("category:delete")
	@RequestMapping("/category/delete")
	public String delete(HttpServletRequest request,Integer categoryId){
		try {
			if(categoryId == null){
				return jsonData(request, 1,"必要参数不能为空");
			}
			
			categoryDao.delete(categoryId);
			return jsonData(request,0,"类目删除成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}


	/**
	 * 上传商品，选择分类需要的数据
	 * @param request
	 * @param pId
	 * @return
	 */
	@RequestMapping("/category/getByPId")
	public String getByPId(HttpServletRequest request,Integer pId) {
	    try{
			List<Map<String, Object>> all = categoryDao.getByPId(pId);
			return jsonData(request, 0,all);
	    } catch(Exception e){
	        log.error("异常",e);
	        return jsonData(request,110,"服务忙");
	    }
	}


	/**
	 * 通过叶子节点，获取该分类的所有父级名称
	 * @param request
	 * @return
	 */
	@RequestMapping("/category/parents")
	public String getCategoryParents(HttpServletRequest request,Integer cateId) {
	    try{
	    	if(cateId == null){
	    		return jsonData(request,1,"必要参数不能为空");
			}
			Long loginMemberId = getLoginMemberId(request);
	    	if(loginMemberId == null){
	    		return jsonData(request,100,"登录超时");
			}

			List<Category> list = categoryDao.getCategoryParents(cateId);

	    	StringBuilder sb = new StringBuilder();
	    	Integer parentId = null;
	    	while (parentId == null || parentId != cateId.intValue()){
				for(Category c : list){
					if(parentId == null || parentId == c.getParentId().intValue()){
						sb.append(c.getCategoryName());
						sb.append(",");
						parentId = c.getCategoryId();
					}
				}
			}

			return jsonData(request,0,sb.toString().substring(0,sb.length()-1));
	    } catch(Exception e){
	        log.error("异常",e);
	        return jsonData(request,110,"服务忙");
	    }
	}

}