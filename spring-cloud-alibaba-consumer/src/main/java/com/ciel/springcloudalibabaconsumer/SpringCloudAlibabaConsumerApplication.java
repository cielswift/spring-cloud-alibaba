package com.ciel.springcloudalibabaconsumer;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@SpringBootApplication

/**
 * 使用nacos作为注册中心
 */
@EnableDiscoveryClient

/**
 * 开启基于注解的aop
 */
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
/**
 * 开启定时任务
 */
@EnableScheduling

/**
 * 开启feign调用
 */
@EnableFeignClients


public class SpringCloudAlibabaConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAlibabaConsumerApplication.class, args);
    }

    /**
     *方法和参数同org.springframework.http.client.ClientHttpRequestInterceptor.intercept相同和;
     * 后面也有BlockException
     */
    @SentinelRestTemplate(blockHandler = "r2", blockHandlerClass = SpringCloudAlibabaConsumerApplication.class,
                          fallback = "r3",fallbackClass = SpringCloudAlibabaConsumerApplication.class )
    @Bean
    //@LoadBalanced //不能访问ip加端口,因为这个一个负载均衡器; 加上这个restTemplate 只能使用服务名访问;
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }


    public static SentinelClientHttpResponse r2(HttpRequest request, byte[] body,
                                                ClientHttpRequestExecution execution, BlockException ex) {
        System.err.println("Oops: " + ex.getClass().getCanonicalName());
        return new SentinelClientHttpResponse("custom block info");
    }

    public static SentinelClientHttpResponse r3(HttpRequest request, byte[] body,
                                                ClientHttpRequestExecution execution, BlockException ex) {
        System.err.println("fallback: " + ex.getClass().getCanonicalName());
        return new SentinelClientHttpResponse("custom fallback info");
    }

}
