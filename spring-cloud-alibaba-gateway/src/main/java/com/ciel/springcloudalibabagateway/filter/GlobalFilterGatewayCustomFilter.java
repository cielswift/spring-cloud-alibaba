//package com.ciel.springcloudalibabagateway.filter;
//
//import com.alibaba.fastjson.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//@Configuration
//public class GlobalFilterGatewayCustomFilter {
//
//    /**
//     * 全局过滤器
//     */
//
//    @Bean
//    @Order(-1)
//    public GlobalFilter a() {
//        return (exchange, chain) -> {
//
//            //  ServerWebExchange //请求上下文
//            System.out.println("第1个过滤器在请求之前执行");
//
//           // List<String> authentication = exchange.getRequest().getQueryParams().get("Authentication");
//
//            List<String> token = exchange.getRequest().getHeaders().get("Authentication");
//            token = List.of("123456");
//            if (null != token && !token.isEmpty()) {
//
//                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//
//                    System.out.println("第1个过滤器在请求之后执行");
//                }));
//
//            } else {
//
//                ServerHttpResponse response = exchange.getResponse();
//                JSONObject message = new JSONObject();
//
//                message.put("status", -1);
//                message.put("data", "鉴权失败");
//
//                byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
//
//                DataBuffer buffer = response.bufferFactory().wrap(bits);
//                response.setStatusCode(HttpStatus.UNAUTHORIZED); //设置状态码
//                //指定编码，否则在浏览器中会中文乱码
//                response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
//                return response.writeWith(Mono.just(buffer));
//
//                //return response.setComplete();  //请求已经结束
//            }
//
//        };
//    }
//
//
//    @Bean
//    @Order(0)
//    public GlobalFilter b() {
//
//        return (exchange, chain) -> {
//            System.out.println("第2个过滤器在请求之前执行");
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//
//                System.out.println("第2个过滤器在请求之后执行");
//
//            }));
//        };
//
//    }
//}
