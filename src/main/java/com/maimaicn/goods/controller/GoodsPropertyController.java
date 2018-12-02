package com.maimaicn.goods.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maimaicn.goods.dao.BrandDao;
import com.maimaicn.goods.dao.GoodsPropertyDao;
import com.maimaicn.goods.domain.GoodsProperty;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.controller.BaseController;
import com.maimaicn.utils.permission.annotation.RequiresPermissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-9-28 13:36:46
 */
@RestController
public class GoodsPropertyController extends BaseController {
	@Autowired
	private GoodsPropertyDao goodsPropertyDao;
	@Autowired
	private BrandDao brandDao;


	/**
	 * @方法描述 根据分类id获取该分类id下的所有属性
	 * @author 张进
	 * @2017年9月28日 下午1:38:50
	 * @param brandId 当这个值不为null时，表明获取的是品牌下的属性
	 * @return
	 */
	@RequiresPermissions("goodsProperty")
	@RequestMapping("/goodsProperty/getGoodsPropertyData")
	public String getCategoryPropertyData(HttpServletRequest request,Integer page, Integer rows,Integer categoryId, Integer brandId, Integer propertyValueId,String propertyName){
		try {
			PageVO<Map<String, Object>> pagevo = goodsPropertyDao.getPropertyPage(page,rows,categoryId,brandId,propertyValueId,propertyName);

			Map<String ,Object> result = new HashMap<String,Object>();
			result.put("total", pagevo.getRecordCount());
			result.put("rows", pagevo.getRecordList());
			return jsonData(request, 0,result);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}
	
	/**
	 * @方法描述 新增类目属性
	 * @author 张进
	 * @2017年10月25日 上午10:16:04
	 * @param request
	 * @param cp
	 * @return
	 */
	@RequiresPermissions("goodsProperty:add")
	@RequestMapping("/goodsProperty/add")
	public String add(HttpServletRequest request,GoodsProperty cp){
		try {
			if(cp.getIsRequired()==null || cp.getPropertyName()==null || "".equals(cp.getPropertyName())
					|| StringUtils.isEmpty(cp.getInputType()) || cp.getDataType()==null){
				return jsonData(request, 1,"必要参数不能为空");
			}
			
			if(cp.getValueId() != null){
				cp.setBelong(GoodsProperty.BELONG_PROPERTYVALUE);//值属性
				cp.setCategoryId(null);
				cp.setBrandId(null);
			}else if(cp.getBrandId() != null){
				cp.setBelong(GoodsProperty.BELONG_BRAND);//品牌属性
				cp.setCategoryId(null);
				cp.setValueId(null);
			}else if(cp.getCategoryId() != null){
				cp.setBelong(GoodsProperty.BELONG_CATEGORY);
				cp.setBrandId(null);
				cp.setValueId(null);
			}else {
			    return jsonData(request,2,"必要参数不能为空");
            }

			goodsPropertyDao.save(cp);
			return jsonData(request,0,"添加属性成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 更新类目属性
	 * @author 张进
	 * @2017年10月25日 上午10:17:02
	 * @param request
	 * @param cp
	 * @return
	 */
	@RequiresPermissions("goodsProperty:update")
	@RequestMapping("/goodsProperty/update")
	public String update(HttpServletRequest request,GoodsProperty cp){
		try {
			if(cp.getPropertyId()==null || cp.getIsRequired()==null || cp.getPropertyName()==null || "".equals(cp.getPropertyName())
					|| StringUtils.isEmpty(cp.getInputType()) || cp.getDataType()==null){
				return jsonData(request,1, "必要参数不能为空");
			}

			cp.setBelong(null);//不能修改的值
//			cp.setBrandId(null);
//			cp.setCategoryId(null);
			cp.setValueId(null);
			goodsPropertyDao.update(cp);
			return jsonData(request,0,"修改属性成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 删除属性
	 * @author 张进
	 * @2017年10月25日 上午11:24:43
	 * @param request
	 * @return
	 */
	@RequiresPermissions("goodsProperty:delete")
	@RequestMapping("/goodsProperty/delete")
	public String delete(HttpServletRequest request,Integer propertyId){
		try {
			if(propertyId == null){
				return jsonData(request, 1,"必要参数不能为空");
			}
			goodsPropertyDao.delete(propertyId);
			return jsonData(request,0,"属性删除成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}


	/**
	 * 获取该分类下，商品的基本属性信息，及属性值信息，品牌信息加在第一位
	 * @param request
	 * @param catId
	 * @return
	 */
	@RequestMapping("/goodsProperty/catGoodsProps")
	public String catGoodsProps(HttpServletRequest request,Integer catId,Integer brandId,Integer valueId) {
	    try{
	    	if(catId == null && brandId == null && valueId == null){
	    		return jsonData(request,1,"必要参数不能为空");
			}
			Map<String,Object> brands = null;
			if(catId != null){
				brands = new HashMap<String,Object>();
				List<Map<String,Object>> brandList = brandDao.combobox(catId);
				brands.put("propertyId",-1);//-1表示品牌
				brands.put("propertyName","品牌");
				brands.put("inputType","select");
				brands.put("dataType","text");
				brands.put("isRequired","yes");
				brands.put("custom","yes");
				brands.put("belong","category");
				brands.put("values",brandList);
			}
			List<Map<String,Object>> list = goodsPropertyDao.getCatGoodsProps(catId,brandId,valueId);
			if(brands != null){
				list.add(0,brands);
			}
	        
	        return jsonData(request,0,list);
	    } catch(Exception e){
	        log.error("获取分类商品基本属性异常",e);
	        return jsonData(request,110,"服务忙");
	    }
	}

}