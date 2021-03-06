package com.ciel.scaconsumer.feignext;

import com.ciel.scaapi.util.SysUtils;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * 通过实现RequestInterceptor接口,完成对所有的Feign请求,动态设置Header
 *
 * 值得注意的一点是FeignInterceptor如果注入到Springboot容器的话会全局生效
 * , 就是说及时没有指定configuration也会对全局feign接口生效;
 * 一般通过@FeignClient的configuration = FeignInterceptor.class 单独设置
 *
 */
//@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate){

        if(requestTemplate.url().contains("producer20")){

            //spring 获取当前请求的request对象;
            HttpServletRequest request = SysUtils.currentRequest();

            //spring 获取当前请求的response对象
            //HttpServletResponse response = requestAttributes.getResponse();

            String authentication = request.getHeader("Authentication");
            requestTemplate.header("Authentication", authentication);
        }
    }
}
