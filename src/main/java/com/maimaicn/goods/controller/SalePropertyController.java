package com.maimaicn.goods.controller;

import javax.servlet.http.HttpServletRequest;

import com.maimaicn.goods.dao.SalePropertyDao;
import com.maimaicn.goods.domain.SaleProperty;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.controller.BaseController;
import com.maimaicn.utils.permission.annotation.RequiresPermissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** 
 * @类描述 
 * @创建人  张进
 * @2017-11-6 11:40:50
 */
@RestController
public class SalePropertyController extends BaseController {
	@Autowired
	SalePropertyDao salePropertyDao;


	@RequiresPermissions("saleProperty")
	@RequestMapping("/saleProperty/indexData")
	public String getIndexData(HttpServletRequest request, Integer page, Integer rows, SaleProperty sp){
		try {
			PageVO<Map<String,Object>> pagevo = salePropertyDao.getPageVO(page, rows, sp);

			Map<String ,Object> result = new HashMap<String,Object>();
			result.put("total", pagevo.getRecordCount());
			result.put("rows", pagevo.getRecordList());
			return jsonData(request, 0,result);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 新增销售属性
	 * @author 张进
	 * @2017年11月7日 上午9:27:33
	 * @param request
	 * @param sp
	 * @return
	 */
	@RequiresPermissions("saleProperty:add")
	@RequestMapping("/saleProperty/add")
	public String add(HttpServletRequest request,SaleProperty sp){
		try {
			if(sp.getCategoryId() == null || StringUtils.isEmpty(sp.getCanCustomValue()) || StringUtils.isEmpty(sp.getIsRemark())
					|| StringUtils.isEmpty(sp.getNeedPropertyPic()) || StringUtils.isEmpty(sp.getPropertyName())
					|| StringUtils.isEmpty(sp.getPropertyType())|| sp.getSortValue()==null){
				return jsonData(request,1, "必要参数不能为空");
			}
			if(sp.getPropertyName().contains(",") || sp.getPropertyName().contains("，")){
				return jsonData(request,2,"属性名称不能特殊符号，如‘，’");
			}
			if(SaleProperty.PROPERTYTYPE_COLOR.equals(sp.getPropertyType()) && salePropertyDao.getColorTypeLength(sp.getCategoryId()) > 0){//一个分类下有允许设置一个颜色分类的销售属性
				return jsonData(request,3,"任何商品只能有一个颜色分类属性");
			}

			salePropertyDao.save(sp);
			return jsonData(request,0,"新增属性成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}
	
	
	/**
	 * @方法描述 更新属性成功
	 * @author 张进
	 * @2017年11月7日 上午9:33:55
	 * @param request
	 * @param sp
	 * @return
	 */
	@RequiresPermissions("saleProperty:update")
	@RequestMapping("/saleProperty/update")
	public String update(HttpServletRequest request,SaleProperty sp){
		try {
			if(sp.getPropertyId() ==  null || sp.getCategoryId() == null || StringUtils.isEmpty(sp.getPropertyName())){
				return jsonData(request,1, "必要参数不能为空");
			}
			if(sp.getPropertyName().contains(",")  || sp.getPropertyName().contains("，")){
				return jsonData(request,2,"属性名称不能特殊符号，如‘，’");
			}
			if(SaleProperty.PROPERTYTYPE_COLOR.equals(sp.getPropertyType()) && salePropertyDao.getColorTypeLength(sp.getCategoryId()) > 0){//一个分类下有允许设置一个颜色分类的销售属性
				return jsonData(request,3,"任何商品只能有一个颜色分类属性");
			}

			salePropertyDao.update(sp);
			return jsonData(request,0,"修改属性成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}

	/***
	 * 修改属性状态
	 * @param request
	 * @param status
	 * @return
	 */
	@RequiresPermissions("saleProperty:updateStatus")
	@RequestMapping("/saleProperty/updateStatus")
	public String updateStatus(HttpServletRequest request,Integer status,Integer propertyId) {
	    try{
	    	if(status == null || propertyId == null){
	    		return jsonData(request,1,"必要参数不能为空");
			}
			SaleProperty sp = new SaleProperty();
	    	sp.setPropertyId(propertyId);
	    	sp.setStatus(status==1?SaleProperty.STATUS_ENABLED:SaleProperty.STATUS_DISABLED);
			salePropertyDao.update(sp);

	        return jsonData(request,0,"状态更新成功");
	    } catch(Exception e){
	        log.error("异常",e);
	        return jsonData(request,110,"服务忙");
	    }
	}

	/**
	 * 删除销售属性，并且同时删掉属性标签和对应的属性值
	 * @param request
	 * @param propertyId
	 * @return
	 */
	@RequiresPermissions("saleProperty:delete")
	@RequestMapping("/saleProperty/delete")
	public String delete(HttpServletRequest request,Integer propertyId) {
	    try{
			if(propertyId == null){
				return jsonData(request,1,"必要参数不能为空");
			}
			salePropertyDao.delete(propertyId);

	        return jsonData(request,0,"删除成功");
	    } catch(Exception e){
	        log.error("异常",e);
	        return jsonData(request,110,"服务忙");
	    }
	}


	/**
	 * 根据分类id获取销售sku属性信息，用于添加商品sku时用，包含属性值，属性标签等信息，安装需要的格式给出
	 * @param request
	 * @param catId
	 * @return
	 */
	@RequestMapping("/saleProperty/catSkuProps")
	public String getByCatId(HttpServletRequest request,Integer catId) {
	    try{
	    	if(catId == null){
	    		return jsonData(request,1,"必要参数不能为空");
			}

			List<Map<String,Object>> list = salePropertyDao.getByCatId(catId);

	        return jsonData(request,0,list);
	    } catch(Exception e){
	        log.error("获取分类销售属性异常",e);
	        return jsonData(request,110,"服务忙");
	    }
	}

}