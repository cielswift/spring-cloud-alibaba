package com.ciel.scatquick.controller;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.sun.beans.editors.IntegerEditor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 全局异常处理 @RestControllerAdvice  全局返回处理 ResponseBodyAdvice 全局参数预处理@InitBinder
 */

@RestControllerAdvice
@Slf4j
public class AfterGlobalExceptionReturnController implements ResponseBodyAdvice<Object> {

    /**
     * 参数预处理
     */
    @InitBinder
    public void initBind(WebDataBinder binder, HttpServletRequest request) {

        if ("date".equals(binder.getObjectName())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
            System.out.println("===PARAM ADVANCE ACTION===");
        }
        if ("type".equals(binder.getObjectName())) {
            binder.registerCustomEditor(Integer.class, new IntegerEditor(){
                @Override
                public void setAsText(String text) throws IllegalArgumentException {
                    super.setAsText(text);
                }
            });
            System.out.println("===PARAM ADVANCE ACTION===");
        }
    }

    // @InitBinder(“b”) 注解表示该方法用来处理和Book和相关的参数,在方法中,给参数添加一个 b 前缀,即请求参数要有b前缀.
//    @InitBinder("bb")
//    public void b(WebDataBinder binder) {
//        binder.setFieldDefaultPrefix("bb.");
//    }

    @ExceptionHandler(AlertException.class) //自定义异常
    @ResponseStatus(HttpStatus.BAD_GATEWAY) //状态码
    public Result alertException(AlertException e) {
        return Result.error(502, "CUSTOM EXCEPTION->".concat(getMessage(e)));
    }

    @ExceptionHandler(AccessDeniedException.class) //权限不足异常
    @ResponseStatus(HttpStatus.FORBIDDEN) //状态码
    public Result accessException(AccessDeniedException e) {
        return Result.error(403, "PERMISSIONS ERROR->".concat(getMessage(e)));
    }

    @ExceptionHandler(Exception.class) //服务器未知异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result globalException(Exception e) {
        e.printStackTrace();
        return Result.error(500, "SERVER UNKNOWN EXCEPTION->".concat(getMessage(e)));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class) //参数绑定异常
    @ResponseStatus(HttpStatus.BAD_REQUEST) //状态码
    public Result paramException(MissingServletRequestParameterException e) {
        return Result.error(400, "PARAM BIND EXCEPTION->".concat(getMessage(e)));
    }

    @ExceptionHandler(ConstraintViolationException.class) //参数校验异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result violation(ConstraintViolationException e) {
        return Result.error(400, "PARAM VALID EXCEPTION->".concat(getMessage(e)));
    }

    private String getMessage(Exception exception) {
        if (Faster.isNull(exception)) {
            return "EXCEPTION IS NULL";
        } else if (Faster.isNull(exception.getMessage())) {
            return "NOT FIND EXCEPTION MESSAGE;EXCEPTION CLASS NAME->".concat(exception.getClass().getName());
        } else {
            return exception.getMessage();
        }
    }


    @Override
    public boolean supports(@Nullable MethodParameter methodParameter,
                            @Nullable Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, @Nullable MethodParameter methodParameter, @Nullable MediaType mediaType,
                                  @Nullable Class<? extends HttpMessageConverter<?>> aClass,
                                  @Nullable ServerHttpRequest serverHttpRequest,
                                  @Nullable ServerHttpResponse serverHttpResponse) {
        //serverHttpResponse.setStatusCode(HttpStatus.OK);
        return o;
    }
}