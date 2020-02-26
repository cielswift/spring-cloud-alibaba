package com.ciel.springcloudalibabaconsumer.controller;

import com.ciel.springcloudalibabaapi.exception.AlertException;
import com.ciel.springcloudalibabaapi.retu.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class AftermathController implements ResponseBodyAdvice<Object> {


    @ExceptionHandler(AlertException.class)
    public Result error(AlertException e) {

        return Result.error("异常::>>"+e.getMessage()==null?"null":e.getMessage());
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {


//        ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) serverHttpRequest;
//        ServletServerHttpResponse serverResponse = (ServletServerHttpResponse) serverHttpResponse;

        // 对于未添加跨域消息头的响应进行处理
//        HttpServletRequest request = serverRequest.getServletRequest();
//        HttpServletResponse response = serverResponse.getServletResponse();
//        String originHeader = "Access-Control-Allow-Origin";
//        if (!response.containsHeader(originHeader)) {
//            String origin = request.getHeader("Origin");
//            if (origin == null) {
//                String referer = request.getHeader("Referer");
//                if (referer != null) {
//                    origin = referer.substring(0, referer.indexOf("/", 7));
//                }
//            }
//            response.setHeader("Access-Control-Allow-Origin", origin);
//        }
//
//        String allowHeaders = "Access-Control-Allow-Headers";
//        if (!response.containsHeader(allowHeaders)) {
//            response.setHeader(allowHeaders, request.getHeader(allowHeaders));
//        }
//
//        String allowMethods = "Access-Control-Allow-Methods";
//        if (!response.containsHeader(allowMethods)) {
//            response.setHeader(allowMethods, "GET,PUT,POST,DELETE,OPTIONS");
//        }
//
//        String exposeHeaders = "access-control-expose-headers";
//        if (!response.containsHeader(exposeHeaders)) {
//            response.setHeader(exposeHeaders, SysConst.HEAD_TOKEN);
//        }

          return body;
    }
}
