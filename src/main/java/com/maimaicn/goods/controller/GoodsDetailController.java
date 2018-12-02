package com.maimaicn.goods.controller;

import com.maimaicn.goods.dao.GoodsDetailDao;
import com.maimaicn.goods.utils.RestTemplateUtils;
import com.maimaicn.utils.JsonData;
import com.maimaicn.utils.RedisLockUtils;
import com.maimaicn.utils.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 商品详情控制类
 */
@RestController
public class GoodsDetailController extends BaseController{

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsDetailDao goodsDetailDao;

    /**
     * 获取商品详情的主要信息，标题，主图，白底图，相册图，商品状态，已售数，收藏数
     * @param request
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/main")
    public String staticInfo(HttpServletRequest request,Integer goodsId) {
        try{
            if(goodsId == null){
                return jsonData(request,1,"必要参数不能为空");
            }
            Object main_info = redisTemplate.opsForValue().get("detail:main:" + goodsId);

            //如果有缓存，就直接返回
            if(main_info != null){
                return jsonData(request,0,main_info);
            }

            //没有缓存，就查db层。
            boolean lock = RedisLockUtils.lock(redisTemplate, "detail:main:lock:" + goodsId);
            if(!lock){//获取锁不成功，就代表有人去加载db数据了，就自旋取缓存
                int count = 1;
                while (main_info == null && count < 60){//最多自旋100次，避免死循环
                    Thread.sleep(200);
                    main_info = redisTemplate.opsForValue().get("detail:main:" + goodsId);
                    count ++;
                }
                if(main_info == null){
                    log.error("获取商品主信息错误，自旋60次未获取数据，goodsId="+goodsId);
                    return jsonData(request,2,"没有找到这个商品。");
                }
                return jsonData(request,0,main_info);
            }else {
                try {
                    //成功获取锁，加载db数据
                    main_info = goodsDetailDao.getDetailMainInfo(goodsId);
                    if(main_info == null){
                        redisTemplate.opsForValue().set("detail:main:" + goodsId,main_info,5, TimeUnit.MINUTES);
                        return jsonData(request,2,"没有找到这个商品");
                    }else {
                        Map<String,Object> detailInfo = ((Map<String,Object>)main_info);
                        JsonData smId = RestTemplateUtils.getShopInfo((Long) detailInfo.get("smId"));
                        if(smId.getCode() == 0){
                            detailInfo.putAll(smId.getInfo(Map.class));
                        }else {
                            log.error("商品详情获取店铺数据错误："+smId.getInfo());
                            detailInfo.put("shopName","");
                            detailInfo.put("logo","");
                            detailInfo.put("isPledge","no");
                        }
                        redisTemplate.opsForValue().set("detail:main:" + goodsId,detailInfo,2, TimeUnit.HOURS);
                        return jsonData(request,0,detailInfo);
                    }
                }finally {
                    RedisLockUtils.unlock(redisTemplate,"detail:main:lock:" + goodsId);
                }
            }
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 商品详情页，获取商品sku信息
     * @param request
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/sku")
    public String skuInfo(HttpServletRequest request,Integer goodsId) {
        try{
            if(goodsId == null){
                return jsonData(request,1,"必要参数不能为空");
            }
            Object sku_info = redisTemplate.opsForValue().get("detail:sku:" + goodsId);

            //如果有缓存，就直接返回
            if(sku_info != null){
                return jsonData(request,0,sku_info);
            }

            //没有缓存，就查db层。
            boolean lock = RedisLockUtils.lock(redisTemplate, "detail:sku:lock:" + goodsId);
            if(!lock){//获取锁不成功，就代表有人去加载db数据了，就自旋取缓存
                int count = 1;
                while (sku_info == null && count < 100){//最多自旋100次，避免死循环
                    Thread.sleep(200);
                    sku_info = redisTemplate.opsForValue().get("detail:sku:" + goodsId);
                    count ++;
                }
                if(sku_info == null){
                    log.error("获取商品主信息错误，自旋100次未获取数据，goodsId="+goodsId);
                    return jsonData(request,2,"没有找到这个商品的sku。");
                }
                return jsonData(request,0,sku_info);
            }else {
                try{
                    //成功获取锁，加载db数据
                    sku_info = goodsDetailDao.getSkuInfo(goodsId);
                    if(sku_info == null){
                        redisTemplate.opsForValue().set("detail:sku:" + goodsId,sku_info,5, TimeUnit.MINUTES);
                        return jsonData(request,2,"没有找到这个商品的sku");
                    }else {
                        redisTemplate.opsForValue().set("detail:sku:" + goodsId,sku_info,30, TimeUnit.MINUTES);
                        return jsonData(request,0,sku_info);
                    }
                }finally {
                    RedisLockUtils.unlock(redisTemplate,"detail:sku:lock:" + goodsId);
                }
            }
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 商品详情也获取商品属性参数信息
     * @param request
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/prop")
    public String GoodsProperties(HttpServletRequest request,Integer goodsId) {
        try{
            if(goodsId == null){
                return jsonData(request,1,"必要参数不能为空");
            }
            Object prop_info = redisTemplate.opsForValue().get("detail:prop:" + goodsId);

            //如果有缓存，就直接返回
            if(prop_info != null){
                return jsonData(request,0,prop_info);
            }
            //没有缓存，就查db层。
            boolean lock = RedisLockUtils.lock(redisTemplate, "detail:prop:lock:" + goodsId);
            if(!lock){//获取锁不成功，就代表有人去加载db数据了，就自旋取缓存
                int count = 1;
                while (prop_info == null && count < 60){//最多自旋100次，避免死循环
                    Thread.sleep(200);
                    prop_info = redisTemplate.opsForValue().get("detail:prop:" + goodsId);
                    count ++;
                }
                if(prop_info == null){
                    log.error("获取商品属性参数信息错误，自旋60次未获取数据，goodsId="+goodsId);
                    return jsonData(request,2,"没有找到这个商品的信息");
                }
                return jsonData(request,0,prop_info);
            }else {
                try {
                    //成功获取锁，加载db数据
                    prop_info = goodsDetailDao.getDetailProperties(goodsId);
                    if(prop_info == null){
                        redisTemplate.opsForValue().set("detail:prop:" + goodsId,prop_info,5, TimeUnit.MINUTES);
                        return jsonData(request,2,"没有找到这个商品的信息");
                    }else {
                        redisTemplate.opsForValue().set("detail:prop:" + goodsId,prop_info,2, TimeUnit.HOURS);
                        return jsonData(request,0,prop_info);
                    }
                }finally {
                    RedisLockUtils.unlock(redisTemplate,"detail:prop:lock:" + goodsId);
                }
            }
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 商品详情描述，图片列表
     * @param request
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/desc")
    public String GoodsDetailImg(HttpServletRequest request,Integer goodsId) {
        try{
            if(goodsId == null){
                return jsonData(request,1,"必要参数不能为空");
            }
            Object desc_info = redisTemplate.opsForValue().get("detail:description:" + goodsId);

            //如果有缓存，就直接返回
            if(desc_info != null){
                return jsonData(request,0,desc_info);
            }
            //没有缓存，就查db层。
            boolean lock = RedisLockUtils.lock(redisTemplate, "detail:description:lock:" + goodsId);
            if(!lock){//获取锁不成功，就代表有人去加载db数据了，就自旋取缓存
                int count = 1;
                while (desc_info == null && count < 60){//最多自旋100次，避免死循环
                    Thread.sleep(200);
                    desc_info = redisTemplate.opsForValue().get("detail:description:" + goodsId);
                    count ++;
                }
                if(desc_info == null){
                    log.error("获取商品详情页信息错误，自旋60次未获取数据，goodsId="+goodsId);
                    return jsonData(request,2,"没有找到这个商品。");
                }
                return jsonData(request,0,desc_info);
            }else {
                try {
                    //成功获取锁，加载db数据
                    desc_info = goodsDetailDao.getGoodsDescriptionImg(goodsId);
                    if(desc_info == null){//空值只缓存5分钟
                        redisTemplate.opsForValue().set("detail:description:" + goodsId,desc_info,5, TimeUnit.MINUTES);
                        return jsonData(request,2,"没有找到这个商品");
                    }else{
                        redisTemplate.opsForValue().set("detail:description:" + goodsId,desc_info,2, TimeUnit.HOURS);
                        return jsonData(request,0,desc_info);
                    }
                }finally {
                    RedisLockUtils.unlock(redisTemplate,"detail:description:lock:" + goodsId);
                }
            }
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 获取详情也店铺信息，包括店铺名称，logo，是否有质保协议
     * @param request
     * @return
     */
    @RequestMapping("/detail/shopInfo")
    public String shopInfo(HttpServletRequest request,Long smId) {
        try{
            if(smId == null){
                return jsonData(request,1,"必要参数不能为空");
            }

            Object shop_info = redisTemplate.opsForValue().get("detail:shopInfo:" + smId);

            //如果有缓存，就直接返回
            if(shop_info != null){
                return jsonData(request,0,shop_info);
            }
            //没有缓存，就查db层。
            boolean lock = RedisLockUtils.lock(redisTemplate, "detail:shopInfo:lock:" + smId);
            if(!lock){//获取锁不成功，就代表有人去加载db数据了，就自旋取缓存
                int count = 1;
                while (shop_info == null && count < 60){//最多自旋100次，避免死循环
                    Thread.sleep(200);
                    shop_info = redisTemplate.opsForValue().get("detail:shopInfo:" + smId);
                    count ++;
                }
                if(shop_info == null){
                    log.error("获取商品详情页店铺信息错误，自旋60次未获取数据，goodsId="+ smId);
                    return jsonData(request,2,"没有找到这个店铺。");
                }
                return jsonData(request,0,shop_info);
            }else {
                try {
                    //成功获取锁，加载db数据
                    shop_info = goodsDetailDao.getShopInfo(smId);
                    if(shop_info == null){//空值只缓存5分钟
                        redisTemplate.opsForValue().set("detail:shopInfo:" + smId,shop_info,5, TimeUnit.MINUTES);
                        return jsonData(request,2,"没有找到这个店铺");
                    }else{
                        redisTemplate.opsForValue().set("detail:shopInfo:" + smId,shop_info,2, TimeUnit.HOURS);
                        return jsonData(request,0,shop_info);
                    }
                }finally {
                    RedisLockUtils.unlock(redisTemplate,"detail:shopInfo:lock:" + smId);
                }
            }
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }
}
