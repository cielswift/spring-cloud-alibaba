package com.ciel.springcloudalibabaproducer1.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public Object error(Exception e) {

        return Map.of("MSG","发生了异常:".concat(e.getClass().getName()).concat(e.getMessage()==null?"NON":e.getMessage()));

    }
}
