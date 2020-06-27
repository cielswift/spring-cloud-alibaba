package com.ciel.scagateway.filter.routes;

import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 路由配置规则
 */
@Configuration
public class CodeRouters {

    /**
     * 负载均衡策略
     * @return
     */
    @Bean
    public RoundRobinRule loadBalancerRule(){
        return new RoundRobinRule();
    }


    //http://127.0.0.1:5210/gateway/con/consumer/d1?name=xia
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/con/**")
                        .and()
                        // .between()
                        //.method()
                        //.query()
                        //.remoteAddr()
//                        .weight("gp1",8) //路由权重;
//                        .and()
                        .before(LocalDateTime.of(2020, 5, 13, 19, 30, 5).atZone(ZoneId.of("Asia/Shanghai")))
                        // .and()
                        // .header("X-Request-Id","\\d+")
                        .filters(f -> f.stripPrefix(1)
                                // .addResponseHeader("X-Response-Default-Foo", "Default-Bar")
                                .filter(requestRateLimiterGateway().apply(limitconfig())) //限流
                                .filter(retryGatewayFilterFactory().apply(retryConfig())) //重试
                        )
                        .uri("lb://consumer") //如果是以lb开头，则会开启负载均衡
                        .order(0)
                        .id("consumer"))

                .route(r -> r.path("/co/**")
                        .filters(f -> f.stripPrefix(2) //http://127.0.0.1:5210/gateway/co/consumer/consumer/iw //要多加一个consumer
                        )
                        .uri("lb://consumer")
                        .order(0)
                        .id("consumer2"))

                .route(r -> r.path("/p10/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(requestRateLimiterGateway().apply(limitconfig()))
                        )
                        .uri("lb://producer10")
                        .order(0)
                        .id("producer10"))

                .route(r -> r.path("/p20/**")
                        .filters(f -> f.stripPrefix(1)
                                .filter(requestRateLimiterGateway().apply(limitconfig()))
                        )
                        .uri("lb://producer20")
                        .order(0)
                        .id("producer20"))
                .build();
    }

    //重试
    @Bean
    public RetryGatewayFilterFactory retryGatewayFilterFactory() {
        return new RetryGatewayFilterFactory();
    }

    @Bean
    public RetryGatewayFilterFactory.RetryConfig retryConfig() {
        RetryGatewayFilterFactory.RetryConfig retryConfig = new RetryGatewayFilterFactory.RetryConfig();
        retryConfig.setRetries(1); //重试次数
        retryConfig.setStatuses(HttpStatus.BAD_GATEWAY); //返回哪个状态码需要进行重试，返回状态码为5XX进行重试

        RetryGatewayFilterFactory.BackoffConfig backoffConfig
                = new RetryGatewayFilterFactory.BackoffConfig();
        backoffConfig.setFirstBackoff(Duration.ofMillis(10));
        backoffConfig.setMaxBackoff(Duration.ofMillis(50));
        backoffConfig.setFactor(2);
        backoffConfig.setBasedOnPreviousValue(false);
        retryConfig.setBackoff(backoffConfig);
        return retryConfig;
    }

//限流返回429

    @Bean(name = "limitconfig")
    public RequestRateLimiterGatewayFilterFactory.Config limitconfig() {
        RequestRateLimiterGatewayFilterFactory.Config rc = new RequestRateLimiterGatewayFilterFactory.Config();
        rc.setRateLimiter(redisRateLimiter());
        rc.setKeyResolver(ipKeyResolver());
        return rc;
    }

    @Bean
    public RequestRateLimiterGatewayFilterFactory requestRateLimiterGateway() {
        return new RequestRateLimiterGatewayFilterFactory(redisRateLimiter(), ipKeyResolver());
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 1);
    }

    /**
     * 基于ip的限流
     */
    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            //获取访问者的ip地址, 通过访问者ip地址进行限流, 限流使用的是Redis中的令牌桶算法
            String ip = exchange.getRequest().getRemoteAddress().getHostString();
            String ip1 = exchange.getRequest().getHeaders().getFirst("x-forworded-for");
            return Mono.just(ip);
        };
    }
}
