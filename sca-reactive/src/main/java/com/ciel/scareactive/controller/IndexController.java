package com.ciel.scareactive.controller;

import com.ciel.scaapi.retu.ResultReactive;
import com.ciel.scareactive.filter.ReactiveRequestContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Security;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Slf4j

@AllArgsConstructor
public class IndexController {

    protected RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/index")
    public Mono<ResultReactive> index() {

        //   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //  Object principal = authentication.getPrincipal();

       // Mono<ServerHttpRequest> request = ReactiveRequestContextHolder.getRequest();

       // MultiValueMap<String, String> queryParams = request.block().getQueryParams();
       // System.out.println(queryParams);

        log.info("fuck you mother");
        redisTemplate.opsForValue().setIfAbsent("xia", UUID.randomUUID().toString());
        Object xia = redisTemplate.opsForValue().get("xia");
        return Mono.just(ResultReactive.ok("success").data(xia));
    }

    @GetMapping("/many")
    public Flux<List> many(){

        return Flux.just(Stream.generate(() -> UUID.randomUUID().toString()).limit(10000).collect(Collectors.toList()));
    }

}
