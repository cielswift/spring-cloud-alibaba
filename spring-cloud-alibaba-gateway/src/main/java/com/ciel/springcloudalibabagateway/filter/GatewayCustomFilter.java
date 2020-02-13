package com.ciel.springcloudalibabagateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayCustomFilter {

    /**
     * 全局过滤器
     */

    @Bean
    @Order(-1)
    public GlobalFilter a() {
        return (exchange, chain) -> {

            System.out.println("第1个过滤器在请求之前执行");

            exchange.getRequest();

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

                System.out.println("第1个过滤器在请求之后执行");
            }));
        };
    }


    @Bean
    @Order(0)
    public GlobalFilter b() {

        return (exchange, chain) -> {
            System.out.println("第2个过滤器在请求之前执行");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

                System.out.println("第2个过滤器在请求之后执行");

            }));
        };

    }
}
