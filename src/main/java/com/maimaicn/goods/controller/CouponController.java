package com.maimaicn.goods.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maimaicn.goods.dao.CouponDao;
import com.maimaicn.goods.domain.Coupon;
import com.maimaicn.goods.domain.CouponDenomination;
import com.maimaicn.utils.controller.BaseController;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 优惠劵控制类
 */
@RestController
public class CouponController extends BaseController{

    @Autowired
    private CouponDao couponDao;

    /**
     * 获取优惠劵列表
     * @param request
     * @return
     */
    @RequestMapping("/coupon/list")
    public String couponList(HttpServletRequest request,String name,String type,String status,Integer pageNo,Integer pageSize) {
        try{
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,1,"登录超时");
            }

            if(pageNo == null) pageNo = 1;
            if(pageSize == null) pageSize = 5;

            Map<String,Object> params = new HashMap<>();
            if(!StringUtils.isEmpty(name)){
                params.put("name",name);
            }
            if(!StringUtils.isEmpty(status)){
                params.put("status",status);
            }
            if(!StringUtils.isEmpty(type)){
                params.put("type",type);
            }
            params.put("memberId",loginMemberId);

            List<Map<String,Object>> list = couponDao.getList(params,pageNo,pageSize);

            return jsonData(request,0,list);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     *
     * @param request
     * @param coupon 优惠劵实体
     * @param goodsIds 使用的商品ids 数组
     * @param denominations 卡卷面额信息数组
     * @return
     */
    @RequestMapping("/coupon/save")
    public String saveCoupon(HttpServletRequest request, Coupon coupon,String goodsIds,String denominations) {
        try{
            Long loginMemberId = getLoginMemberId(request);
            if(loginMemberId == null){
                return jsonData(request,1,"登录超时");
            }
            if(StringUtils.isEmpty(coupon.getName()) || StringUtils.isEmpty(coupon.getType())
                    || coupon.getStartTime()==null || coupon.getEndTime() == null){
                return jsonData(request,2,"必要参数不能为空");
            }
            if(coupon.getEndTime().getTime() <= new Date().getTime()){
                return jsonData(request,3,"结束时间小于当前时间");
            }
            if(coupon.getStartTime().getTime() > coupon.getEndTime().getTime()){
                return jsonData(request,4,"开始时间不能大于结束时间");
            }

            coupon.setCreateTime(new Date());
            coupon.setMemberId(loginMemberId);

            //采集封装面额信息
            ObjectMapper om = new ObjectMapper();
            ArrayList<Map<String,Object>> denomination_map_list = om.readValue(denominations, ArrayList.class);
            List<CouponDenomination> denomination_list = new ArrayList<>();
            for(Map<String,Object> map : denomination_map_list){
                if(map.get("amount") == null || map.get("threshold") == null || map.get("circulation") == null || map.get("limitGet") == null){
                    return jsonData(request,2,"必要参数不能为空");
                }
                Double amount = Double.parseDouble((String) map.get("amount"));
                Double threshold = Double.parseDouble((String) map.get("threshold"));
                Integer circulation = Integer.parseInt((String) map.get("circulation"));
                Integer limitGet = Integer.parseInt((String) map.get("limitGet"));
                if(amount%2 != 0 && amount%3 != 0 && amount%5 != 0 && amount != 1){
                    return jsonData(request,3,"优惠金额必须是1/2/3/5及5的整数倍金额");
                }
                if(amount >= threshold){
                    return jsonData(request,4,"门槛需要高于金额");
                }
                if(circulation > 100000){
                    return jsonData(request,5,"发现量不能超过10万张");
                }

                denomination_list.add(new CouponDenomination(amount,threshold,circulation,limitGet,coupon.getStartTime().getTime() > coupon.getCreateTime().getTime()?CouponDenomination.STATUS_WAIT:CouponDenomination.STATUS_GETTING));
            }

            ArrayList<Integer> apply_goods_ids = new ArrayList<>();
            if("goods".equals(coupon.getType())) {
                String[] goodsId_arr = goodsIds.split(",");
                for (String goodsId : goodsId_arr) {
                    apply_goods_ids.add(Integer.parseInt(goodsId));
                }
            }
            if("goods".equals(coupon.getType()) && denomination_list.size() !=1){
                return jsonData(request,6,"商品级优惠劵只能设置1张面额信息");
            }
            if("shop".equals(coupon.getType()) && apply_goods_ids.size() > 0){
                return jsonData(request,7,"店铺级优惠劵不能设置适用商品");
            }

            couponDao.saveCoupon(coupon,denomination_list,apply_goods_ids);

            return jsonData(request,0,"优惠劵保存成功");
        } catch(NumberFormatException e){
            log.error("保存优惠劵数量格式化异常",e);
            return jsonData(request,8,"面额数量填写错误");
        } catch(Exception e){
            log.error("保存优惠劵异常",e);
            return jsonData(request,110,"服务忙");
        }
    }

    /**
     * 修改优惠劵信息，只能修改优惠劵发行数量（只能改大，不能改小），和限领数量
     */
    @RequestMapping("/coupon/update")
    public String couponUpdate(HttpServletRequest request,Integer cardId,Integer limitGet,Integer circulation) {
        try{
            if(cardId == null || limitGet == null || circulation == null){
                return jsonData(request,1,"必要参数不能为空");
            }

            couponDao.updateDenomination(cardId,circulation,limitGet);
            return jsonData(request,0,"更新成功");
        } catch(Exception e){
            log.error("更新优惠劵面额异常",e);
            return jsonData(request,110,"服务忙");
        }
    }


    /**
     * 获取一张优惠劵信息
     * @param request
     * @param cardId
     * @return
     */
    @RequestMapping("/coupon/getInfo")
    public String getInfo(HttpServletRequest request,Integer cardId) {
        try{
            if(cardId == null ){
                return jsonData(request,1,"必要参数不能为空");
            }

            Map<String,Object> data = couponDao.getOneCouponInfo(cardId);
            if(data == null){
                return jsonData(request,2,"没有找到对应的优惠劵信息");
            }

            return jsonData(request,0,data);
        } catch(Exception e){
            log.error("异常",e);
            return jsonData(request,110,"服务忙");
        }
    }



}
