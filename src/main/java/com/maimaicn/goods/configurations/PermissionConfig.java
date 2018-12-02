package com.maimaicn.goods.configurations;

import com.maimaicn.utils.permission.filter.SessionFilter;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Properties;

/**
 * 权限管理配置类
 */
@Configuration
public class PermissionConfig implements EnvironmentAware {

    private Properties property;

    public void setEnvironment(Environment environment) {
        property = Binder.get(environment).bind("spring.redis", Properties.class).get();
    }


    @Bean("sessionRedisTemplate")
    public RedisTemplate sessionRedisTemplate(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(property.getProperty("host_session"));
        configuration.setPort(Integer.parseInt(property.getProperty("port_session")));
        configuration.setDatabase(Integer.parseInt(property.getProperty("database_session")));
        configuration.setPassword(RedisPassword.of(property.getProperty("password_session")));

        JedisConnectionFactory jcf = new JedisConnectionFactory(configuration);

        jcf.setTimeout(2000);
        jcf.setPoolConfig(new JedisPoolConfig());
        jcf.afterPropertiesSet();

        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jcf);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    /**
     * 配置session过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean someFilterRegistration(@Qualifier("sessionRedisTemplate") RedisTemplate redisTemplate) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SessionFilter(redisTemplate));
        registration.setOrder(1);//第一个加载权限过滤器
        registration.addUrlPatterns("/*");
//        registration.addInitParameter("paramName", "paramValue");
        registration.setName("sessionFilter");
        return registration;
    }


    /**
     * 配置权限aop自动代理
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator d = new DefaultAdvisorAutoProxyCreator();
//        d.setAdvisorBeanNamePrefix("authorizingAdvisor");
        d.setProxyTargetClass(true);
        return d;
    }
}
