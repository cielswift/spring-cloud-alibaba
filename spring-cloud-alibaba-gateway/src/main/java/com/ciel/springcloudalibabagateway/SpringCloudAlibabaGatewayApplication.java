package com.ciel.springcloudalibabagateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.net.URLClassLoader;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

//http://127.0.0.1:5210/gateway/con/consumer/d1?name=xia
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                .route(r -> r.path("/con/**")
                        .and()
                        .before(LocalDateTime.of(2020, 5, 13, 19, 30, 5).atZone(ZoneId.of("Asia/Shanghai")))
                       // .and()
                       // .header("X-Request-Id","\\d+")
                        .filters(f ->  f.stripPrefix(1)
                        // .addResponseHeader("X-Response-Default-Foo", "Default-Bar")
                         .filter(requestRateLimiterGateway().apply(limitconfig()))
                        )
                        .uri("lb://consumer")
                        .order(0)
                        .id("consumer"))

                .build();
    }

//返回429

    @Bean(name = "limitconfig")
    public RequestRateLimiterGatewayFilterFactory.Config limitconfig(){
        RequestRateLimiterGatewayFilterFactory.Config rc = new RequestRateLimiterGatewayFilterFactory.Config();
        rc.setRateLimiter(redisRateLimiter());
        rc.setKeyResolver(ipKeyResolver());
        return rc;
    }

    @Bean
    public RedisRateLimiter redisRateLimiter(){
       return new RedisRateLimiter(1, 1);
    }

    @Bean
    public RequestRateLimiterGatewayFilterFactory requestRateLimiterGateway(){
        return  new RequestRateLimiterGatewayFilterFactory(redisRateLimiter(),ipKeyResolver());
    }

    //定义一个KeyResolver
    @Bean
    public KeyResolver ipKeyResolver(){
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                //获取访问者的ip地址, 通过访问者ip地址进行限流, 限流使用的是Redis中的令牌桶算法
                String hostString = exchange.getRequest().getRemoteAddress().getHostString();
                return Mono.just(hostString);
            }
        };

      //  return (exchange) -> Mono.just(exchange.getRequest().getRemoteAddress().getHostString());
    }

    @Bean //redis string
    public RedisTemplate redisTemplateString(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(RedisSerializer.string());
        return redisTemplate;
    }
}
