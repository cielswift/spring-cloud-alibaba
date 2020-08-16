package com.ciel.scareactive.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * ReactiveRequestContextFilter
 *
 * @author L.cm
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveRequestContextFilter implements WebFilter {

    //这里为支持的请求头，如果有自定义的header字段请自己添加（不知道为什么不能使用*）
    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,token,username,client";
    private static final String ALLOWED_METHODS = "*";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_Expose = "*";
    private static final String MAX_AGE = "18000L";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (CorsUtils.isCorsRequest(request)) {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
            headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
            headers.add("Access-Control-Max-Age", MAX_AGE);
            headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
            headers.add("Access-Control-Expose-Headers", ALLOWED_Expose);
            headers.add("Access-Control-Allow-Credentials", "true");
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
            }
        }

        return chain.filter(exchange)
                .subscriberContext(ctx -> ctx.put(ReactiveRequestContextHolder.CONTEXT_KEY, request));
    }


}