package com.ciel.springcloudalibabaproducer2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication

/**
 * 使用nacos作为注册中心
 */
@EnableDiscoveryClient

/**
 * 开启基于注解的aop
 */
@EnableAspectJAutoProxy


public class SpringCloudAlibabaProducer2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAlibabaProducer2Application.class, args);
    }

}
