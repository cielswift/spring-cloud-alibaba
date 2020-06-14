package com.ciel.scatquick.config;

import com.ciel.scatquick.controller.LoginInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer   {


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new MultiRequestBodyArgumentResolver());
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
    }

    /**--------------------------------------------------------------------------------
    /**
     * 注册rest风格url
     */
    @Bean
    public FormContentFilter formContentFilter() {
        return new FormContentFilter();
    }


    @Override  //注册拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/error/**");
        //拦截的地址和排除的地址
    }


    /**
     * 跨域设置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  //允许所有跨域请求
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("Authentication"); //重要: 暴露响应头

//        前端默认只能获取以下响应头信息
//        Cache-Control
//        Content-Language
//        Content-Type
//        Expires
//        Last-Modified
//        Pragma

       // response.setHeader("access-control-expose-headers", "Authentication"); //暴露响应头

//        Access-Control-Max-Age用来指定本次预检请求的有效期，单位为秒，，在此期间不用发出另一条预检请求。
//        例如：
//        resp.addHeader("Access-Control-Max-Age", "0")，表示每次异步请求都发起预检请求，也就是说，发送两次请求。
//        resp.addHeader("Access-Control-Max-Age", "1800")，表示隔30分钟才发起预检请求。也就是说，发送两次请求
    }
}


