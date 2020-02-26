package com.ciel.springcloudalibabaproducer1;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication

/**
 * 使用nacos作为注册中心
 */
@EnableDiscoveryClient

/**
 * 开启基于注解的aop
 */
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)

@ComponentScan(basePackages = "com.ciel")

@MapperScan("com.ciel.springcloudalibabacommons.mapper")

@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE)

/**
 * 开启feign调用
 */
@EnableFeignClients

/**
 * hmily 要扫描的包
 */
@ComponentScan("org.dromara.hmily")

public class SpringCloudAlibabaProducer1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAlibabaProducer1Application.class, args);
    }


//    @Bean
//    public RedisTemplate stringRedis(){
//        RedisTemplate redis = new RedisTemplate();
//        redis.setDefaultSerializer(RedisSerializer.string());
//        return redis;
//    }

}
