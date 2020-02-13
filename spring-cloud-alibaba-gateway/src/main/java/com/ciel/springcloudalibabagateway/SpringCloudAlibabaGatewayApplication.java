package com.ciel.springcloudalibabagateway;

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

public class SpringCloudAlibabaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAlibabaGatewayApplication.class, args);
    }

}
