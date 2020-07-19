package com.ciel.scagateway.filter.controller.current;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

/**
 * ReactiveRequestContextHolder
 * 获取当前请求
 */
public class ReactiveRequestContextHolder {
    static final Class<ServerHttpRequest> CONTEXT_KEY = ServerHttpRequest.class;

    public static Mono<ServerHttpRequest> getRequest() {
        return Mono.subscriberContext()
            .map(ctx -> ctx.get(CONTEXT_KEY));
    }


}