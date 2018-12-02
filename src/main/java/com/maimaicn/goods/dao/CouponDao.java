package com.maimaicn.goods.dao;

import com.maimaicn.goods.domain.Coupon;
import com.maimaicn.goods.domain.CouponDenomination;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CouponDao  {

    private static final String ns = "mappers.CouponMapper.";

    @Autowired
    private SqlSession sqlSession;


    /**
     * 获取优惠劵列表
     * @param params
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<Map<String,Object>> getList(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        params.put("startNo",(pageNo-1)*pageSize);
        params.put("pageSize",pageSize);

        return sqlSession.selectList(this.ns+"getCouponList",params);
    }


    /**
     *
     * @param coupon
     * @param denomination_list
     * @param apply_goods_ids
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void saveCoupon(Coupon coupon, List<CouponDenomination> denomination_list, ArrayList<Integer> apply_goods_ids) throws Exception{
        //插入优惠劵主体信息
        sqlSession.insert(this.ns+"insertCoupon",coupon);

        Map<String,Object> params = new HashMap<>();
        params.put("couponId",coupon.getCouponId());

        //插入优惠劵面额信息
        if(denomination_list.size() > 0){
            params.put("denominations", denomination_list);
            sqlSession.insert(this.ns+"batchInsertDenomination",params);
        }

        //批量插入优惠劵适用的商品id
        if(apply_goods_ids.size() > 0){
            params.put("goodsIds", apply_goods_ids);
            sqlSession.insert(this.ns+"batchInsertApplyGoods",params);
        }
    }

    /**
     * 商家后台修改优惠劵发行量，限领次数
     * @param cardId 优惠劵面额id
     * @param circulation 发行量
     * @param limitGet 没人限领数量
     */
    public void updateDenomination(Integer cardId, Integer circulation, Integer limitGet) {
        Map<String,Object> params = new HashMap<>();
        params.put("cardId",cardId);
        params.put("circulation",circulation);
        params.put("limitGet",limitGet);

        sqlSession.update(this.ns+"updateDenomination",params);
    }

    /**
     * 获取一张优惠劵信息
     * @param cardId
     * @return
     */
    public Map<String,Object> getOneCouponInfo(Integer cardId) {
        return sqlSession.selectOne(this.ns+"getOneCouponInfo",cardId);
    }
}
