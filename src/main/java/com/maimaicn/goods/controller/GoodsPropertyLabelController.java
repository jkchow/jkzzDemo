package com.maimaicn.goods.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.maimaicn.goods.dao.GoodsPropertyLabelDao;
import com.maimaicn.goods.domain.GoodsPropertyLabel;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.controller.BaseController;
import com.maimaicn.utils.permission.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 
 * @类描述 
 * @创建人  张进
 * @2017-11-2 16:38:18
 */
@RestController
public class GoodsPropertyLabelController extends BaseController {
	@Autowired
	GoodsPropertyLabelDao goodsPropertyLabelDao;


	/**
	 * @方法描述 根据分类id获取该分类以及该分类的上级分类的所以属性标签
	 * @author 张进
	 * @2017年11月2日 下午4:40:10
	 * @param request
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/goodsPropertyLabel/indexData")
	public String indexData(HttpServletRequest request, Integer page ,Integer rows ,Integer categoryId,String labelName){
		try {
			PageVO<Map<String,Object>> pageVO = goodsPropertyLabelDao.indexData(page,rows,categoryId,labelName);

			Map<String ,Object> result = new HashMap<String,Object>();
			result.put("total", pageVO.getRecordCount());
			result.put("rows", pageVO.getRecordList());
			return jsonData(request, 0,result);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 根据分类id获取属性标签的下拉框数据，包含该分类的所有上级的所有属性标签
	 * @author 张进
	 * @2017年11月2日 下午5:34:58
	 * @param request
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/goodsPropertyLabel/getComboboxData")
	public String getComboboxData(HttpServletRequest request,Integer categoryId){
		try {
			if(categoryId == null){
				return jsonData(request, 1,"必要参数不能为空");
			}
			List<Map<String,Object>> list = goodsPropertyLabelDao.getComboboxData(categoryId);
			return jsonData(request, 0,list);
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 新增属性标签
	 * @author 张进
	 * @2017年11月2日 下午4:44:58
	 * @param request
	 * @param gpl
	 * @return
	 */
	@RequiresPermissions("goodsPropertyLabel:add")
	@RequestMapping("/goodsPropertyLabel/add")
	public String add(HttpServletRequest request,GoodsPropertyLabel gpl){
		try {
			if(gpl.getLabelName() == null || "".equals(gpl.getLabelName()) || gpl.getCategoryId() == null){
				return jsonData(request, 1,"必要参数不能为空");
			}
			goodsPropertyLabelDao.save(gpl);
			return jsonData(request,0,"新增属性标签成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 修改属性标签
	 * @author 张进
	 * @2017年11月2日 下午4:45:42
	 * @param request
	 * @param gpl
	 * @return
	 */
	@RequiresPermissions("goodsPropertyLabel:update")
	@RequestMapping("/goodsPropertyLabel/update")
	public String update(HttpServletRequest request,GoodsPropertyLabel gpl){
		try {
			if(gpl.getLabelName() == null || "".equals(gpl.getLabelName()) || gpl.getLabelId()==null){
				return jsonData(request, 1,"必要参数不能为空");
			}
//			gpl.setCategoryId(null);//分类不能修改
			goodsPropertyLabelDao.update(gpl);
			return jsonData(request,0,"修改属性标签成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request,110, "服务忙");
		}
	}
	
	/**
	 * @方法描述 删除属性标签
	 * @author 张进
	 * @2017年11月2日 下午4:43:34
	 * @param request
	 * @return
	 */
	@RequiresPermissions("goodsPropertyLabel:delete")
	@RequestMapping("/goodsPropertyLabel/delete")
	public String delete(HttpServletRequest request,Integer labelId){
		try {
			if(labelId == null){
				return jsonData(request,1, "必要参数不能为空");
			}
			goodsPropertyLabelDao.delete(labelId);
			return jsonData(request,0,"删除成功");
		} catch (Exception e) {
			log.error("服务忙", e);
			return jsonData(request, 110,"服务忙");
		}
	}

}