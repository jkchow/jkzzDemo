package com.maimaicn.goods.service;

import com.maimaicn.goods.dao.FreightTemplateDao;
import com.maimaicn.goods.dao.SkuDao;
import com.maimaicn.goods.domain.OrderSkuParams;
import com.maimaicn.goods.domain.CommitOrderView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * skuservice
 */
@Service
public class SkuService {

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private FreightTemplateDao freightTemplateDao;


    /**
     * 获取下单和订单确认页需要的东西
     * @param skuParams
     * @param areaId
     * @return
     */
    public List<CommitOrderView> getMakeOrderInfo(ArrayList<OrderSkuParams> skuParams, Integer areaId){


        return null;
    }

}
