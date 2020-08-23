package com.ciel.scareactive.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
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
import org.springframework.util.Base64Utils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.KeyPair;
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

    public static void main(String[] args) {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        RSA rsa = SecureUtil.rsa("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8CW4LynRVdSKbsFa6Cl3m4mlmO4trv/IMufrY2+qB4oFkllgU4aKrF3UPF7xEHV7QFDFAhLN8AnRvxfQCXtA1ZLAfsfJCOaO+VS0y9Z2XBvcxC9xEV7knZtOkZPIre5Z6zUkXYt7IEIFNDiwi6kvQqUVLeYpCaEwRMYuukI5a4t3atLpcoFlwgrmybEbuJ2j7QdkMTanG3O5tj7H4dEbjfFhYeY8gZhnZHqagPaG5F9CwauRL7xFvuemrvIxHim8HEb+wnZFR2rW7f9pMaPl/2RKqcxxm6AjOfv1x1hP9QYZDOSvk8RkS+7dcqTsLVjsp6A5ogb/BhTGeqcybiso1AgMBAAECggEAI4domqO7TXZffqlYCbjxuzFmluGDW22eL5dodVdAPSBuMHJT3EYx9T/uZ1alxP0DsC9Qiw9H1pOkKoZtr5D4qE4JFUQLMUYw0ULI0oskJqgzRrol+WfV3UMJlFNaiqrEaRmmyNtumsC7nbKi1koM3zVMmZEydY+9Z7ZD3U0FCNjnRR/Ob8CykPe/QFRmLJqZTeXdejAescSaw1ax3DZ0gs68F1FjrjCtTzny7xZLBE3xerUl6630Cgc44YwYY8LLh312/EelvpdrmIKFgFgNbhymK++urqi4ZvHmYZO9c1permdlXJc3uCpru4qBHnfq1QnuKyIQU1ot6AxkbvhKdQKBgQD7IAjgRtGcioK2IdaNScmWlvhcuDE88f4GDSusa5O/zs0xOrRO5Q5K7vJXfVoNFArahd4dQspN9hZ8/XOiK2LKqCVPIJ6j5u7gkY++OOE+CEfnzsNCcVZ49pHJZiPbZiuE0qFPJKq+J63JXMOm5/xIV/AqglFpd7ic4/8PTRh49wKBgQC/r+DOSiu4gHyWgq3HWTkWQKnBIP5o3VHskw1QGdKO3okDJK+q9QZoBlM2neRCvp/khKxlG+BcWykEwoZQ7BsWVoJ1Ha3N2CLMtbWFTqUdL/PQxYcnNYYrZdgMLWQ4J3PnaqM80jUbZvhtGeDkjv9fJndBYim7lHDTnP6KNveXMwKBgQCmg9de35ad9jo2Crn5dbP15qVI1dnQT6Xg3VrYOXz+8mGdAxAMQFX52fdXjAbqJSANRjbBsEf06fh/aJAEOECJoHUcwsRuafJ7dgmaWJ0CRPto+fD/Xae5DcYdhVzBXj39FK1hjS6qaCUUIC6bvbfLQIQ/q6nQBVKktYmq27QegQKBgECAiXBBSycBrWrkPF/ZaTlQtw6y8IkO+HNYJGGw3saOoaI6JRas7uPwRO4bAWVoSduWvYAkekH5tvrQDmyKlq7Um3+XZhXfRg6Gkrdx+GOy8CNvZIRQIX7W/XVTjObMd9cGbjmYLqCy7KOh0qh2af51rimFQ6/ISpMawBh4WKY7AoGAZcncXLkR7DJrw7h3JiqlyLbsghkDqTcMXxY/zkm8U5Fat7OE7yPyftUXvIaGMVyNqncM0Brq9EpYfbXhU8VzochGtpS82nYgv5Fr7jIQWV7IPHwuUTl04P9oE4PTxocgBkSCnePfGNehwcFENmo+CykT92xL41M+MBhd0Px4lc4=",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvAluC8p0VXUim7BWugpd5uJpZjuLa7/yDLn62NvqgeKBZJZYFOGiqxd1Dxe8RB1e0BQxQISzfAJ0b8X0Al7QNWSwH7HyQjmjvlUtMvWdlwb3MQvcRFe5J2bTpGTyK3uWes1JF2LeyBCBTQ4sIupL0KlFS3mKQmhMETGLrpCOWuLd2rS6XKBZcIK5smxG7ido+0HZDE2pxtzubY+x+HRG43xYWHmPIGYZ2R6moD2huRfQsGrkS+8Rb7npq7yMR4pvBxG/sJ2RUdq1u3/aTGj5f9kSqnMcZugIzn79cdYT/UGGQzkr5PEZEvu3XKk7C1Y7KegOaIG/wYUxnqnMm4rKNQIDAQAB");

        String encryptBcd = rsa.encryptBcd("夏培鑫", KeyType.PublicKey);

        System.out.println(encryptBcd);

        String decryptStr = rsa.decryptStr(encryptBcd, KeyType.PrivateKey);

        System.out.println(decryptStr);

        System.out.println(System.currentTimeMillis()-timeMillis);
    }

    @GetMapping("/")
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
