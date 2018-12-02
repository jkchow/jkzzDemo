package com.maimaicn.goods.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimaicn.goods.domain.Goods;
import com.maimaicn.goods.domain.GoodsPropertyData;
import com.maimaicn.goods.domain.SalePropForVerity;
import com.maimaicn.goods.domain.Sku;
import com.maimaicn.utils.dao.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * 商品dao
 */
@Repository
public class GoodsDao extends BaseDao<Goods>{
    private static final String ns = "mappers.Goods.";

    public GoodsDao(){
        this.setTableName("goods");
        this.setTableId("goodsId");
    }

    @Autowired
    private SqlSession sqlSession;

    /**
     * 上传保存商品
     * @param goods 商品基本信息实体
     * @param props_list 商品属性list
     * @param sku_list skulist
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Integer saveGoods(Goods goods, List<GoodsPropertyData> props_list, List<Sku> sku_list,String detail_img_json) {
        sqlSession.insert(this.ns+"saveGoods",goods);
        Map<String,Object> params = new HashMap<>();
        params.put("goodsId",goods.getGoodsId());
        params.put("propsList",props_list);

        //保存商品属性信息
        sqlSession.insert(this.ns+"saveGoodsProperties",params);

        //保存商品sku信息
        params.remove("propsList");
        params.put("skuList",sku_list);
        sqlSession.insert(this.ns+"saveSku",params);

        //保存商品详情图片信息
        params.remove("skuList");
        params.put("detailImgJson",detail_img_json);
        sqlSession.insert(this.ns+"saveDetailImgJson",params);

        return goods.getGoodsId();
    }

    /**
     * 修改商品属性
     * @param goods
     * @param props_list
     * @param sku_list
     * @param detailImgs
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateGoods(Goods goods, List<GoodsPropertyData> props_list, List<Sku> sku_list, String detailImgs) throws Exception {
        this.update(goods);

        Map<String,Object> params = new HashMap<>();
        params.put("goodsId",goods.getGoodsId());

        //修改商品属性,先删除，后插入的操作
        sqlSession.delete(this.ns+"deleteGoodsProperties",params);
        params.put("propsList",props_list);
        sqlSession.insert(this.ns+"saveGoodsProperties",params);

        //修改sku信息,有skuid的就是修改操作，没有skuId的就是删除操作
        List<Sku> insertSkuList = new ArrayList<>();
        List<Sku> updateSkuList = new ArrayList<>();
        for(Sku sku : sku_list){
            if(sku.getSkuId() != null){
                updateSkuList.add(sku);
            }else {
                insertSkuList.add(sku);
            }
        }
        params.put("skuList",updateSkuList);//删除skuid不在这里的sku
        sqlSession.delete(this.ns+"deleteSku",params);
        sqlSession.update(this.ns+"updateSku",params);
        params.put("skuList",insertSkuList);
        sqlSession.insert(this.ns+"saveSku",params);

        //修改详情图片
        params.remove("skuList");
        params.put("detailImgJson",detailImgs);
        sqlSession.insert(this.ns+"updateDetailImgJson",params);



    }

    /**
     * 查看商品是否重复上传
     * @param loginMemberId
     * @param mainImg
     * @param whiteImg
     * @return
     */
    public boolean isRepeatUpload(Long loginMemberId, String mainImg, String whiteImg) {
        Map<String,Object> params = new HashMap<>();
        params.put("memberId",loginMemberId);
        params.put("mainImg",mainImg);
        params.put("whiteImg",whiteImg);
        Integer goodsId = sqlSession.selectOne(this.ns+"isRepeatUpload",params);

        return goodsId==null?false:true;
    }


    /**
     * 获取商品的信息，用作修改使用
     * @param goodsId
     */
    public Map<String,Object> getForUpdate(Integer goodsId) throws Exception {
        Goods goods = get(goodsId);
        List<Map<String,Object>> props = sqlSession.selectList(this.ns+"getGoodsPropsForUpdate",goodsId);
        List<Map<String,Object>> skus = sqlSession.selectList(this.ns+"getSkuForUpdate",goodsId);
        String deatil_imgs = sqlSession.selectOne(this.ns+"getDetailImgs",goodsId);

        Map<String,Object> data = new HashMap<>();
        data.put("goodsId",goodsId);
        data.put("categoryId",goods.getCategoryId());
        data.put("title",goods.getTitle());
        data.put("mainImg",goods.getMainImg());
        data.put("whiteImg",goods.getWhiteImg());
        data.put("albumImgs",goods.getAlbumImgs());
        data.put("promSerIds",goods.getPromiseServiceIds());
        data.put("freightId",goods.getFreightId());
        data.put("wuliuParam",goods.getWuliuParam());
        data.put("subStockType",goods.getSubStockType());
        data.put("startType",goods.getStartType());

        data.put("props",props);
        data.put("skus",skus);
        data.put("deatilImgs",deatil_imgs);

        return data;
    }


    /**
     * 获取商品的基本信息，包含商品的goodsId，主图，标题，价格（取最大的sku价格）
     * @param goodsId
     * @return
     */
    public List<Map<String,Object>> getGoodsInfo(Integer[] goodsId) {
        List<Map<String,Object>> goodsList = sqlSession.selectList(this.ns+"getGoodsInfo",goodsId);
        List<Map<String,Object>> goodsPriceList = sqlSession.selectList(this.ns+"getGoodsSkuMaxPrice",goodsId);
        List<Map<String,Object>> goodsStatistics = sqlSession.selectList(this.ns+"getGoodsSalesStatistics",goodsId);

        for(Map<String,Object> goodsMap : goodsList) {
            //拼装商品价格信息
            for (Map<String, Object> priceMap : goodsPriceList) {
                if (goodsMap.get("goodsId") == priceMap.get("goodsId")) {
                    goodsMap.put("h_price", priceMap.get("h_price"));
                    goodsMap.put("srd", priceMap.get("srd"));
                    goodsMap.put("rd", priceMap.get("rd"));
                    continue;
                }
            }

            //拼装商品销量信息
            Object goods_sales = null;
            for (Map<String, Object> sales : goodsStatistics) {
                if (goodsMap.get("goodsId") == sales.get("goodsId")) {
                    goods_sales = sales.get("sales");
                    continue;
                }
            }

            if(goods_sales == null)
                goodsMap.put("sales", 0);
            else
                goodsMap.put("sales", goods_sales);


            //广告图，没有就加上
            if(goodsMap.get("ad169") == null){
                goodsMap.put("ad169", "");
            }
        }


        return goodsList;
    }


    /**
     * 获取商品solr发布时需要的信息
     * @param goodsId
     */
    public Map<String,Object> getSolrInfo(Integer goodsId) throws Exception {
        Map<String,Object> baseInfo = sqlSession.selectOne(this.ns+"getSolrGoodsInfo",goodsId);
        if(baseInfo == null){
            return null;
        }
        Map<String,Object> priceInfo = sqlSession.selectOne(this.ns+"getSolrPriceInfo",goodsId);

        Map<String,Object> params = new HashMap<>();
        params.put("categoryId",((Long)baseInfo.get("cateId")).intValue());
        params.put("goodsId", goodsId);
        List<Map<String,Object>> propInfo = sqlSession.selectList(this.ns+"getSolrPropertiesInfo",params);

        List<String> prop_val_names = new LinkedList<>();
        List<Integer> prop_val_ids = new LinkedList<>();
        ObjectMapper om = new ObjectMapper();
        for(Map<String,Object> prop : propInfo){
            prop_val_names.add((String) prop.get("valueName"));

            Object valueJson = om.readValue((String) prop.get("valueJson"), Object.class);
            if(valueJson instanceof Map){
                prop_val_ids.add((Integer) ((Map)valueJson).get("valueId"));
            }else if(valueJson instanceof List){
                List<Map<String,Object>> val_list = (List) valueJson;
                for(Map<String,Object> map : val_list){
                    prop_val_ids.add(Integer.parseInt((String) map.get("vId")));
                }
            }
        }

        baseInfo.putAll(priceInfo);
        baseInfo.put("prop_val_names",prop_val_names);
        baseInfo.put("prop_val_ids",prop_val_ids);

        return baseInfo;
    }

    /**
     * 获取商品图库  添加广告源时所需
     */
    public Map<String,Object> getGoodsImgStorage(Long memberId,String imgName, Integer categoryId, Integer pageNo, Integer pageSize) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("startNo", (pageNo-1)*pageSize);
        map.put("pageSize", pageSize);
        map.put("imgName", imgName);
        map.put("categoryId", categoryId);
        map.put("memberId",memberId);
        List<Map<String,Object>> list = sqlSession.selectList(ns +"getGoodsImgStorage", map);
        int count = sqlSession.selectOne(ns +"getGoodsImgStorageCount", map);

        map.clear();
        map.put("count", count);
        map.put("currentPage", pageNo);
        map.put("pageCount", (count + pageSize -1)/pageSize);
        map.put("list", list);
        return map;
    }

    /**
      * @描述 获取商品基本信息
    　* @author wxt
    　* @date 2018/11/12 14:57
    　*/
    public Map<String,Object> getAllGoodsInfo(Integer goodsId) {
        return  sqlSession.selectOne(this.ns+"getAllGoodsInfo",goodsId);
    }


    /**
     * @Description 获取商品上传时校验sku信息时需要的sku数据
     * @Author 张进
     * @Time 2018/11/30 13:34
     * @param
     * @return
     */
    public List<SalePropForVerity> getSkuPropForVerify(Integer categoryId) {
        return sqlSession.selectList(this.ns+"getSkuPropForVerify",categoryId);
    }
}
