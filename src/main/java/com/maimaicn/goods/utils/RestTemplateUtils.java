package com.maimaicn.goods.utils;

import com.maimaicn.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Set;


@Component
public class RestTemplateUtils {

    private static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(@Qualifier("restTemplate") RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    /**
     * 获取店铺信息
     * @param shopMId
     * @return
     */
    public static JsonData getShopInfo(Long shopMId){
        return restTemplate.getForObject("http://goods/detail/shopInfo?smId=" + shopMId, JsonData.class);
    }

    /**
     * 获取会员账号信息
     * @param memberId 买家会员id
     * @return
     */
    public static JsonData getMemberAccountInfo(Long memberId, Set<Long> smIds){
        StringBuilder sb = new StringBuilder();
        for(Long smId : smIds){
            sb.append(smId);
            sb.append(";");
        }
        return restTemplate.getForObject("http://account/redPacketType/getRedPacketAndAccountByMIdForMember?midInfo=" + memberId + "_" + sb.toString(), JsonData.class);
    }

}
