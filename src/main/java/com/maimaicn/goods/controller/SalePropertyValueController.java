package com.maimaicn.goods.controller;

import com.maimaicn.goods.dao.SalePropertyValueDao;
import com.maimaicn.goods.domain.SalePropertyValue;
import com.maimaicn.utils.controller.BaseController;
import com.maimaicn.utils.permission.annotation.RequiresPermissions;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @类描述 
 * @创建人  张进
 * @2017-11-6 11:41:24
 */
@RestController
public class SalePropertyValueController extends BaseController {
	@Autowired
	SalePropertyValueDao salePropertyValueDao;


	@RequiresPermissions("saleProperty:value")
	@RequestMapping("/salePropertyValue/indexData")
	public String getIndexData(HttpServletRequest request, Integer propertyId){
		try {
			if(propertyId == null){
				return jsonData(request,1,"必要参数不能为空");
			}
			List<Map<String,Object>> list = salePropertyValueDao.getAllByPropertyId(propertyId);
			return jsonData(request, 0,list);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}
	
	/**
	 * @方法描述 添加销售属性值
	 * @author 张进
	 * @2017年11月7日 下午4:29:33
	 * @param request
	 * @param spv
	 * @return
	 */
	@RequiresPermissions("salePropertyValue:add")
	@RequestMapping("/salePropertyValue/add")
	public String add(HttpServletRequest request,SalePropertyValue spv){
		try {
			if(spv.getPropertyId()==null || StringUtils.isEmpty(spv.getName())){
				return jsonData(request,1, "必要参数不能为空");
			}
			salePropertyValueDao.save(spv);
			return jsonData(request,0,"添加属性值成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 修改销售属性值
	 * @author 张进
	 * @2017年11月7日 下午4:30:10
	 * @param request
	 * @param spv
	 * @return
	 */
	@RequiresPermissions("salePropertyValue:update")
	@RequestMapping("/salePropertyValue/update")
	public String update(HttpServletRequest request,SalePropertyValue spv){
		try {
			if(spv.getValueId() == null || StringUtils.isEmpty(spv.getName())){
				return jsonData(request, 1,"必要参数不能为空");
			}
			salePropertyValueDao.updateNameAndLabel(spv);
			return jsonData(request,0,"修改属性值成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 删除销售属性值
	 * @author 张进
	 * @2017年11月7日 下午4:32:00
	 * @param request
	 * @param valueId
	 * @return
	 */
	@RequiresPermissions("salePropertyValue:delete")
	@RequestMapping("/salePropertyValue/delete")
	public String delete(HttpServletRequest request,Integer valueId){
		try {
			if(valueId == null){
				return jsonData(request,1, "必要参数不能为空");
			}
			salePropertyValueDao.delete(valueId);
			return jsonData(request,0,"属性值删除成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}

}