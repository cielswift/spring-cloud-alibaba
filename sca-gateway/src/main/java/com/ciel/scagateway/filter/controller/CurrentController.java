package com.ciel.scagateway.filter.controller;

import com.ciel.scagateway.filter.controller.current.ReactiveRequestContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin //允许跨域调用

@AllArgsConstructor //在构造函数里注入属性参数
@Slf4j //直接注入日志对象
public class CurrentController {

    @GetMapping("/test2")
    @ResponseBody
    public Mono<String> test2() {
        WebClient webClient = WebClient.builder()
                .filter(testFilterFunction())
                .baseUrl("https://www.baidu.com")
                .build();

        return webClient.get().uri("").retrieve().bodyToMono(String.class);
    }


    private ExchangeFilterFunction testFilterFunction() {
        return (request, next) -> ReactiveRequestContextHolder.getRequest()
                .flatMap(r -> {
                    ClientRequest clientRequest = ClientRequest.from(request)
                            .headers(headers -> headers.set(HttpHeaders.USER_AGENT, r.getHeaders().getFirst(HttpHeaders.USER_AGENT)))
                            .build();
                    return next.exchange(clientRequest);
                });
    }


}
