package com.maimaicn.goods.configurations;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cache.annotation.EnableCaching;
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
 * redis配置
 */
@Configuration
@EnableCaching//开启注解
public class RedisConfig implements EnvironmentAware {

    private Properties property;

    public void setEnvironment(Environment environment) {
        property = Binder.get(environment).bind("spring.redis", Properties.class).get();
    }


    @Bean("redisTemplate")
    public RedisTemplate sessionRedisTemplate(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(property.getProperty("host"));
        configuration.setPort(Integer.parseInt(property.getProperty("port")));
        configuration.setDatabase(Integer.parseInt(property.getProperty("database")));
        configuration.setPassword(RedisPassword.of(property.getProperty("password")));

        JedisConnectionFactory jcf = new JedisConnectionFactory(configuration);

        jcf.setTimeout(2000);
        jcf.setPoolConfig(new JedisPoolConfig());
        jcf.afterPropertiesSet();

        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jcf);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }



}
