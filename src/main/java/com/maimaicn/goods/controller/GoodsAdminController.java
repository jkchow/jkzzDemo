package com.maimaicn.goods.controller;

import com.maimaicn.goods.configurations.interceptor.AuthLogin;
import com.maimaicn.goods.dao.CategoryDao;
import com.maimaicn.goods.dao.GoodsListDao;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.controller.BaseController;
import com.maimaicn.utils.permission.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GoodsAdminController extends BaseController {
    @Autowired
    private GoodsListDao goodsListDao;
    @Autowired
    private CategoryDao categoryDao;

    /**
     * 后台获取全部商品列表
     *
     * @param request
     * @param page
     * @param rows
     * @return
     */
    @AuthLogin
    @RequiresPermissions("goodsAdminList:goodsList")
    //@RequiresPermissions("category:index")
    @RequestMapping("goodsAdmin/goodsList")
    public String goodsList(HttpServletRequest request, Integer page, Integer rows,String memberId,String goodsId,String startType,String status) {
        try {
            if(page == null) page = 1;
            if(rows == null) rows = 20;
            page=page<1?1:page;
            Map<String, Object> params = new HashMap<>();
            if (memberId !=null && ! memberId.trim().equals("")) {
                params.put("memberId",memberId);
            }
            if (goodsId !=null && ! goodsId.trim().equals("")) {
                params.put("goodsId",goodsId);
            }
            if (startType !=null ) {
                params.put("startType",startType);
            }
            if (status !=null ) {
                params.put("status",status);
            }
            PageVO<Map<String, Object>> pagevo = goodsListDao.goodsAdminList(page,rows,params);
            params.clear();
            params.put("total", pagevo.getRecordCount());
            params.put("rows", pagevo.getRecordList());
            return jsonData(request, 0,params);
        } catch (Exception e) {
            log.error("服务忙", e);
            return jsonData(request,110, "服务忙");
        }
    }
    @RequiresPermissions("goodsAdminList:goodsDetail")
    @RequestMapping("goodsAdmin/goodsDetail")
    public String goodsDetail(HttpServletRequest request,Integer goodsId) {
        try {
            if (goodsId ==null ) {
                return jsonData(request,110, "必要参数不能为空");
            }
            Map<String, Object> map = goodsListDao.goodsAdminDetail(goodsId);

            return jsonData(request, 0,map);
        } catch (Exception e) {
            log.error("服务忙", e);
            return jsonData(request,110, "服务忙");
        }
    }

}
