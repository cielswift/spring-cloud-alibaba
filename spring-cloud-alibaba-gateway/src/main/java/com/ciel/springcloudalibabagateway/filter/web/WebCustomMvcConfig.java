package com.ciel.springcloudalibabagateway.filter.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

@Configuration
public class WebCustomMvcConfig {

    /**
     * 跨域
     * 关闭webflux的cors
     */
//    @Bean
//    public CorsWebFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedMethod("*");
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsWebFilter(source);
//    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                return chain.filter(exchange);
            }
        };
    }


    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (!CorsUtils.isCorsRequest(request)) {
                return chain.filter(ctx);
            }

            HttpHeaders requestHeaders = request.getHeaders();
            ServerHttpResponse response = ctx.getResponse();
            HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
            HttpHeaders headers = response.getHeaders();

            if(StringUtils.isEmpty(headers.get(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))){
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
            }

            if(null == headers.get(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS)){
                headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
            }


            if (requestMethod != null) {
                if(StringUtils.isEmpty(headers.get(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS))){
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
                }

            }

            if(StringUtils.isEmpty(headers.get(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))){
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            }

            if(StringUtils.isEmpty(headers.get(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS))){
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
            }

            if(StringUtils.isEmpty(headers.get(HttpHeaders.ACCESS_CONTROL_MAX_AGE))){
                headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "1800");
            }

            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
            return chain.filter(ctx);
        };
    }
}
