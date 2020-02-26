package com.ciel.springcloudalibabaproducer2;

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

/**
 * 开启feign调用
 */
@EnableFeignClients

@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE)

/**
 * hmily 要扫描的包
 */
@ComponentScan("org.dromara.hmily")

public class SpringCloudAlibabaProducer2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAlibabaProducer2Application.class, args);
    }

//    @Bean
//    public RedisTemplate stringRedis(){
//        RedisTemplate redis = new RedisTemplate();
//        redis.setDefaultSerializer(RedisSerializer.string());
//        return redis;
//    }
}
