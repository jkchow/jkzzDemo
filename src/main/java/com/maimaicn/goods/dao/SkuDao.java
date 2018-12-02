package com.maimaicn.goods.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimaicn.goods.domain.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class SkuDao {

    private static final String ns = "mappers.SkuMapper.";

    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private FreightTemplateDao freightTemplateDao;


    /**
     * 获取购物车列表商品信息
     * @param skuIds
     * @return
     */
    public List<Map<String,Object>> getShoppingCartList(Integer[] skuIds) throws IOException {
        if(skuIds.length == 0)return null;

        List<Map<String,Object>> list = sqlSession.selectList(this.ns + "getShoppingCartList", skuIds);

        //整理sku属性
        ObjectMapper om = new ObjectMapper();
        Set<Integer> skuPropIdSet = new HashSet<>();
        for(Map<String,Object> map : list){
            String properties = (String) map.get("skuProperties");
            ArrayList<Map<String,Object>> arrayList = om.readValue(properties, ArrayList.class);
            ArrayList<Map<String,Object>> propList = new ArrayList<>();
            for(Map<String,Object> one_prop : arrayList){
                Map<String,Object> prop = new HashMap<>();
                Integer pId = (Integer) one_prop.get("pId");
                skuPropIdSet.add(pId);
                prop.put("pId",pId);
                prop.put("vn",one_prop.get("vn"));

                Object img = one_prop.get("img");
                if(img != null && !StringUtils.isEmpty((String) img)){//如果商品的sku图片不为空，则替换商品的主图
                    map.put("mainImg",img);
                }
                propList.add(prop);
            }

            map.put("skuName",propList);
            map.remove("skuProperties");

            //整合商品的状态,商品状态不是上架状态的，在购物车里全显示为失效的
            if(!"up".equals(map.get("goodsStatus"))){
                map.put("status","delete");
            }
            map.remove("goodsStatus");
        }

        //db查询sku属性名称
        Map<String,Object> params = new HashMap<>();
        params.put("skuPropIdSet",skuPropIdSet);
        List<Map<String,Object>> skuProp = sqlSession.selectList(this.ns + "getSkuPropList", params);
        //遍历列表，把sku名称替换成字符串。
        for(Map<String,Object> map : list){
            ArrayList<Map<String,Object>> propList = (ArrayList<Map<String, Object>>) map.get("skuName");
            StringBuilder sb = new StringBuilder();
            for(Map<String,Object> prop: propList){
                Integer pId = (Integer) prop.get("pId");
                for(Map<String,Object> propName : skuProp){
                    if(pId == ((Long) propName.get("pId")).intValue()){
                        sb.append(propName.get("pn"));
                        sb.append(":");
                        sb.append(prop.get("vn"));
                        sb.append("  ");
                        break;
                    }
                }
            }
            map.put("skuName",sb.toString());
        }

        return list;
    }

    /**
     * 获取添加购物车时需要的信息，这个sku的商品店铺id，店铺名称，添加时冗余，方便后期查询
     * @param skuId
     * @return
     */
    public Map<String,Object> shoppingCartForAdd(Integer skuId) {
        return sqlSession.selectOne(this.ns+"shoppingCartForAdd",skuId);
    }

    /**
     * 清除购物车失效商品时，获取我的失效商品skuid
     * @param skuIds
     * @return
     */
    public List<Integer> getDisabledSku(Integer[] skuIds) {
        return sqlSession.selectList(this.ns+"getDisabledSku",skuIds);
    }

    /**
     * 下单时，获取订单的相关信息，包括，订单的商品信息，商品时候参加活动，活动的价格，运费，优惠劵的情况
     * @param skuParams
     * @param areaId 收货地址id，算运费需要
     * @return
     */
    public List<MakeOrderSku> getMakeOrderInfo(ArrayList<OrderSkuParams> skuParams, Integer areaId) throws IOException {
        List<MakeOrderSku> mosList = sqlSession.selectList(this.ns+"getCommitSkuInfo",skuParams);

        //把传递进来的sku购买数量合并到结果集中
        for(MakeOrderSku mos : mosList){
            for(OrderSkuParams osp : skuParams){
                if(osp.getSkuId().intValue() == mos.getSkuId().intValue()){
                    mos.setNum(osp.getNum());
                    break;
                }
            }
        }

        //计算运费
        mosList = freightTemplateDao.getGoodsFreight(mosList,areaId);

        //合并最后返回的值
        for(MakeOrderSku mos : mosList){
            mos.setSubtotal(mos.getPrice() * mos.getNum());
            //设置sku名称list和，替换sku主图
            extractSKUName(mos);
        }

        return mosList;
    }

    /**
     * 提取skuname
     * @param mos
     * @return
     */
    private void extractSKUName(MakeOrderSku mos) throws IOException {
        ObjectMapper om = new ObjectMapper();
        ArrayList<Map<String,Object>> skus = om.readValue(mos.getSkuProperties(), ArrayList.class);

        //提取属性名id
        Set<Integer> skuPropIdSet = new HashSet<>();
        for(Map<String,Object> sku : skus){
            skuPropIdSet.add((Integer) sku.get("pId"));
        }
        Map<String,Object> params = new HashMap<>();
        params.put("skuPropIdSet",skuPropIdSet);
        List<Map<String,Object>> skuProp = sqlSession.selectList(this.ns + "getSkuPropList", params);

        //拼接sku名称
        List<String> skunames = new LinkedList();
        for(Map<String,Object> sku : skus){
            Integer pId = (Integer) sku.get("pId");
            StringBuilder sb = new StringBuilder();
            //提取属性名称
            for(Map<String,Object> sp : skuProp){
                if(pId == ((Long) sp.get("pId")).intValue()) {
                    sb.append(sp.get("pn"));
                    break;
                }
            }
            //提取属性名称，备注
            sb.append(":");
            sb.append(sku.get("vn"));
            if(!StringUtils.isEmpty((String) sku.get("remark"))){
                sb.append("(" + sku.get("remark") + ")");
            }

            skunames.add(sb.toString());

            //设置sku主图
            String sku_img = (String) sku.get("img");
            if(!StringUtils.isEmpty(sku_img)){
                mos.setMainImg(sku_img);
            }
        }

        mos.setSkuName(skunames);
    }


    /**
     * 减商品库存
     * @param orderSkuParams
     */
    public void subSkuStock(ArrayList<OrderSkuParams> orderSkuParams) {
        sqlSession.update(this.ns+"subSkuStock",orderSkuParams);
    }

    /**
     * 加商品库存
     * @param orderSkuParams
     */
    public void addStock(ArrayList<OrderSkuParams> orderSkuParams) {
        sqlSession.update(this.ns+"addSkuStock",orderSkuParams);
    }
}
