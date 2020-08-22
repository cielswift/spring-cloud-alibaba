package com.ciel.scareactive.controller;

import com.ciel.scaapi.retu.ResultReactive;
import com.ciel.scareactive.aop.Logto;
import com.ciel.scareactive.es.nat.EsTemplate;
import com.ciel.scareactive.filter.ReactiveRequestContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
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

    protected RestHighLevelClient restClient;

    @GetMapping("/index")
    @Logto
    public Mono<ResultReactive> index() throws IOException {

        //   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //  Object principal = authentication.getPrincipal();

        // Mono<ServerHttpRequest> request = ReactiveRequestContextHolder.getRequest();


        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // searchSourceBuilder.query(QueryBuilders.termQuery("name", "安娜"));
        searchSourceBuilder.query(QueryBuilders.termsQuery("name", "安娜","安妮"));

        // 创建查询请求对象，将查询对象配置到其中
        SearchRequest searchRequest = new SearchRequest(EsTemplate.SCA_GIRLS);
        searchRequest.source(searchSourceBuilder);
        // 执行查询，然后处理响应结果
        SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);

        redisTemplate.opsForValue().setIfAbsent("xia", UUID.randomUUID().toString());
        Object xia = redisTemplate.opsForValue().get("xia");


        return Mono.just(ResultReactive.ok("success").data(searchResponse.getHits().getHits()));
    }

    @GetMapping("/many")
    public Flux<String> many() {
        return Flux.fromIterable(Stream.generate(() -> UUID.randomUUID().toString())
                .limit(10000).collect(Collectors.toList()));
    }

}
