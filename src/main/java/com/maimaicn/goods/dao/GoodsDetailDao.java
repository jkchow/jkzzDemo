package com.maimaicn.goods.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimaicn.goods.domain.Goods;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品详情页相关dao操作
 */
@Repository
public class GoodsDetailDao {

    private static final String ns = "mappers.GoodsDetailMapper.";

    @Autowired
    private SqlSession sqlSession;


    /**
     * 在商品详情页中，获取商品主要信息，包含标题，主图，白底图，相册图，商品状态，已售数，收藏数
     * @param goodsId
     * @return
     */
    public Map<String,Object> getDetailMainInfo(Integer goodsId) {
        Map<String,Object> main = sqlSession.selectOne(this.ns+"getDetailMainInfo",goodsId);
        if(main == null){
            return null;
        }
        if(main.get("slogan") == null){
            main.put("slogan","");
        }
        if(main.get("sellPoint") == null){
            main.put("sellPoint","");
        }
        if(!Goods.STATUS_UP.equals(main.get("status"))){
            main.put("status","disabled");
        }else {
            main.put("status","enable");
        }

        Map<String,Object> sales_collects = sqlSession.selectOne(this.ns+"getSalesCollect",goodsId);
        if(sales_collects == null){
            main.put("collects",0);
            main.put("sales",0);
        }else {
            main.putAll(sales_collects);
        }

        if(main.get("albumImgs") != null){
            main.put("albumImgs",((String)main.get("albumImgs")).split("\\|"));
        }

        //获取商品加入的服务承诺信息
        if(main.get("promiseServiceIds") != null){
            String[] promiseServiceIds = ((String) main.get("promiseServiceIds")).split(",");
            List<Object> services = sqlSession.selectList(this.ns+"getDetailService",promiseServiceIds);
            main.put("services",services);
            main.remove("promiseServiceIds");
        }

        return main;
    }

    /**
     * 获取商品详情也得数据，商品的属性参数
     * @param goodsId
     * @return
     */
    public List<Map<String,Object>> getDetailProperties(Integer goodsId) {
        return sqlSession.selectList(this.ns+"getDetailProperties",goodsId);
    }


    /**
     * 获取商品详情页，详情描述信息，图片列表
     * @param goodsId
     * @return
     */
    public ArrayList getGoodsDescriptionImg(Integer goodsId) throws IOException {
        String details = sqlSession.selectOne(this.ns+"getGoodsDescriptionImg",goodsId);
        if(StringUtils.isEmpty(details)){
            return null;
        }
        ObjectMapper om = new ObjectMapper();
        return om.readValue(details, ArrayList.class);
    }

    /**
     * 获取商品详情页sku信息
     * @param goodsId
     * @return
     */
    public Object getSkuInfo(Integer goodsId) throws IOException {
        List<Map<String,Object>> skus = sqlSession.selectList(this.ns + "getSkuInfo", goodsId);
        if(skus.size() == 0){
            return null;
        }
        ObjectMapper om = new ObjectMapper();

        for(Map<String,Object> sku : skus){
            sku.put("skuInfo",om.readValue((String)sku.get("skuInfo"),ArrayList.class));
        }

        //取出sku属性名称的集合
        ArrayList<Map<String,Object>> skuInfo = (ArrayList) skus.get(0).get("skuInfo");
        Integer [] sku_props = new Integer[skuInfo.size()];
        int i = 0;
        for(Map<String,Object> map : skuInfo){
            sku_props[i++] = (Integer) map.get("pId");
        }

        List<Map<String,Object>> prop_list = sqlSession.selectList(this.ns+"getSkuPropNames",sku_props);

        Map<String,Object> reslut = new HashMap<>();
        reslut.put("sku",skus);
        reslut.put("skuName",prop_list);
        return reslut;
    }

    /**
     * 详情页获取店铺信息，包括店铺logo，名称，是否质保
     * @param smId
     * @return
     */
    public Map<String, Object> getShopInfo(Long smId) {
        return sqlSession.selectOne(this.ns+"getShopInfo",smId);
    }
}
