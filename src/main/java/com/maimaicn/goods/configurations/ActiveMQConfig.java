package com.maimaicn.goods.configurations;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import java.util.Properties;

@Configuration
public class ActiveMQConfig implements EnvironmentAware  {

    private Properties property;

    public void setEnvironment(Environment environment) {
        property = Binder.get(environment).bind("spring.activemq", Properties.class).get();
    }

    @Bean("connectionFactory")
    @Primary
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory amqConnectFactory = new ActiveMQConnectionFactory();
        amqConnectFactory.setBrokerURL(property.getProperty("broker-url"));
        amqConnectFactory.setUserName(property.getProperty("user"));
        amqConnectFactory.setPassword(property.getProperty("password"));

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(amqConnectFactory);
        cachingConnectionFactory.setCacheConsumers(true);
        cachingConnectionFactory.setSessionCacheSize(10);//会话最大链接数
        return cachingConnectionFactory;
    }

    @Bean("jmsQueueTemplate")
    public JmsTemplate jmsQueueTemplate(@Qualifier("connectionFactory")ConnectionFactory connectionFactory){
        JmsTemplate jmsQueueTemplate = new JmsTemplate();
        jmsQueueTemplate.setConnectionFactory(connectionFactory);
        jmsQueueTemplate.setReceiveTimeout(10000);
        jmsQueueTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);// NON_PERSISTENT 非持久化 PERSISTENT 持久化,发送消息时用使用持久模式
        jmsQueueTemplate.setPubSubDomain(false);//true是topic，false是queue，默认是false，此处显示写出false

        return jmsQueueTemplate;
    }

    @Bean("jmsTopicTemplate")
    public JmsTemplate jmsTopicTemplate(@Qualifier("connectionFactory")ConnectionFactory connectionFactory){
        JmsTemplate jmsTopicTemplate = new JmsTemplate();
        jmsTopicTemplate.setConnectionFactory(connectionFactory);
        jmsTopicTemplate.setReceiveTimeout(10000);
        jmsTopicTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);//2-消息需要持久化，默认不持久化，那就只有在线的监听者能收到消息了。
        jmsTopicTemplate.setPubSubDomain(true);//true是topic，false是queue，默认是false，此处显示写出false

        return jmsTopicTemplate;
    }


   /* @Bean("topicContainerFactory")
    public DefaultJmsListenerContainerFactory containerFactory(@Qualifier("cachingConnectionFactory")ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        //设置连接数
        containerFactory.setConcurrency("1-10");
        //重连间隔时间
        containerFactory.setRecoveryInterval(1000L);
        *//*
        Session.AUTO_ACKNOWLEDGE = 1    自动确认
        Session.CLIENT_ACKNOWLEDGE = 2    客户端手动确认
        Session.DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
        Session.SESSION_TRANSACTED = 0    事务提交并确认
        INDIVIDUAL_ACKNOWLEDGE = 4    单条消息确认 activemq 独有*//*
        containerFactory.setSessionAcknowledgeMode(4);
        return containerFactory;
    }*/

    @Bean("queueContainerFactory")
    public DefaultJmsListenerContainerFactory topicContainerFactory(@Qualifier("connectionFactory")ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();

        containerFactory.setConnectionFactory(new SingleConnectionFactory(connectionFactory));
        containerFactory.setPubSubDomain(false);//queue方式
//        containerFactory.setSubscriptionDurable(true);//持久化订阅者
//        containerFactory.setBackOff(new FixedBackOff(1000l,0));//设置重连信息，这里设置成连不上就不用连了，因为可能有其他人已经连了，就不处理了

        //重连间隔时间
        containerFactory.setRecoveryInterval(1000L);
        containerFactory.setConcurrency("1-10");//并发处理数
        containerFactory.setSessionAcknowledgeMode(4);
        return containerFactory;
    }


}
