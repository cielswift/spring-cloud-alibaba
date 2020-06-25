package com.ciel.scatquick.config;

import com.ciel.scatquick.controller.LoginInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.TimeZone;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {


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

    /**
     * 统一输出风格
     * <p>
     * extendMessageConverters 用于扩展或修改默认转换器转换器列表;
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (int i = 0; i < converters.size(); i++) {
            if (converters.get(i) instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper objectMapper = new ObjectMapper();
                //驼峰
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
                //null值字段不返回
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                //null值字段不返回
                objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
                //允许对象忽略json中不存在的属性
                objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
                //时区
                objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                converter.setObjectMapper(objectMapper);
                converters.set(i, converter);
                break;
            }
        }
    }


    /**
     * --------------------------------------------------------------------------------
     * /**
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


