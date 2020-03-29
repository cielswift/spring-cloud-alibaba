package com.ciel.scaproducer1.controller;

import com.ciel.scaapi.retu.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
public class AfterExceptionController implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {

       return Result.error("异常$$").data(e.getClass().getName().concat(e.getMessage()==null?"NON":e.getMessage()));
    }

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter,
                                  MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return o;
    }
}