package com.ciel.scaconsumer;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.netflix.loadbalancer.RoundRobinRule;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.collections.RedisCollectionFactoryBean;
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

/**
 * oauth2授权服务器
 */
//@EnableAuthorizationServer
public class SCAConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SCAConsumerApplication.class, args);
    }

    /**
     * feign 日志打印级别
     *
     * NONE：默认的，不显示任何日志；
     * BASIC：仅记录请求方法、URL、响应状态码及执行时间；
     * HEADERS：除了BASIC中定义的信息之外，还有请求和响应的头信息；
     * FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据。
     */
    @Bean
    Logger.Level level() {
        return Logger.Level.FULL;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 负载均衡策略
     *
     * com.netflix.loadbalancer.RandomRule：从提供服务的实例中以随机的方式；
     * com.netflix.loadbalancer.RoundRobinRule：以线性轮询的方式，就是维护一个计数器，从提供服务的实例中按顺序选取，第一次选第一个，第二次选第二个，以此类推，到最后一个以后再从头来过；
     * com.netflix.loadbalancer.RetryRule：在RoundRobinRule的基础上添加重试机制，即在指定的重试时间内，反复使用线性轮询策略来选择可用实例；
     * com.netflix.loadbalancer.WeightedResponseTimeRule：对RoundRobinRule的扩展，响应速度越快的实例选择权重越大，越容易被选择；
     * com.netflix.loadbalancer.BestAvailableRule：选择并发较小的实例；
     * com.netflix.loadbalancer.AvailabilityFilteringRule：先过滤掉故障实例，再选择并发较小的实例；
     * com.netflix.loadbalancer.ZoneAwareLoadBalancer：采用双重过滤，同时过滤不是同一区域的实例和故障实例，选择并发较小的
     */
    @Bean
    public RoundRobinRule roundRobinRule(){
        return new RoundRobinRule();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *方法和参数同org.springframework.http.client.ClientHttpRequestInterceptor.intercept相同和;
     * 后面也有BlockException
     */
    @SentinelRestTemplate(blockHandler = "r2", blockHandlerClass = SCAConsumerApplication.class,
                          fallback = "r3",fallbackClass = SCAConsumerApplication.class )
    @Bean
    //@LoadBalanced //不能访问ip加端口,因为这个一个负载均衡器; 加上这个restTemplate 只能使用服务名访问;
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }


    public static SentinelClientHttpResponse r2(HttpRequest request, byte[] body,
                                                ClientHttpRequestExecution execution, BlockException ex) {
        System.err.println("rest 限流了::" + ex.getClass().getCanonicalName());
        return new SentinelClientHttpResponse("rest 限流了::");
    }

    public static SentinelClientHttpResponse r3(HttpRequest request, byte[] body,
                                                ClientHttpRequestExecution execution, BlockException ex) {
        System.err.println("rest 错误了::" + ex.getClass().getCanonicalName());
        return new SentinelClientHttpResponse("rest 错误了:: ");
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
