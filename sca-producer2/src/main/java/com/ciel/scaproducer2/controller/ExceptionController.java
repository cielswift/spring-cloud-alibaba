package com.ciel.scaproducer2.controller;

import com.ciel.scaapi.retu.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {

       return Result.error("异常").body(e.getClass().getName().concat(e.getMessage()==null?"NON":e.getMessage()));
    }
}
