package com.ciel.scaproducer1.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Configuration
public class WebCustomMvcConfig implements WebMvcConfigurer {


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
                //反序列化的时候如果多了其他属性,不抛出异常
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                //如果是空对象的时候,不抛异常
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                //时间格式化
                objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                converter.setObjectMapper(objectMapper);
                converters.set(i, converter);
                break;
            }
        }
    }

    /**
     * 注册rest风格url
     */
    @Bean
    public FormContentFilter formContentFilter() {
        return new FormContentFilter();
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
    }
}
