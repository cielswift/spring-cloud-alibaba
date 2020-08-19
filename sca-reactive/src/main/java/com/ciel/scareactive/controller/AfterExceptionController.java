package com.ciel.scareactive.controller;

import com.ciel.scaapi.retu.ResultReactive;
import com.ciel.scareactive.filter.ReactiveRequestContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class AfterExceptionController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResultReactive> handleError(Exception e) {
        log.error("未知异常", e);
        // 发送：未知异常异常事件
        return ReactiveRequestContextHolder.getRequest()
                .doOnSuccess(r -> publishEvent(r, e))
                .flatMap(r -> Mono.just(ResultReactive.error(e.getMessage())));

    }

    private void publishEvent(ServerHttpRequest request, Throwable error) {
        // 具体业务逻辑
        System.out.println("hello");
    }
}
