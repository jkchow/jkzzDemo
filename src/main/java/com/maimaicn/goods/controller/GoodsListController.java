package com.maimaicn.goods.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimaicn.goods.dao.CategoryDao;
import com.maimaicn.goods.dao.GoodsListDao;
import com.maimaicn.goods.domain.Goods;
import com.maimaicn.goods.domain.UpdateSkuPojo;
import com.maimaicn.goods.utils.SendMQUtils;
import com.maimaicn.utils.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.params.Params;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class GoodsListController extends BaseController {

    @Autowired
    private GoodsListDao goodsListDao;
    @Autowired
    private CategoryDao categoryDao;

    /**
     * 商家获取商品列表
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/goodsList")
    public String goodsList(HttpServletRequest request,Integer goodsId,String status,String goodsName,Integer pageNo,Integer pageSize,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date uploadTime) {
        try{
            if(!StringUtils.isEmpty(status) && !Goods.STATUS_UP.equals(status) && !Goods.STATUS_DOWN.equals(status)){
                return jsonData(request,1,"状态错误");
            }
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,100,"登录超时");
            }

            if(pageNo == null) pageNo = 1;
            if(pageSize == null) pageSize = 5;

            List<Map<String,Object>> list = goodsListDao.goodsList(pageNo,pageSize,loginMemberId,goodsId,status,goodsName,uploadTime);

            return jsonData(request,0,list);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 获取商品的sku信息，用于修改
     * @param request
     * @param goodsId
     * @return
     */
    @RequestMapping("/goodsList/skuInfo")
    public String skuForUpdate(HttpServletRequest request, Integer goodsId) {
        try{
            if(goodsId == null){
                return jsonData(request,1,"必要参数不能为空");
            }
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,100,"登录超时");
            }

            List<Map<String, Object>> skuUpdateInfo = goodsListDao.getSkuUpdateInfo(goodsId, loginMemberId);
            if(skuUpdateInfo == null){
                return jsonData(request,2,"没有找到这个商品的sku信息");
            }

            return jsonData(request,0,skuUpdateInfo);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     * 修改商品的sku信息
     * @param request
     * @return
     */
    @RequestMapping("/goodsList/updateSku")
    public String updateSku(HttpServletRequest request,String updateJson,Integer goodsId) {
        try{
            if(StringUtils.isEmpty(updateJson) || goodsId == null){
                return jsonData(request,1,"必要参数不能为空");
            }
            Long loginMemberId = this.getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,110,"登录超时");
            }
            ObjectMapper om = new ObjectMapper();
            List<Map<String,Object>> list = om.readValue(updateJson, List.class);

            List<UpdateSkuPojo> updateList = new ArrayList<>();
            for(Map<String,Object> map : list ){
                UpdateSkuPojo usp = new UpdateSkuPojo(map.get("skuId") == null ? null : (Integer) map.get("skuId"),
                        map.get("stock") == null ? null : (Integer) map.get("stock"),
                        map.get("b2cp") == null ? null : Double.parseDouble(map.get("b2cp") + ""),
                        map.get("b2bp") == null ? null : Double.parseDouble(map.get("b2bp") + ""),
                        map.get("rd") == null ? null : Double.parseDouble(map.get("rd") + ""),
                        map.get("srd") == null ? null : Double.parseDouble(map.get("srd") + ""),
                        map.get("sr") == null ? null : Double.parseDouble(map.get("sr") + ""));
                if(usp.getStock()==null && usp.getB2cp()==null && usp.getB2bp()==null && usp.getRd()==null &&
                        usp.getSrd() == null && usp.getSr()==null){
                    return jsonData(request,2,"更新参数不能为空");
                }
                updateList.add(usp);
            }

            goodsListDao.updateSku(updateList,loginMemberId,goodsId);

            return jsonData(request,0,"更新成功");
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     * 删除一组商品，逻辑删除
     * @param request
     * @param goodsId
     * @return
     */
    @RequestMapping("/goodsList/delete")
    public String deleteGoods(HttpServletRequest request,Integer [] goodsId) {
        try{
            if(goodsId == null || goodsId.length == 0){
                return jsonData(request,1,"必要参数不能为空");
            }
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,100,"登录超时");
            }

            int update = goodsListDao.delete(loginMemberId,goodsId);

            if(update != 0){
                //更新solr信息
                for(int i = 0, j=goodsId.length ; i < j; i++){
                    SendMQUtils.sendUnloadSolrGoodsJms(goodsId[i]);
                }
                return jsonData(request,0,"删除成功");
            }else {
                return jsonData(request,2,"删除失败");
            }
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 上下架商品
     * @param request
     * @param goodsId
     * @param status 1-上架商品，2-下架商品
     * @return
     */
    @RequestMapping("/goodsList/upDown")
    public String upDown(HttpServletRequest request,Integer goodsId,Integer status) {
        try{
            if(goodsId == null || status == null){
                return jsonData(request,1,"必要参数不能为空");
            }
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,100,"登录超时");
            }

            String goods_status ;
            if(status == 1){
                goods_status = Goods.STATUS_UP;
            }else if(status == 2){
                goods_status = Goods.STATUS_DOWN;
            }else {
                return jsonData(request,2,"状态错误");
            }

            int update = goodsListDao.updateStatus(loginMemberId,goodsId, goods_status);

            //更新solr信息
            if(status == 1){
                SendMQUtils.sendPublishSolrGoodsJms(goodsId);
            }else if(status == 2){
                SendMQUtils.sendUnloadSolrGoodsJms(goodsId);
            }

            if(update != 0){
                return jsonData(request,0,"状态更新成功");
            }else {
                return jsonData(request,3,"状态更新失败");
            }
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 根据条件获取商品列表
     * @param request
     * @param pageNo
     * @param pageSize
     * @author wxt
     * @return
     */
    @RequestMapping("/goodsListByCategory")
    public String goodsListByCategory(HttpServletRequest request, Integer pageNo, Integer pageSize, Integer categoryId, @RequestParam(value = "goodsIds[]",required = false)Integer [] goodsIds, String  goodsName) {
        try{
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,1,"登录超时");
            }

            if(pageNo == null) pageNo = 1;
            if(pageSize == null) pageSize = 20;
            if(categoryId>=3128){
                categoryId=null;
            }
            Map<String,Object> params = new HashMap<>();
            params.put("memberId",loginMemberId);
            params.put("categoryId",categoryId);
            params.put("goodsIds",goodsIds);
            params.put("goodsName",goodsName);

            List<Map<String,Object>> list = goodsListDao.goodsListByCategory(pageNo,pageSize,params);

            return jsonData(request,0,list);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }
}
