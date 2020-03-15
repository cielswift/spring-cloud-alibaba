package com.ciel.scaproducer2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class WebCustomMvcConfig implements WebMvcConfigurer {


    /**
     * 对于添加了 @RequestBody 和 @ResponseBody 注解的后端端点，都会经历由 HttpMessageConverter 进行的数据转换的过程。
     * 而在 Spring 启动之初，就已经有一些默认的转换器被注册了。通过在 RequestResponseBodyMethodProcessor 中打断点，
     * 我们可以获取到一个 converters 列表：
     */

    @Bean
    public AbstractHttpMessageConverter<Object> toStringHttpMessageConverter(){
        return new AbstractHttpMessageConverter<Object>
                (new MediaType("application", "toString", StandardCharsets.UTF_8)) {

            //构造函数指定媒体类型

            @Override
            protected boolean supports(@NonNull Class<?> clazz) {
                return true;
            }
            // 从请求体封装数据 对应 RequestBody 用 String 接收
            @Override
            protected Object readInternal(@NonNull Class<?> clazz, @NonNull HttpInputMessage inputMessage)
                    throws IOException, HttpMessageNotReadableException {
                return StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
            }

            // 从响应体封装数据 对应 ResponseBody
            @Override
            protected void writeInternal(@NonNull Object o, @NonNull HttpOutputMessage outputMessage)
                    throws IOException, HttpMessageNotWritableException {

                String result = o.toString();
                outputMessage.getBody().write(result.getBytes());
            }
        };
    }

    /**
     * 配置自定义的消息转换器
     *
     * 通过@PutMapping(value = "/sec/{yy}",
     *             produces = {"application/toString","application/json","application/xml"}) 配置返回类型
     *
     *    header头 Accept 的类型，即可获得不同形式的 Book 返回结果
     *    ，可以是 application/toString，application/json，application/xml，都会对应各自的 HttpMessageConverter。
     */

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(toStringHttpMessageConverter());
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
                .allowedHeaders("*");
    }
}
