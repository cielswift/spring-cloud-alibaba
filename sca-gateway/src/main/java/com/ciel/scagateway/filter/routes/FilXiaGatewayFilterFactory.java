package com.ciel.scagateway.filter.routes;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * 以GatewayFilterFactory 结尾
 */
@Component
public class FilXiaGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    @Override
    public GatewayFilter apply(NameValueConfig config) {

        return (ex,chain) -> {

            String name = config.getName();
            String value = config.getValue();
            System.out.println("请求进来:"+name + value);

            ServerHttpRequest request = ex.getRequest().mutate().build();
            ServerWebExchange build = ex.mutate().request(request).build();
            return chain.filter(build);
        };
    }

}
