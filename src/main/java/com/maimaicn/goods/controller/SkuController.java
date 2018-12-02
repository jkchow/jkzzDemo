package com.maimaicn.goods.controller;

import com.maimaicn.goods.dao.SkuDao;
import com.maimaicn.goods.domain.*;
import com.maimaicn.goods.service.B2BWhiteListService;
import com.maimaicn.goods.utils.RestTemplateUtils;
import com.maimaicn.utils.JsonData;
import com.maimaicn.utils.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class SkuController extends BaseController{

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private B2BWhiteListService b2BWhiteListService;

    /**
     * 获取购物车的sku商品信息
     * @param request
     * @param skuId
     * @return
     */
    @RequestMapping("/sku/shoppingCartList")
    public String shoppingCartList(HttpServletRequest request,Integer[] skuId) {
        try{
            if(skuId == null || skuId.length == 0){
                return jsonData(request,1,"必要参数不能为空");
            }

            List<Map<String,Object>> list = skuDao.getShoppingCartList(skuId);

            return jsonData(request,0,list);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 获取添加购物车时需要的信息，这个sku的商品店铺id，店铺名称，添加时冗余，方便后期查询
     * @param request
     * @param skuId
     * @return
     */
    @RequestMapping("/sku/shoppingCartForAdd")
    public String shoppingCartForAdd(HttpServletRequest request,Integer skuId) {
        try{
            if(skuId == null){
                return jsonData(request,1,"必要参数不能为空");
            }

            Map<String,Object> data = skuDao.shoppingCartForAdd(skuId);

            return jsonData(request,0,data);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     * 清除购物车失效商品时，获取我的失效商品skuid
     * @param request
     * @param skuIds
     * @return
     */
    @RequestMapping("/sku/getDisabledSku")
    public String getDisabledSku(HttpServletRequest request,Integer [] skuIds) {
        try{
            if(skuIds == null || skuIds.length == 0){
                return jsonData(request,1,"必要参数不能为空");
            }

            List<Integer> list = skuDao.getDisabledSku(skuIds);

            return jsonData(request,0,list);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 获取订单确认页需要的商品信息，包括，商品的价格，商品参加的活动信息，运费信息
     * @param request
     * @param skuInfo skuId:num;skuId:num
     * @return
     */
    @RequestMapping("/sku/confirmOrderInfo")
    public String confirmOrderInfo(HttpServletRequest request,String skuInfo,Integer areaId) {
        try{
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,100,"登录超时");
            }
            if(StringUtils.isEmpty(skuInfo)){
                return jsonData(request,2,"必要参数不能为空");
            }
            if(areaId == null){//如果地址为空，就去获取这个会员的默认地址，默认地址也为空就返回错误
                return jsonData(request,2,"必要参数不能为空.");
            }

            //解析参数
            ArrayList<OrderSkuParams> paramsList = parseSkuInfo(skuInfo);

            //查询db信息
            List<MakeOrderSku> skusList = skuDao.getMakeOrderInfo(paramsList,areaId);

            //获取店铺信息，并按店铺信息分类封装
            Set<Long> shopMIdSet = new HashSet<>();
            for(MakeOrderSku mos : skusList){
                shopMIdSet.add(mos.getShopMId());
            }

            //获取商家的账户信息，平台红包，商家红包余额
            JsonData memberAccountInfo = RestTemplateUtils.getMemberAccountInfo(loginMemberId, shopMIdSet);
            Map<String,Object> accountMap = null;
            if(memberAccountInfo.getCode() == 0){
                accountMap = memberAccountInfo.getInfo(Map.class);
            }

            List<Map<String,Object>> shopList = new LinkedList<>();
            for(Long shopMId : shopMIdSet){
                Map<String,Object> oneShop = new HashMap<>();
                //获取店铺信息，logo，名称
                JsonData json = RestTemplateUtils.getShopInfo(shopMId);
                if(json.getCode() != 0){
                    return jsonData(request,4,json.getInfo());
                }
                Map<String,Object> shopName = json.getInfo(Map.class);
                oneShop.put("shopName",shopName.get("shopName"));
                oneShop.put("shopMId",shopMId);
                oneShop.put("logo",shopName.get("logo"));

                List<CommitOrderView> shopSkuList = new LinkedList<>();
                double totalFreight = 0;//店铺总运费
                double shopSubtotal = 0;//店铺需要支付的总金额
                for(MakeOrderSku mos : skusList){
                    if(shopMId.longValue() == mos.getShopMId().longValue()){
                        //最终显示的sku信息
                        CommitOrderView cov = new CommitOrderView(mos.getSkuId(), mos.getGoodsId(), mos.getTitle(), mos.getMainImg(), mos.getDeliveryTime(), mos.getSkuName(), mos.getNum(), mos.getPrice(), 0d,0d, mos.getSubtotal(), mos.getFreight(), (!Goods.STATUS_UP.equals(mos.getGoodsStatus()) || !Sku.STATUS_NORMAL.equals(mos.getStatus())) ? false : true);
                        //计算店铺总运费
                        totalFreight += mos.getFreight();
                        //计算店铺总费用，最后也就是订单的总计
                        double myRedDeduction = accountMap==null?0: (double) accountMap.get(shopMId.toString());
                        double mySellerRedDeduction = accountMap==null?0: (double) accountMap.get("platformRed");
                        if(myRedDeduction != 0){
                            cov.setRedDeduction(myRedDeduction > mos.getRedDeduction()*mos.getNum()?mos.getRedDeduction()*mos.getNum():myRedDeduction);
                            double thisSubtotal = mos.getSubtotal() - cov.getRedDeduction();
                            mos.setSubtotal(thisSubtotal);
                        }
                        if(mySellerRedDeduction != 0){
                            cov.setSellerRedDeduction(mySellerRedDeduction > mos.getSellerRedDeduction()*mos.getNum()?mos.getSellerRedDeduction()*mos.getNum():mySellerRedDeduction);
                            double thisSellerSubtotal = mos.getSubtotal() - cov.getSellerRedDeduction();
                            mos.setSubtotal(thisSellerSubtotal);
                        }
                        shopSubtotal += mos.getSubtotal();
                        shopSkuList.add(cov);
                    }
                }
                oneShop.put("totalFreight",totalFreight);
                oneShop.put("shopSubtotal",shopSubtotal + totalFreight);
                oneShop.put("shopSku",shopSkuList);

                shopList.add(oneShop);
            }

            return jsonData(request,0,shopList);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 下单需要的sku信息
     * @param request
     * @param skuInfo skuId:num;skuId:num
     * @param areaId
     * @return
     */
    @RequestMapping("/sku/makeOrderInfo")
    public String makeOrderInfo(HttpServletRequest request,String skuInfo,Integer areaId) {
        try{
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,100,"登录超时");
            }
            if(StringUtils.isEmpty(skuInfo)){
                return jsonData(request,2,"必要参数不能为空");
            }
            if(areaId == null){//如果地址为空，就去获取这个会员的默认地址，默认地址也为空就返回错误
                return jsonData(request,2,"必要参数不能为空.");
            }

            //解析参数
            ArrayList<OrderSkuParams> paramsList = parseSkuInfo(skuInfo);

            //查询db信息
            List<MakeOrderSku> skusList = skuDao.getMakeOrderInfo(paramsList,areaId);
            List<Map<String,Object>> resultList = new LinkedList<>();

            for(MakeOrderSku mos : skusList){
                if(!Goods.STATUS_UP.equals(mos.getGoodsStatus()) || !Sku.STATUS_NORMAL.equals(mos.getStatus())){
                    return jsonData(request,4,"商品 "+mos.getTitle() + " 已失效");
                }
                //判断库存
                if(mos.getStock() - mos.getNum() < 0){
                    return jsonData(request,5,"商品 "+mos.getTitle() + " 已失效");
                }
                //去掉不需要的字段，减小传输
                Map<String,Object> map = new HashMap<>();
                map.put("skuId",mos.getSkuId());
                map.put("goodsId",mos.getGoodsId());
                map.put("num",mos.getNum());
                map.put("shopMId",mos.getShopMId());
                map.put("freight",mos.getFreight());
                map.put("deliverTime",mos.getDeliveryTime());
                map.put("goodsTitle",mos.getTitle());
                map.put("skuImg",mos.getMainImg());
                StringBuilder sb = new StringBuilder();
                for(String s : mos.getSkuName()){
                    sb.append(s);
                    sb.append(" ; ");
                }
                map.put("skuName",sb.toString());

                if(b2BWhiteListService.isB2BBuyer(mos.getShopMId(),loginMemberId)){//如果买家是卖家的b2b的白名单，则按b2b价格结算
                    map.put("orderType","B2B");
                    map.put("price",mos.getB2bPrice());
                    map.put("sellerRedDeduction",0);
                    map.put("redDeduction",0);
                }else {
                    map.put("orderType","B2C");
                    map.put("price",mos.getPrice());
                    map.put("sellerRedDeduction",mos.getSellerRedDeduction());
                    map.put("redDeduction",mos.getRedDeduction());
                    map.put("shareRebate",mos.getShareRebate());
                }

                resultList.add(map);
            }

            return jsonData(request,0,resultList);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     * 下单减库存
     * @param request
     * @param skuInfo skuId:num;skuId:num
     * @return
     */
    @RequestMapping("/sku/subStock")
    public String subStock(HttpServletRequest request,String skuInfo,String pass) {
        try{
            ArrayList<OrderSkuParams> orderSkuParams = parseSkuInfo(skuInfo);
            if(orderSkuParams == null || orderSkuParams.size() == 0){
                return jsonData(request,1,"参数不合法");
            }
            if(!"159357".equals(pass)){
                return jsonData(request,2,"未授权的操作");
            }
            skuDao.subSkuStock(orderSkuParams);

            return jsonData(request,0,"减库存成功");
        } catch(DataIntegrityViolationException e){
            return jsonData(request,3,"减库存失败，库存不足");
        } catch(Exception e){
            log.error("减库存失败:"+skuInfo,e);
            return jsonData(request,110,"服务忙");

        }
    }

    /**
     * 回滚减库存操作，也就是加库存
     * @param request
     * @param skuInfo
     * @param pass
     * @return
     */
    @RequestMapping("/sku/addStock")
    public String addStock(HttpServletRequest request,String skuInfo,String pass) {
        try{
            ArrayList<OrderSkuParams> orderSkuParams = parseSkuInfo(skuInfo);
            if(orderSkuParams == null || orderSkuParams.size() == 0){
                return jsonData(request,1,"参数不合法");
            }
            if(!"147852".equals(pass)){
                return jsonData(request,2,"未授权的操作");
            }
            skuDao.addStock(orderSkuParams);

            return jsonData(request,0,"库存添加成功");
        } catch(Exception e) {
            log.error("异常", e);
            return jsonData(request, 110, "服务忙");
        }
    }


    //-----------------------------private-------------------------------

    /**
     * 解析sku参数字符串
     * @param skuInfo
     * @return
     * @throws Exception
     */
    private ArrayList<OrderSkuParams> parseSkuInfo(String skuInfo) throws Exception {
        ArrayList<OrderSkuParams> paramsList = new ArrayList<>();
        String[] skus = skuInfo.split(";");
        for(int i = 0, j = skus.length ; i < j; i++){
            if(!StringUtils.isEmpty(skus[i])){
                String[] one_sku = skus[i].split(":");
                if(one_sku.length != 2){
                    throw new Exception("参数不合法.");
                }
                OrderSkuParams cop = new OrderSkuParams(Integer.parseInt(one_sku[0]),Integer.parseInt(one_sku[1]));
                if(cop.getSkuId() == null || cop.getNum() == null){
                    throw new Exception("参数不合法");
                }
                paramsList.add(cop);
            }
        }

        return paramsList;
    }


}
