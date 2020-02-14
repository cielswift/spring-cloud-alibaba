package com.ciel.springcloudalibabaproducer2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
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

@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE)

public class SpringCloudAlibabaProducer2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAlibabaProducer2Application.class, args);
    }

}
