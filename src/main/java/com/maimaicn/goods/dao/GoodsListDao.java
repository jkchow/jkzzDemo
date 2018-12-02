package com.maimaicn.goods.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimaicn.goods.domain.*;
import com.maimaicn.utils.PageVO;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class GoodsListDao {

    private static final String ns = "mappers.GoodsListMapper.";


    @Autowired
    private SqlSession sqlSession;

    /**
     * 获取商品列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Map<String,Object>> goodsList(Integer pageNo, Integer pageSize,Long sellerMemberId,Integer goodsId,String status,String goodsName,Date createTime) {
        Map<String,Object> params = new HashMap<>();
        params.put("memberId",sellerMemberId);
        params.put("startNo",(pageNo-1)*pageSize);
        params.put("pageSize",pageSize);
        params.put("goodsId",goodsId);
        params.put("status",status);
        params.put("goodsName",goodsName);
        params.put("createTime",createTime);
        List<Map<String,Object>> goodsList = sqlSession.selectList(this.ns + "goodsList", params);
        if(goodsList == null || goodsList.size() == 0){
            return new ArrayList<>();
        }

        //获取商品的总库存
        List<Map<String,Object>> stockList = sqlSession.selectList(this.ns+"getTotalStock",goodsList);

        //获取商品的价格区间
        List<Map<String,Object>> priceRangeList = sqlSession.selectList(this.ns+"getPriceRange",goodsList);

        for(Map<String,Object> goodsMap : goodsList){
            for(Map<String,Object> stockMap : stockList){
                if(goodsMap.get("goodsId") == stockMap.get("goodsId")){
                    goodsMap.put("t_stock",stockMap.get("t_stock"));
                    continue;
                }
            }

            for(Map<String,Object> priceRange : priceRangeList){
                if(goodsMap.get("goodsId") == priceRange.get("goodsId")){
                    goodsMap.put("l_price",priceRange.get("l_price"));
                    goodsMap.put("h_price",priceRange.get("h_price"));
                    goodsMap.put("b2b_l_price",priceRange.get("b2b_l_price"));
                    goodsMap.put("b2b_h_price",priceRange.get("b2b_h_price"));
                    continue;
                }
            }
        }
        return goodsList;
    }

    /**
     * 获取修改sku信息的原始数据，用于回显
     * @param goodsId 商品id
     * @param sellerMemberId
     */
    public List<Map<String,Object>> getSkuUpdateInfo(Integer goodsId, Long sellerMemberId) throws IOException {
        Map<String,Object> params = new HashMap<>();
        params.put("goodsId",goodsId);
        params.put("sellerMemberId",sellerMemberId);

        //1.先校验该商品是不是当前登录的卖家的商品，不是就返回
        Object o = sqlSession.selectOne(this.ns + "isSellerGoods", params);
        if(o == null){
            return null;
        }

        //2.获取sku的信息
        List<Map<String,Object>> skuList = sqlSession.selectList(this.ns+"getSkuUpdateInfo",goodsId);

        return extractSKUName(skuList);
    }


    private List<Map<String,Object>> extractSKUName(List<Map<String,Object>> skuList) throws IOException {
        //提取属性名id
        ObjectMapper om = new ObjectMapper();
        List<Map<String,Object>> skus = om.readValue((String) skuList.get(0).get("skuProperties"), List.class);
        Set<Integer> skuPropIdSet = new HashSet<>();
        for(Map<String,Object> sku : skus){
            skuPropIdSet.add((Integer) sku.get("pId"));
        }
        Map<String,Object> params = new HashMap<>();
        params.put("skuPropIdSet",skuPropIdSet);
        List<Map<String,Object>> skuPropNames = sqlSession.selectList(this.ns + "getSkuPropList", params);

        //拼接sku名称
        for(Map<String,Object> sku : skuList){
            List<Map<String,Object>> skuProps = om.readValue((String) sku.get("skuProperties"), List.class);
            StringBuilder skuName = new StringBuilder();
            for(Map<String,Object> oneProp : skuProps){
                Integer pId = (Integer) oneProp.get("pId");
                //提取属性名称
                for(Map<String,Object> sp : skuPropNames){
                    if(pId == ((Long) sp.get("pId")).intValue()) {
                        skuName.append(sp.get("pn"));
                        break;
                    }
                }
                //拼接属性值
                skuName.append(":");
                skuName.append(oneProp.get("vn"));
                skuName.append("; ");
            }
            sku.remove("skuProperties");
            sku.put("skuName",skuName.toString());
        }

        return skuList;
    }

    /**
     * 更新sku信息
     * @param updateList
     * @param sellerMemberId
     */
    public void updateSku(List<UpdateSkuPojo> updateList, Long sellerMemberId,Integer goodsId) {
        //1.先校验该商品是不是当前登录的卖家的商品，不是就返回
        Map<String,Object> params = new HashMap<>();
        params.put("goodsId",goodsId);
        params.put("sellerMemberId",sellerMemberId);
        Object o = sqlSession.selectOne(this.ns + "isSellerGoods", params);
        if(o == null || updateList.size() == 0){
            return ;
        }

        params.put("updateSkuList",updateList);
        sqlSession.update(this.ns+"updateSku",params);
    }

    /**
      * @描述 根据条件获取商品
    　* @author wxt
    　* @date 2018/11/12 19:58
    　*/
    public List<Map<String,Object>> goodsListByCategory(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        params.put("startNo",(pageNo-1)*pageSize);
        params.put("pageSize",pageSize);
        return sqlSession.selectList(this.ns + "goodsListByCategory", params);
    }

    /**
     * 修改商品状态
     * @param sellerMemberId
     * @param goodsId
     * @param status
     * @return
     */
    public int updateStatus(Long sellerMemberId, Integer goodsId, String status) {
        Map<String,Object> params = new HashMap<>();
        params.put("goodsId",goodsId);
        params.put("sellerMemberId",sellerMemberId);
        params.put("status",status);

        return sqlSession.update(this.ns+"updateStatus",params);
    }

    /**
     * 批量删除商品，逻辑删除
     * @param sellerMemberId
     * @param goodsId
     * @return
     */
    public int delete(Long sellerMemberId, Integer[] goodsId) {
        Map<String,Object> params = new HashMap<>();
        params.put("goodsIds",goodsId);
        params.put("sellerMemberId",sellerMemberId);
        return sqlSession.update(this.ns+"deleteGoods",params);
    }

    /**
     * 获取商品列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageVO<Map<String,Object>> goodsAdminList(Integer pageNo, Integer pageSize,Map<String, Object> params) {
        params.put("startNo", (pageNo - 1) * pageSize);
        params.put("pageSize", pageSize);
        int count = sqlSession.selectOne(ns+"goodsAdminListCount",params);
        if (count<=0) {
            return  new PageVO<Map<String,Object>>(pageNo, pageSize, count, new ArrayList<>());
        }
        List<Map<String, Object>> goodsList = sqlSession.selectList(this.ns + "goodsAdminList", params);
        if (goodsList!=null&&goodsList.size()>0) {
            Set<String> brandIdSet = new HashSet<>();
            Set<String> categoryIdSet = new HashSet<>();
            for (Map<String, Object> map:goodsList ) {
                brandIdSet.add(map.get("brandId").toString());
                categoryIdSet.add(map.get("categoryId").toString());
            }
            //获取brand category 名字列表
            List<Brand> brandList = new ArrayList<>();
            List<Category> categoryList = new ArrayList<>();
            if (brandIdSet.size()>0) {
                brandList = sqlSession.selectList(this.ns + "getBrandListByIds", brandIdSet);
            }
            if (categoryIdSet.size()>0) {
                categoryList = sqlSession.selectList(this.ns + "getCategoryListByIds", categoryIdSet);
            }
            //list 转为map  降低时间复杂度
            Map<Integer,String> brandMap=new HashMap<>();
            Map<Integer,String> categoryMap=new HashMap<>();
            for(Brand brand:brandList ){
                brandMap.put(brand.getBrandId(),brand.getChName());
            }
            for(Category category:categoryList ){
                categoryMap.put(category.getCategoryId(),category.getCategoryName());
            }
            //给列表重新赋值
            for (Map<String, Object> map:goodsList ) {
                map.put("brandId",brandMap.get(Integer.valueOf(map.get("brandId").toString())));
                map.put("categoryId",categoryMap.get(Integer.valueOf(map.get("categoryId").toString())));
            }

        }

        return new PageVO<Map<String,Object>>(pageNo, pageSize, count, goodsList);
    }
    /**
     * 获取商品详情  主图   sku详情
     * @param goodsId
     * @return
     */
    public Map<String,Object> goodsAdminDetail(Integer goodsId) throws IOException{
        Map<String,Object> goodsInfo =new HashMap<>();
        Goods goods = sqlSession.selectOne(this.ns + "getGoodsDetailById", goodsId);
        if(goods == null){
            return goodsInfo;
        }
        if (goods.getAlbumImgs()!=null) {
            String albumImgs=goods.getAlbumImgs();
            goodsInfo.put("albumImgs",albumImgs.split("\\|"));
        }
        goodsInfo.put("mainImg",goods.getMainImg());
        /*goodsInfo.put("title",goods.getTitle());
        goodsInfo.put("categoryId",goods.getCategoryId());
        goodsInfo.put("brandId",goods.getBrandId());
        goodsInfo.put("goodsId",goods.getGoodsId());*/

        List<Sku> skuList=sqlSession.selectList(this.ns + "getSkuByGoodsId", goodsId);
        if (skuList==null) {
            return goodsInfo;
        }
        ArrayList<Map<String,Object>> skuPropList = new ArrayList<>();
        Set<Integer> skuPropIdSet = new HashSet<>();
        //整理sku属性
        ObjectMapper om = new ObjectMapper();
        for(Sku sku : skuList) {
            String properties = sku.getSkuProperties();
            ArrayList<Map<String, Object>> arrayList = om.readValue(properties, ArrayList.class);
            ArrayList<Map<String,Object>> onepropList = new ArrayList<>();
            for(Map<String,Object> one_prop : arrayList){//遍历每行里的数据
                Map<String,Object> prop = new HashMap<>();
                Integer pId = (Integer) one_prop.get("pId");
                skuPropIdSet.add(pId);//保存pid  统一查询
                prop.put("pId",pId);
                prop.put("vn",one_prop.get("vn"));
                onepropList.add(prop);//添加到子集合
            }
            Map<String,Object> skuStock = new HashMap<>();
            skuStock.put("stock",sku.getStock());//每行库存
            skuStock.put("properties",onepropList);

            skuPropList.add(skuStock);//添加到父集合
        }
        List<SaleProperty> salePropList ;
        if (skuPropIdSet.size()>0) {
            salePropList = sqlSession.selectList(this.ns + "getSalePropListByIds", skuPropIdSet);//查询出所有id 对应的prop名字
        }else{
            return goodsInfo;
        }
        //list 转为map  降低时间复杂度
        Map<Integer,String> salePropMap=new HashMap<>();
        for(SaleProperty saleProperty:salePropList ){
            salePropMap.put(saleProperty.getPropertyId(),saleProperty.getPropertyName());
        }
        //为集合赋值
        for (Map<String,Object> prarentSkuMap : skuPropList) {
            StringBuilder skuProp = new StringBuilder();
            if (prarentSkuMap.get("properties")!=null) {
                ArrayList<Map<String,Object>> properties = (ArrayList<Map<String, Object>>) prarentSkuMap.get("properties");
                for (Map<String,Object> sonSkuMap : properties) {
                    //String pName= salePropMap.get(sonSkuMap.get("pId"));
                    //sonSkuMap.put("pId",pName);
                    skuProp.append(salePropMap.get(sonSkuMap.get("pId")));
                    skuProp.append(":");
                    skuProp.append(sonSkuMap.get("vn"));
                    skuProp.append(";");
                }
            }
            //prarentSkuMap.put("skuProperty",skuProp);
            prarentSkuMap.put("properties",skuProp);

        }
        goodsInfo.put("skuList",skuPropList);

        return  goodsInfo;
    }


}
