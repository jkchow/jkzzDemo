package com.maimaicn.goods.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

/**
 * 发送mq消息专用工具类
 */
@Component
public class SendMQUtils {
    protected static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private static JmsTemplate jmsQueueTemplate;
    private static JmsTemplate jmsTopicTemplate;


    @Autowired
    public void setJmsQueueTemplate(@Qualifier("jmsQueueTemplate") JmsTemplate jmsQueueTemplate){
        this.jmsQueueTemplate = jmsQueueTemplate;
    }
    @Autowired
    public void setJmsTopicTemplate(@Qualifier("jmsTopicTemplate") JmsTemplate jmsTopicTemplate){
        this.jmsTopicTemplate = jmsTopicTemplate;
    }


    /**
     * 发送一个商品发布消息
     */
    public static void sendPublishSolrGoodsJms(Integer goodsId) {
        try{
            if(goodsId == null){
                return ;
            }

            jmsQueueTemplate.send("solr.publish.goods", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setInt("goodsId",goodsId);

                    return mapMessage;
                }
            });
        }catch (Exception e){
            //把注册信息先写本地
            log.error("创建发布商品消息异常"+goodsId,e);
        }
    }


    /**
     * 发布一个下架商品的消息
     * @param goodsId
     */
    public static void sendUnloadSolrGoodsJms(Integer goodsId) {
        try{
            if(goodsId == null){
                return ;
            }

            jmsQueueTemplate.send("solr.unload.goods", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setInt("goodsId",goodsId);

                    return mapMessage;
                }
            });
        }catch (Exception e){
            //把注册信息先写本地
            log.error("创建下架商品消息异常"+goodsId,e);
        }
    }

}
