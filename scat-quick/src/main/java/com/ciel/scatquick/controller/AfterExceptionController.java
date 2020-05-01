package com.ciel.scatquick.controller;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@RestControllerAdvice
public class AfterExceptionController implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(AlertException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY) //状态码
    public Result alertException(AlertException e){
        return Result.error(502,getMessage(e));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) //状态码
    public Result accessException(AccessDeniedException e){
        return Result.error(403,"权限不足:".concat(getMessage(e)));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //状态码
    public Result globalException(Exception e){
        return Result.error(500,"服务器异常:".concat(getMessage(e)));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //状态码
    public Result paramException(MissingServletRequestParameterException e){
        return Result.error(400,"参数异常:".concat(getMessage(e)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result violation(ConstraintViolationException e){
        return Result.error(400,"参数校验异常:".concat(getMessage(e)));
    }

    private String getMessage(Exception exception){
        if(Faster.isNull(exception)){
            return "null";
        }else if(Faster.isNull(exception.getMessage())){
            return exception.getClass().getName();
        }else {
            return exception.getMessage();
        }
    }

    @InitBinder
    public void init(WebDataBinder binder, HttpServletRequest request){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
        System.out.println("参数预处理");
    }

    @Override
    public boolean supports(@Nullable MethodParameter methodParameter,
                            @Nullable Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, @Nullable MethodParameter methodParameter,@Nullable  MediaType mediaType,
                                  @Nullable Class<? extends HttpMessageConverter<?>> aClass,
                                  @Nullable ServerHttpRequest serverHttpRequest, @Nullable ServerHttpResponse serverHttpResponse) {
        //serverHttpResponse.setStatusCode(HttpStatus.OK);
        return o;
    }
}