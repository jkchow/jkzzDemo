package com.maimaicn.goods.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class B2BWhiteListDao {
    private static final String ns = "mappers.B2BWhiteListMapper.";

    @Autowired
    private SqlSession sqlSession;


    /**
     * 查看这个会员是不是卖家的b2b买家
     * @param sellerMemberId
     * @param buyerMemberId
     * @return
     */
    public boolean isB2BBuyer(Long sellerMemberId,Long buyerMemberId) {
        Map<String,Object> params = new HashMap<>();
        params.put("sellerMemberId",sellerMemberId);
        params.put("buyerMemberId",buyerMemberId);

        Object o = sqlSession.selectOne(this.ns + "isB2BBuyer", params);
        return o==null?false:true;
    }
}
