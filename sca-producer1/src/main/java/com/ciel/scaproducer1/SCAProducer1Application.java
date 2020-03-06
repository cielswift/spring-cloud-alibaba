package com.ciel.scaproducer1;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 使用多数据源排除自动加载的数据源
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)

/**
 * 使用nacos作为注册中心
 */
@EnableDiscoveryClient

/**
 * 开启基于注解的aop
 */
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)

@ComponentScan(basePackages = "com.ciel")

@MapperScan("com.ciel.scacommons.mapper")

@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE)

/**
 * 开启feign调用
 */
@EnableFeignClients

/**
 * hmily 要扫描的包
 */
@ComponentScan("org.dromara.hmily")

@EnableScheduling  //开启定时任务
public class SCAProducer1Application {

    public static void main(String[] args) {
        SpringApplication.run(SCAProducer1Application.class, args);
    }


    @Bean
    @Primary
    public RedisTemplate<String, String> redisString(RedisConnectionFactory factory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setDefaultSerializer(RedisSerializer.string());
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        return redisTemplate;
    }

}
