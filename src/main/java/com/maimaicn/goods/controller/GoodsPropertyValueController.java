package com.maimaicn.goods.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.maimaicn.goods.dao.GoodsPropertyValueDao;
import com.maimaicn.goods.domain.GoodsPropertyValue;
import com.maimaicn.utils.controller.BaseController;
import com.maimaicn.utils.permission.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-10-25 15:35:54
 */
@RestController
public class GoodsPropertyValueController extends BaseController {
	@Autowired
	GoodsPropertyValueDao goodsPropertyValueDao;


	/**
	 * @方法描述 获取分类属性值集合，根据属性id
	 * @author 张进
	 * @2017年10月25日 下午3:51:02
	 * @param request
	 * @return
	 */
	@RequiresPermissions("goodsProperty:value")
	@RequestMapping("/goodsPropertyValue/getValuesByPropertyId")
	public String getIndexData(HttpServletRequest request, Integer propertyId){
		try {
			List<Map<String,Object>> list = goodsPropertyValueDao.getValuesByPropertyId(propertyId);
			return jsonData(request,0, list);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}
	
	/**
	 * @方法描述 添加值
	 * @author 张进
	 * @2017年10月25日 下午3:53:17
	 * @param request
	 * @param cpv
	 * @return
	 */
	@RequiresPermissions("goodsPropertyValue:add")
	@RequestMapping("/goodsPropertyValue/add")
	public String add(HttpServletRequest request,GoodsPropertyValue cpv){
		try {
			if(cpv.getPropertyId()==null || cpv.getValueName()==null || "".equals(cpv.getValueName()) || cpv.getSortValue() == null){
				return jsonData(request,1, "必要参数不能为空");
			}
			
			goodsPropertyValueDao.save(cpv);
			return jsonData(request,0,"新增成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}
	
	/**
	 * @方法描述 修改值
	 * @author 张进
	 * @2017年10月25日 下午3:55:11
	 * @param request
	 * @param cpv
	 * @return
	 */
	@RequiresPermissions("goodsPropertyValue:update")
	@RequestMapping("/goodsPropertyValue/update")
	public String update(HttpServletRequest request,GoodsPropertyValue cpv){
		try {
			if(cpv.getValueId()==null || cpv.getPropertyId()==null || cpv.getValueName()==null || "".equals(cpv.getValueName()) || cpv.getSortValue() == null){
				return jsonData(request, 1,"必要参数不能为空");
			}
			goodsPropertyValueDao.update(cpv);
			return jsonData(request,0,"修改成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}
	
	/**
	 * @方法描述 删除属性值
	 * @author 张进
	 * @2017年10月25日 下午3:56:16
	 * @param request
	 * @return
	 */
	@RequiresPermissions("goodsPropertyValue:delete")
	@RequestMapping("/goodsPropertyValue/delete")
	public String delete(HttpServletRequest request,Integer valueId){
		try {
			if(valueId == null){
				return jsonData(request, 1,"必要参数不能为空");
			}
			goodsPropertyValueDao.delete(valueId);
			return jsonData(request,0,"删除成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}

}