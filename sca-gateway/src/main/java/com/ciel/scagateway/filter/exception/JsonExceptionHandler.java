package com.ciel.scagateway.filter.exception;

import com.alibaba.fastjson.JSON;
import com.ciel.scaapi.retu.Result;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component

/**
 * WebExceptionHandler是Spring-WebFlux的异常处理器顶层接口，
 * 因此追溯到子类可以追踪到DefaultErrorWebExceptionHandler是Spring Cloud Gateway的全局异常处理器，
 * 配置类是ErrorWebFluxAutoConfiguration
 */

@Getter
@Setter
public class JsonExceptionHandler implements ErrorWebExceptionHandler {

    @Autowired
    ObjectProvider<List<ViewResolver>> viewResolversProvider;

    @Autowired
    ServerCodecConfigurer serverCodecConfigurer;


    @PostConstruct
    public void init(){
        setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        setMessageWriters(serverCodecConfigurer.getWriters());
        setMessageReaders(serverCodecConfigurer.getReaders());
    }

    private static final Logger log = LoggerFactory.getLogger(JsonExceptionHandler.class);

    /**
     * MessageReader
     */
    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();

    /**
     * MessageWriter
     */
    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

    /**
     * ViewResolvers
     */
    private List<ViewResolver> viewResolvers = Collections.emptyList();

    /**
     * 存储处理异常后的信息
     */
    private ThreadLocal<Map<String,Object>> exceptionHandlerResult = new ThreadLocal<>();


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        /**
         * 封装响应体,此body可修改为自己的jsonBody
         */
        Map<String,Object> result = new HashMap<>(1<<3);
        result.put("code",500);
        result.put("body",String.format("异常 类型-> %s 信息-> %s",ex.getClass().getName(),ex.getMessage()));


        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        exceptionHandlerResult.set(result);

        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);


        DataBuffer buffer = exchange.getResponse().bufferFactory()
                .wrap(JSON.toJSONString(Result.error("全局异常")).getBytes(StandardCharsets.UTF_8));

        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST); //设置状态码
        exchange.getResponse().getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return exchange.getResponse().writeWith(Mono.just(buffer));

    }

}