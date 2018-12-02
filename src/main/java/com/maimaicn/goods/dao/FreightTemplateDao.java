package com.maimaicn.goods.dao;

import com.maimaicn.goods.domain.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 运费模板dao
 */
@Repository
public class FreightTemplateDao {
    private static final String ns = "mappers.FreightTemplate.";

    @Autowired
    private SqlSession sqlSession;


    /**
     * 根据模板名称获取该用户的模板数据
     * @param memberId
     * @param templateName
     * @return
     */
    public Integer getTemplateByName(Long memberId, String templateName) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("memberId",memberId);
        params.put("templateName",templateName);
        return sqlSession.selectOne(ns+"getTemplateByName",params);
    }

    /**
     * 保存一条运费模板信息
     * @param freightTemplate 运费模本基本信息实体
     * @param freightDetails 运费模板价格详细信息list
     * @param freightFreeConditions 指定条件包邮信息list
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void save(FreightTemplate freightTemplate, List<FreightDetail> freightDetails, List<FreightFreeConditions> freightFreeConditions) {
        sqlSession.insert(ns+"save_freight_template",freightTemplate);

        //保存运费模板信息
        if(FreightTemplate.FRANK_NO.equals(freightTemplate.getFrank())) {   //不包邮
            this.saveFreightDetailsAndConditions(freightTemplate, freightDetails, freightFreeConditions);
        }
    }
    /**
     * 修改一条运费模板信息
     * @param freightTemplate 运费模本基本信息实体
     * @param freightDetails 运费模板价格详细信息list
     * @param freightFreeConditions 指定条件包邮信息list
     */
    @Transactional
    public void update(FreightTemplate freightTemplate, List<FreightDetail> freightDetails, List<FreightFreeConditions> freightFreeConditions) {
        Integer templateId = freightTemplate.getTemplateId();
        Map<String,Object> oldfreightTmp = sqlSession.selectOne(ns+"getTemplateById",templateId);//通过模板id查找以前数据

        sqlSession.update(ns+"update_freight_template",freightTemplate);//更新模板

        //删除旧数据
        if(oldfreightTmp!=null && FreightTemplate.FRANK_NO.equals(oldfreightTmp.get("frank"))){
            //不包邮
            sqlSession.delete(ns+"delete_freight_detail_and_location",templateId);//删除freight_detail   删除freight_detail_location 通过templateId
            //如果有指定条件包邮
            if(FreightTemplate.FREECONDITIONS_YES.equals(oldfreightTmp.get("freeConditions"))){
                sqlSession.delete(ns+"delete_freight_free_conditions_and_location",templateId); //删除freight_free_conditions   删除freight_free_conditions_location 通过templateId
            }
        }
        //保存运费模板信息
        if(FreightTemplate.FRANK_NO.equals(freightTemplate.getFrank())) {   //不包邮
            this.saveFreightDetailsAndConditions(freightTemplate, freightDetails, freightFreeConditions);
        }

    }
    /**
     * 保存运费模板信息
     * @param freightTemplate 运费模本基本信息实体
     * @param freightDetails 运费模板价格详细信息list
     * @param freightFreeConditions 指定条件包邮信息list
     */
    private void saveFreightDetailsAndConditions(FreightTemplate freightTemplate, List<FreightDetail> freightDetails, List<FreightFreeConditions> freightFreeConditions) {

        for(FreightDetail fd: freightDetails){
            fd.setTemplateId(freightTemplate.getTemplateId());
        }
        sqlSession.insert(ns+"save_freight_template_details",freightDetails);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(FreightDetail fd: freightDetails){
            if(FreightDetail.ISDEFAULT_NO.equals(fd.getIsDefault())){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("locationIds",fd.getLocationIds().split(","));
                map.put("detailId",fd.getDetailId());
                list.add(map);
            }
        }
        if(list.size() > 0){
            sqlSession.insert(ns+"save_freight_template_details_locations",list);//保存运费的特定地区
        }

        //如果有指定条件包邮，还得保存特定条件包邮信息
        if(FreightTemplate.FREECONDITIONS_YES.equals(freightTemplate.getFreeConditions())){
            for(FreightFreeConditions ffc: freightFreeConditions){
                ffc.setTemplateId(freightTemplate.getTemplateId());
            }
            sqlSession.insert(ns+"save_freight_template_free_conditions",freightFreeConditions);

            list.clear();
            for(FreightFreeConditions ffc: freightFreeConditions){
                if(FreightFreeConditions.ISDEFAULT_NO.equals(ffc.getIsDefault())){
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("locationIds",ffc.getLocationIds().split(","));
                    map.put("conditionsId",ffc.getConditionsId());
                    list.add(map);
                }
            }
            if(list.size() > 0){
                sqlSession.insert(ns+"save_freight_template_free_conditions_locations",list);
            }
        }

    }

    /**
     * 修改一条运费模板信息
     * @param templateId 运费模本id
     */
    @Transactional
    public void delete(Integer templateId) {
        sqlSession.delete(ns+"delete_freight_template",templateId);//删除模板

        sqlSession.delete(ns+"delete_freight_detail_and_location",templateId);//删除freight_detail   删除freight_detail_location 通过templateId
        sqlSession.delete(ns+"delete_freight_free_conditions_and_location",templateId); //删除freight_free_conditions   删除freight_free_conditions_location 通过templateId

    }
    /**
     * 获取运费模板详情
     * @param templateId  运费模板id
     */
    @Transactional
    public Map<String,Object> detail(Integer templateId) {
        Map<String,Object> freightTemplate = sqlSession.selectOne(ns + "get_freight_template_by_id", templateId);//获取模板
        //Map<String,Object> mapTemp =new HashMap<>();
        if(freightTemplate!=null && FreightTemplate.FRANK_NO.equals(freightTemplate.get("frank"))){
            //不包邮
            List<Object> detailLists=null;
            detailLists=sqlSession.selectList(ns+"get_freight_detail_list",templateId);
            freightTemplate.put("details",detailLists);
        }

        //如果有指定条件包邮
        if(freightTemplate!=null ){
            if (FreightTemplate.FREECONDITIONS_YES.equals(freightTemplate.get("freeConditions"))) {

            }
            List<Object> condiDetailLists=null;
            condiDetailLists=sqlSession.selectList(ns+"get_freight_condition_detail_list",templateId);
            freightTemplate.put("freeConditionsDetail",condiDetailLists);
        }
        return freightTemplate;
    }



    /**
     * 添加商品时获取我的运费模版下拉列表数据
     * @param memberId
     * @return
     */
    public List<Map<String,Object>> getMyTempCombo(Long memberId) {
        Map<String,Object> params = new HashMap<>();
        params.put("memberId",memberId);
        return sqlSession.selectList(ns+"getMyTempCombo",params);
    }

    /**
     * 获取并计算商品信息运费
     * @param mosList 包含skuId，freightId，wuliuParam,num,price
     * @param locationId 收货地址地址id
     * @return
     */
    public List<MakeOrderSku> getGoodsFreight(List<MakeOrderSku> mosList, Integer locationId) {
        //获取这个订单所有商品需要的运费模板信息，先查看是否包邮，在查看是否指定地区包邮，是否设置特定运费，最后使用默认运费
        List<Map<String,Object>> freight = sqlSession.selectList(this.ns+"getOrderFreightInfo",mosList);
        //合并运费模板和sku信息，方便下面计算sku运费信息
        for(MakeOrderSku mos : mosList){
            for(Map<String,Object> f : freight){
                if(mos.getFreightId().intValue() == ((Long) f.get("freightId")).intValue()){
                    mos.setDeliveryTime((Integer) f.get("deliveryTime"));//该商品的发货时间
                    mos.setFrank((String) f.get("frank"));
                    mos.setPriceType((String) f.get("priceType"));
                    mos.setFreeConditions((String) f.get("freeConditions"));
                    break;
                }
            }
        }

        //现在freightInfo包含以下字段：skuId，freightId，wuliuParam,num,price，deliveryTime,frank,priceType,freeConditions,freight
        for(MakeOrderSku mos : mosList){
            //如果该模板包邮，则运费直接设置为0
            if("yes".equals(mos.getFrank())){
                mos.setFreight(0.0);
                continue;
            }

            //是否设置指定条件包邮
            if("yes".equals(mos.getFreeConditions())){//有设置条件包邮
                //1.先看是否有该地区的特殊包邮条件
                Map<String,Object> params = new HashMap<>();
                params.put("freightId",mos.getFreightId());
                params.put("locationId",(locationId/100)*100);//转换为市级的地址id
                FreightFreeConditions ffc = sqlSession.selectOne(this.ns+"getThisFreeCondition",params);

                if(ffc == null){//如果不存在指定地区的包邮条件，就获取默认的包邮条件
                    ffc = sqlSession.selectOne(this.ns+"getDefaultFreeCondition",params);
                }
                if(ffc != null){//如果有包邮条件，才看是否满足包邮
                    switch (ffc.getConditionsType()){
                        case FreightFreeConditions.PRICETYPEVALUE_PRICETYPE ://按计价类型包邮
                            if("piece".equals(mos.getPriceType()) && mos.getNum() >= ffc.getPriceTypeValue()){//满多少件包邮
                                mos.setFreight(0.0);//设置运费为0
                            }else if(!"piece".equals(mos.getPriceType()) && mos.getNum()*mos.getWuliuParam() <= ffc.getPriceTypeValue()){//剩下的是按商品的重量和体积计费，都是小于多少重量、体积包邮
                                mos.setFreight(0.0);//设置运费为0
                            }
                            break;
                        case FreightFreeConditions.PRICETYPEVALUE_AMOUNT ://按金额包邮
                            if(mos.getNum() * mos.getPrice() >= ffc.getAmount() ){//满多少元包邮
                                mos.setFreight(0.0);//设置运费为0
                            }
                            break;
                        case FreightFreeConditions.PRICETYPEVALUE_MIX ://按金额+计价类型（件数、重量、体积）混合包邮
                            if(mos.getNum() * mos.getPrice() >= ffc.getAmount() ){
                                if("piece".equals(mos.getPriceType()) && mos.getNum() >= ffc.getPriceTypeValue()){//满多少件包邮
                                    mos.setFreight(0.0);//设置运费为0
                                }else if(!"piece".equals(mos.getPriceType()) && mos.getNum()*mos.getWuliuParam() <= ffc.getPriceTypeValue()){//剩下的是按商品的重量和体积计费，都是小于多少重量、体积包邮
                                    mos.setFreight(0.0);//设置运费为0
                                }
                            }
                            break;
                    }
                }
            }

            //如果走到这里运费还没有计算出来，那就没有包邮的条件了，只能按商品设置的运费价格来计算运费了
            if(mos.getFreight() == null){
                //1.先看是否有该地区的特殊包邮条件
                Map<String,Object> params = new HashMap<>();
                params.put("freightId",mos.getFreightId());
                params.put("locationId",(locationId/100)*100);//转换为市级的地址id
                FreightDetail fd = sqlSession.selectOne(this.ns+"getFreightDetailForSku",params);

                if(fd == null){//如果不存在指定地区的包邮条件，就获取默认的包邮条件
                    fd = sqlSession.selectOne(this.ns+"getDefaultFreightDetailForSku",params);
                }

                //根据计价类型，来计算运费
                double freight_temp = fd.getFirstPrice();//首费是肯定的;
                double remain = 0;
                switch (mos.getPriceType()){
                    case FreightTemplate.PRICETYPE_PIECE : //按件计算
                        remain = mos.getNum() - fd.getFirst();
                        if(remain > 0){
                            freight_temp += Math.ceil(remain / fd.getSubsequent()) * fd.getSubsequentPrice();//续费
                        }
                        break;
                    case FreightTemplate.PRICETYPE_WEIGHT ://按重量计算
                        remain = mos.getWuliuParam()*mos.getNum() - fd.getFirst();
                        if(remain > 0){
                            freight_temp += Math.ceil(remain / fd.getSubsequent()) * fd.getSubsequentPrice();
                        }
                        break;
                    case FreightTemplate.PRICETYPE_BULK ://按体积计算
                        remain = mos.getWuliuParam()*mos.getNum() - fd.getFirst();
                        if(remain > 0){
                            freight_temp += Math.ceil(remain / fd.getSubsequent()) * fd.getSubsequentPrice();
                        }
                        break;
                }
                mos.setFreight(freight_temp);
            }
        }

        return mosList;
    }


    /**
     * 获取我的运费模板列表
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Map<String,Object>> myFreightTempList(Long memberId, Integer pageNo, Integer pageSize) {
        Map<String,Object> params = new HashMap<>();
        params.put("memberId",memberId);
        params.put("startNo",(pageNo-1)*pageSize);
        params.put("pageSize",pageSize);

        return sqlSession.selectList(this.ns+"myFreightTempList",params);
    }
}
