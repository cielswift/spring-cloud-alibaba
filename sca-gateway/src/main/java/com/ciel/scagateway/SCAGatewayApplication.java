package com.ciel.scagateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
/**
 * 自动扫描配置Properties类,不再需要@Configuration或者@Component
 */

@ConfigurationPropertiesScan("com.ciel.scagateway.filter.config")

public class SCAGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SCAGatewayApplication.class, args);
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
                        .filters(f ->  f.stripPrefix(1)
                        // .addResponseHeader("X-Response-Default-Foo", "Default-Bar")
                         .filter(requestRateLimiterGateway().apply(limitconfig())) //限流
                         .filter(retryGatewayFilterFactory().apply(retryConfig())) //重试
                        )
                        .uri("lb://consumer")
                        .order(0)
                        .id("consumer"))

                .route(r -> r.path("/co/**")
                        .filters(f ->  f.stripPrefix(2) //http://127.0.0.1:5210/gateway/co/consumer/consumer/iw //要多加一个consumer
                        )
                        .uri("lb://consumer")
                        .order(0)
                        .id("consumer2"))

                .route(r -> r.path("/p10/**")
                        .filters(f ->  f.stripPrefix(1)
                                .filter(requestRateLimiterGateway().apply(limitconfig()))
                        )
                        .uri("lb://producer10")
                        .order(0)
                        .id("producer10"))

                .route(r -> r.path("/p20/**")
                        .filters(f ->  f.stripPrefix(1)
                                .filter(requestRateLimiterGateway().apply(limitconfig()))
                        )
                        .uri("lb://producer20")
                        .order(0)
                        .id("producer20"))

                .build();
    }

    //重试
    @Bean
    public RetryGatewayFilterFactory retryGatewayFilterFactory(){
        return new RetryGatewayFilterFactory();
    }

    @Bean
    public RetryGatewayFilterFactory.RetryConfig retryConfig(){
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
        return new RequestRateLimiterGatewayFilterFactory(redisRateLimiter(),ipKeyResolver());
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
    public RedisTemplate<String, String> redisTemplateString(RedisConnectionFactory Factory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(Factory);
        redisTemplate.setDefaultSerializer(RedisSerializer.string());
        return redisTemplate;
    }
}
