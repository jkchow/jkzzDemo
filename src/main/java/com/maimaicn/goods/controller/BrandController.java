package com.maimaicn.goods.controller;

import com.maimaicn.goods.dao.BrandDao;
import com.maimaicn.goods.domain.Brand;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.controller.BaseController;
import com.maimaicn.utils.permission.annotation.RequiresPermissions;
import jdk.nashorn.internal.ir.BreakableNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-9-26 16:58:04
 */
@RestController
public class BrandController extends BaseController {

	@Autowired
	private BrandDao brandDao;


	@RequiresPermissions("brand")
	@RequestMapping("/brand/indexData")
	public String getIndexData(HttpServletRequest request, Integer page, Integer rows, String chName,String enName, Integer categoryId){
		try {
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("chName", chName);
			params.put("enName", enName);
			params.put("categoryId", categoryId);
			page=page<1?1:page;
			PageVO<Map<String,Object>> pagevo = brandDao.getIndexData(page,rows,params);

			params.clear();
			params.put("total", pagevo.getRecordCount());
			params.put("rows", pagevo.getRecordList());
			return jsonData(request,0, params);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}

	/**
	 * 根据分类id获取该分类下所有的品牌下拉列表
	 * @param request
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/brand/combobox")
	public String combobox(HttpServletRequest request, Integer categoryId){
		try {
			if(categoryId == null){
				return jsonData(request,1,"必要参数不能为空");
			}
			List<Map<String,Object>> list = brandDao.combobox(categoryId);

			return jsonData(request,0, list);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 新增品牌
	 * @author 张进
	 * @2017年10月29日 下午3:54:57
	 * @param request
	 * @param gb
	 * @return
	 */
	@RequiresPermissions("brand:add")
	@RequestMapping("/brand/add")
	public String add(HttpServletRequest request,Brand gb){
		try {
			if(StringUtils.isEmpty(gb.getChName())){
				return jsonData(request,1, "必要参数不能为空");
			}
			brandDao.save(gb);
			return jsonData(request,0,"新增品牌成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}
	/**
	 * @方法描述 修改品牌
	 * @author 张进
	 * @2017年10月29日 下午3:56:03
	 * @param request
	 * @param gb
	 * @return
	 */
	@RequiresPermissions("brand:update")
	@RequestMapping("/brand/update")
	public String update(HttpServletRequest request,Brand gb){
		try {
			if(gb.getBrandId()==null|| StringUtils.isEmpty(gb.getChName())){
				return jsonData(request,1, "必要参数不能为空");
			}
			brandDao.update(gb);
			return jsonData(request,0,"品牌修改成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 删除品牌
	 * @author 张进
	 * @2017年10月29日 下午3:57:12
	 * @param request
	 * @param brandId
	 * @return
	 */
	@RequiresPermissions("brand:delete")
	@RequestMapping("/brand/delete")
	public String delete(HttpServletRequest request,Integer brandId){
		try {
			if(brandId == null){
				return jsonData(request,1, "必要参数不能为空");
			}
			brandDao.delete(brandId);
			return jsonData(request,0,"品牌删除成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}


	/**
	 * 给品牌添加经营类目
	 * @param request
	 * @param brandId
	 * @param categoryId
	 * @return
	 */
	@RequiresPermissions("brand:addBusinessCat")
	@RequestMapping("/brand/addBusinessCat")
	public String addBusinessCat(HttpServletRequest request,Integer brandId,Integer categoryId) {
	    try{
	    	if(brandId == null || categoryId == null){
	    		return jsonData(request,1,"必要参数不能为空");
			}
			brandDao.addBusinessCat(brandId,categoryId);
	        
	        return jsonData(request,0,"添加成功");
	    } catch(DuplicateKeyException e){
	    	return jsonData(request,2,"该类目已经添加了哦！");
	    } catch(Exception e){
	        log.error("添加品牌类目异常",e);
	        return jsonData(request,110,"服务忙");
	    }
	}

	/**
	 * 获取该品牌经营的类目列表
	 * @param request
	 * @param brandId
	 * @return
	 */
	@RequiresPermissions("brand:deleteBusinessCat")
	@RequestMapping("/brand/listBusinessCat")
	public String listBusinessCat(HttpServletRequest request,Integer brandId) {
	    try{
	    	if(brandId == null){
	    		return jsonData(request,1,"必要参数不能为空");
			}
	        List<Map<String,Object>> list = brandDao.getBusinessCatList(brandId);

	        return jsonData(request,0,list);
	    } catch(Exception e){
	        log.error("异常",e);
	        return jsonData(request,110,"服务忙");
	    }
	}

	/**
	 * 删除品牌经营类目
	 * @param request
	 * @param brandId
	 * @param categoryId
	 * @return
	 */
	@RequiresPermissions("brand:deleteBusinessCat")
	@RequestMapping("/brand/deleteBusinessCat")
	public String deleteBusinessCat(HttpServletRequest request,Integer brandId,Integer categoryId) {
		try{
			if(brandId == null || categoryId == null){
				return jsonData(request,1,"必要参数不能为空");
			}
			brandDao.deleteBusinessCat(brandId,categoryId);

			return jsonData(request,0,"取消成功");
		} catch(Exception e){
			log.error("取消品牌经营类目异常",e);
			return jsonData(request,110,"服务忙");
		}
	}


	/**
	 * @方法描述 获取我的品牌(添加广告源时所需)
	 * @author full
	 * 2018年11月15日 上午10:37:26
	 * @return
	 */
	@RequestMapping("/getBrandPage")
	public String getBrandPage(HttpServletRequest request,Integer pageNo,Integer pageSize,Brand b){
		try {

			PageVO<Brand> pageVO = brandDao.getPageVO(pageNo, pageSize,b,null,null);
			return jsonData(request,0, pageVO);
		} catch (Exception e) {
			log.error("获取品牌异常", e);
			return jsonData(request,110, "服务忙");
		}

	}

	/**
	 * 获取品牌信息，根据品牌id,小芳用
	 * @param request
	 * @return
	 */
	@RequestMapping("/brand/info")
	public String brandInfo(HttpServletRequest request,Integer[] brandId) {
	    try{
	    	if(brandId == null || brandId.length == 0){
	    		return jsonData(request,1,"必要参数不能为空");
			}
			List<Map<String,Object>> list = brandDao.getBrandSimpleInfo(brandId);


	        return jsonData(request,0,list);
	    } catch(Exception e){
	        log.error("异常",e);
	        return jsonData(request,110,"服务忙");
	    }
	}

}