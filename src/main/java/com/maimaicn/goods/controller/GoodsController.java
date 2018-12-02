package com.maimaicn.goods.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimaicn.goods.dao.GoodsDao;
import com.maimaicn.goods.dao.PromiseServiceDao;
import com.maimaicn.goods.dao.SalePropertyDao;
import com.maimaicn.goods.domain.*;
import com.maimaicn.goods.utils.SendMQUtils;
import com.maimaicn.utils.PageVO;
import com.maimaicn.utils.RedisLockUtils;
import com.maimaicn.utils.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class GoodsController extends BaseController {

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private PromiseServiceDao promiseServiceDao;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 上传商品
     * @param request
     * @param goods
     * @param promSerIds
     * @param goodsProps
     * @param sku
     * @param detailImgs
     * @return
     */
    @CrossOrigin(allowCredentials="true",origins = "*")
    @RequestMapping("/save")
    public String save(HttpServletRequest request,Goods goods,@RequestParam(value = "promSerIds[]",required = false) Integer[] promSerIds
                       ,String goodsProps,String sku,String detailImgs) {
        Long loginMemberId = getLoginMemberId(request);
        if(loginMemberId == null){
            return jsonData(request,100,"登录超时");
        }
        //加锁，以会员id为锁，同一会员，同一时间只能上传一个商品
        if(!RedisLockUtils.lock(redisTemplate, loginMemberId.toString())){
            return jsonData(request,2,"正在上传");
        }

        try{
            //1.商品基本参数校验
            if(goods.getCategoryId() == null || goods.getFreightId() == null || goods.getStartType() == null || goods.getSubStockType() == null
                    || StringUtils.isEmpty(goods.getTitle()) || StringUtils.isEmpty(goods.getMainImg()) || StringUtils.isEmpty(goods.getWhiteImg()) || StringUtils.isEmpty(goods.getAlbumImgs())
                    || StringUtils.isEmpty(sku)){
                return jsonData(request,3,"必要参数不能为空");
            }

            //2.封装商品基本属性list pojo
            ObjectMapper om = new ObjectMapper();
            ArrayList<Map> props = om.readValue(goodsProps, ArrayList.class);
            List<GoodsPropertyData> props_list = new ArrayList<>();
            for(Map map : props){
                int propertyId = new Integer(String.valueOf(map.get("propertyId")));// Integer.parseInt((String) map.get("propertyId"));
                String valueJson = om.writeValueAsString(map.get("valueJson"));
                GoodsPropertyData gpd = new GoodsPropertyData(propertyId,(String) map.get("propertyName"),(String) map.get("valueName"),valueJson);

                if(propertyId == -1){//在我们的定义里propertyId=-1的就是品牌id,取出品牌id放到商品基本属性中去
                    goods.setBrandId(new Integer(String.valueOf(om.readValue(gpd.getValueJson(),Map.class).get("vId"))));
                }

                props_list.add(gpd);
            }

            //3.封装校验sku信息
            List<SalePropForVerity> salePropList = goodsDao.getSkuPropForVerify(goods.getCategoryId());//校验sku属性用
            ArrayList<Map> skus = om.readValue(sku, ArrayList.class);
            List<Sku> sku_list = new ArrayList<>();
            int customSkuValue = -1;
            for(Map map : skus){
                Double price = Double.parseDouble((String) map.get("price"));
                Integer stock = Integer.parseInt((String) map.get("stock"));
                Double sr = Double.parseDouble((String) map.get("sr"));//分享返佣
                Double rd = Double.parseDouble((String) map.get("rd"));//分享返佣
                Double srd = Double.parseDouble((String) map.get("srd"));//分享返佣
                if(price == null || price == 0 || stock == null ||stock == 0 || sr == null|| rd == null|| srd == null){
                    return jsonData(request,4,"sku价格/库存不能为空");
                }

                //校验sku属性
                List<Map<String,Object>> skuProperties = (List<Map<String,Object>>) map.get("skuProperties");
                List<SkuProperty> skuPropList = new ArrayList<>();
                for(Map<String,Object> sp_map : skuProperties){
                    SkuProperty sp = om.readValue(om.writeValueAsString(sp_map), SkuProperty.class);
                    int pId = sp.getpId();
                    for(SalePropForVerity spfv : salePropList){
                        if(pId == spfv.getPropertyId()){
                            if(SaleProperty.ISREMARK_NO.equals(spfv.getIsRemark()) && !StringUtils.isEmpty(sp.getR())){//不允许备注，然而有备注值，就清空备注值
                                sp.setR("");
                            }
                            if(SaleProperty.NEEDPROPERTYPIC_NO.equals(spfv.getNeedPropertyPic()) && !StringUtils.isEmpty(sp.getImg())){//不允许sku图片，然而有sku图片，就清空sku图片
                                sp.setImg("");
                            }
                            if(SaleProperty.CANCUSTOMVALUE_NO.equals(spfv.getCanCustomValue()) && sp.getvId() < 0){
                                return jsonData(request,5,"sku属性错误");
                            }
                            if(sp.getvId() < 0){
                                sp.setvId(customSkuValue--);
                            }
                            skuPropList.add(sp);
                            break;
                        }
                    }
                }

                sku_list.add(new Sku(om.writeValueAsString(skuPropList),price,stock,(String)map.get("outerId"),(String)map.get("barcode"),sr,rd,srd));
            }

            //4.校验售后服务信息，要确保提交的服务是该商家加入的服务且有效的服务。
            String promiseServiceIds = promiseServiceDao.getThisGoodsPromiseIds(promSerIds,goods.getCategoryId(),loginMemberId);
            goods.setPromiseServiceIds(promiseServiceIds);
            goods.setMemberId(loginMemberId);

            //5.校验该商品是否重复上传，校验标准：5张相册图是否一样、
            Integer goodsId = goods.getGoodsId();
            if(goodsId == null){//商品上传操作
                if(goodsDao.isRepeatUpload(loginMemberId,goods.getMainImg(),goods.getWhiteImg())){
                    return jsonData(request,5,"商品重复上传");
                }
                goodsId = goodsDao.saveGoods(goods,props_list,sku_list,detailImgs);
            }else {//商品修改操作
                goodsDao.updateGoods(goods,props_list,sku_list,detailImgs);
            }

            if(goods.getStartType() == 1){//立刻上架,发送商品上架消息，发布到solr中
                SendMQUtils.sendPublishSolrGoodsJms(goodsId);
            }

            return jsonData(request,0,"商品保存成功");
        } catch(Exception e){
            log.error("保存商品异常",e);
            return jsonData(request,110,"服务忙");
        }finally {
            //释放锁
            RedisLockUtils.unlock(redisTemplate,loginMemberId.toString());
        }
    }


    /**
     * 修改商品时，获取回显数据
     * @param request
     * @param goodsId
     * @return
     */
    @RequestMapping("/getForUpdate")
    public String getForUpdate(HttpServletRequest request,Integer goodsId) {
        try{
            Long loginMemberId = this.getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,1,"登录超时");
            }
            if(goodsId == null){
                return jsonData(request,1,"必要参数不能为空");
            }

            Map<String, Object> forUpdate = goodsDao.getForUpdate(goodsId);

            return jsonData(request,0,forUpdate);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 获取商品的基本信息
     * @param request
     * @param goodsId
     * @return
     */
    @RequestMapping("/goodsInfo")
    public String goodsInfo(HttpServletRequest request,@RequestParam(value = "goodsId") Integer[] goodsId) {
        try{
            if(goodsId == null || goodsId.length == 0){
                return jsonData(request,1,"必要参数不能为空");
            }
            List<Map<String,Object>> list = goodsDao.getGoodsInfo(goodsId);

            return jsonData(request,0,list);
        } catch(Exception e){
            log.error("获取商品信息异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     * 获取商品图库(添加广告源时所需)
     * @param request
     * @param imgName  图片名称
     * @param categoryId 商品分类
     * @return
     */
    @RequestMapping("/getGoodsImgStorage")
    public String goodsImageStorage(HttpServletRequest request,String imgName,Integer categoryId,Integer pageNo, Integer pageSize) {
        try{
            Long loginMemberId = this.getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,1,"登录超时");
            }
            if(pageNo == null) pageNo = 1;
            if(pageSize == null) pageSize = 15;
            Map<String,Object> map= goodsDao.getGoodsImgStorage(loginMemberId,imgName,categoryId,pageNo,pageSize);
            return jsonData(request,0,map);
        } catch(Exception e){
            log.error("获取商品图库异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
      * @描述 根据ID获取商品基本信息
    　* @author wxt
    　* @date 2018/11/12 14:54
    　*/
    @RequestMapping("/getAllGoodsInfo")
    public String getAllGoodsInfo(HttpServletRequest request,Integer goodsId){
        try{
            if(goodsId == null){
                return jsonData(request,1,"必要参数不能为空");
            }
            Map<String,Object> map = goodsDao.getAllGoodsInfo(goodsId);
            return jsonData(request,0,map);
        } catch(Exception e){
            log.error("获取商品信息异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

}
