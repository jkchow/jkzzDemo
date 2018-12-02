package com.maimaicn.goods.mqListener;

import com.maimaicn.goods.dao.GoodsDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;
import java.util.Map;

/**
 * 监听发布商品的信息mq
 */
@Component
public class PublishGoodsToSolrListener {
    private static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private SolrClient solrClient;


    /**
     * 从消息队列监听消息，从消息队列获取商品id，然后查询db得到solr需要的信息，最后提交到solr
     * 当solr中 id 存在的时候, 此方法是修改(当然, 我这里用的 uuid, 不会存在的), 如果 id 不存在, 则是新增
     * @param message
     */
    @JmsListener(destination = "solr.publish.goods",containerFactory = "queueContainerFactory")
    public void publishGoods(Message message){
        Integer goodsId = null;
        try {
            MapMessage map_message = (MapMessage) message;
            goodsId = map_message.getInt("goodsId");

            Map<String, Object> solrInfo = goodsDao.getSolrInfo(goodsId);
            if(solrInfo == null){
                message.acknowledge();//确认收到消息
                return;
            }

            //添加solr信息
            SolrInputDocument sid = new SolrInputDocument();
            sid.addField("goodsId",solrInfo.get("goodsId"));
            sid.addField("title",solrInfo.get("title"));
            sid.addField("h_price",solrInfo.get("h_price"));
            sid.addField("l_price",solrInfo.get("l_price"));
            sid.addField("catId",solrInfo.get("cateId"));
            sid.addField("catName",solrInfo.get("cateName"));
            sid.addField("brandId",solrInfo.get("brandId"));
            sid.addField("brandName",solrInfo.get("brandName"));
            sid.addField("propValNames",solrInfo.get("prop_val_names"));
            sid.addField("propValIds",solrInfo.get("prop_val_ids"));
            sid.addField("mainImg",solrInfo.get("mainImg"));
            sid.addField("shopMId",solrInfo.get("shopMId"));
            sid.addField("shopName",solrInfo.get("shopName"));
            sid.addField("sales",solrInfo.get("sales")==null?0:solrInfo.get("sales"));
            sid.addField("createTime",solrInfo.get("createTime"));
            solrClient.add(sid);//当 id 存在的时候, 此方法是修改(当然, 我这里用的 uuid, 不会存在的), 如果 id 不存在, 则是新增
            solrClient.commit();

            message.acknowledge();//确认收到消息
        }catch (Exception e){
            log.error("发布商品到solr信息监听异常：goodsId="+goodsId,e);
        }

    }


    /**
     * 下架一个商品的solr信息，就是删除操作
     * @param message
     */
    @JmsListener(destination = "solr.unload.goods",containerFactory = "queueContainerFactory")
    public void unloadGoods(Message message) {
        Integer goodsId = null;
        try {
            MapMessage map_message = (MapMessage) message;
            goodsId = map_message.getInt("goodsId");

            solrClient.deleteById(goodsId+"");
            solrClient.commit();

            message.acknowledge();//确认收到消息
        } catch (Exception e) {
            log.error("删除商品到solr信息监听异常：goodsId=" + goodsId, e);
        }
    }
}
