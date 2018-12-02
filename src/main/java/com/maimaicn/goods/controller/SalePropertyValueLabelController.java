package com.maimaicn.goods.controller;

import com.maimaicn.goods.dao.SalePropertyValueLabelDao;
import com.maimaicn.goods.domain.SalePropertyValueLabel;
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
 * @2017-11-6 11:43:17
 */
@RestController
public class SalePropertyValueLabelController extends BaseController {
	@Autowired
	SalePropertyValueLabelDao salePropertyValueLabelDao;


	/**
	 * @方法描述 获取销售属性主页的数据
	 * @author 张进
	 * @2017年11月7日 下午2:52:22
	 * @param request
	 * @param propertyId
	 * @return
	 */
	@RequiresPermissions("saleProperty:valueLabel")
	@RequestMapping("/salePropValLabel/pageData")
	public String getIndexData(HttpServletRequest request, Integer propertyId){
		try {
			if(propertyId == null){
				return jsonData(request, 0,"必要参数不能为空");
			}
			List<Map<String,Object>> list = salePropertyValueLabelDao.getAllByPropertyId(propertyId);
			return jsonData(request, 0,list);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 获取下拉框的数据
	 * @author 张进
	 * @2017年11月7日 下午5:36:48
	 * @param request
	 * @param propertyId
	 * @return
	 */
	@RequestMapping("/salePropValLabel/combobox")
	public String getComboboxData(HttpServletRequest request,Integer propertyId){
		try {
			if(propertyId == null){
				return jsonData(request, 1,"必要参数不能为空");
			}
			List<Map<String,Object>> list = salePropertyValueLabelDao.getComboboxData(propertyId);
			return jsonData(request, 0,list);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,0, "服务忙");
		}
	}
	
	/**
	 * @方法描述 新增属形值标签
	 * @author 张进
	 * @2017年11月7日 下午3:19:21
	 * @param request
	 * @param spvl
	 * @return
	 */
	@RequiresPermissions("salePropValLabel:add")
	@RequestMapping("/salePropValLabel/add")
	public String add(HttpServletRequest request,SalePropertyValueLabel spvl){
		try {
			if(spvl.getPropertyId() == null || StringUtils.isEmpty(spvl.getLabelName())){
				return jsonData(request,1, "必要参数不能为空");
			}
			salePropertyValueLabelDao.save(spvl);
			return jsonData(request,0,"新增成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 修改属性值标签
	 * @author 张进
	 * @2017年11月7日 下午3:21:19
	 * @param request
	 * @param spvl
	 * @return
	 */
	@RequiresPermissions("salePropValLabel:update")
	@RequestMapping("/salePropValLabel/update")
	public String update(HttpServletRequest request,SalePropertyValueLabel spvl){
		try {
			if(StringUtils.isEmpty(spvl.getLabelName())){
				return jsonData(request,1, "必要参数不能为空");
			}
			salePropertyValueLabelDao.update(spvl);
			return jsonData(request,0,"修改成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 删除属性值标签
	 * @author 张进
	 * @2017年11月7日 下午3:21:50
	 * @param request
	 * @return
	 */
	@RequiresPermissions("salePropValLabel:delete")
	@RequestMapping("/salePropValLabel/delete")
	public String delete(HttpServletRequest request,Integer labelId){
		try {
			if(labelId == null){
				return jsonData(request,1, "必要参数不能为空");
			}
			salePropertyValueLabelDao.delete(labelId);
			return jsonData(request,0,"删除成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}

}