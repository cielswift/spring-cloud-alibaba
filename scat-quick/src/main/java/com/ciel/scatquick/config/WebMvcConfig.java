package com.ciel.scatquick.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ciel.scatquick.controller.LoginInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

// @PutMapping(value = "/sec/{yy}",
//            produces = {"application/toString","application/json","application/xml"})
//    /**
//     * 对于添加了 @RequestBody 和 @ResponseBody 注解的后端端点，都会经历由 HttpMessageConverter 进行的数据转换的过程。
//     * 而在 Spring 启动之初，就已经有一些默认的转换器被注册了。通过在 RequestResponseBodyMethodProcessor 中打断点，
//     * 我们可以获取到一个 converters 列表：
//     */
//
//    @Bean
//    public AbstractHttpMessageConverter<Object> toStringHttpMessageConverter(){
//        return new AbstractHttpMessageConverter<Object>
//                (new MediaType("application", "toString", StandardCharsets.UTF_8)) {
//
//            //构造函数指定媒体类型
//            @Override
//            protected boolean supports(@NonNull Class<?> clazz) {
//                return true;
//            }
//            // 从请求体封装数据 对应 RequestBody 用 String 接收
//            @Override
//            protected Object readInternal(@NonNull Class<?> clazz, @NonNull HttpInputMessage inputMessage)
//                    throws IOException, HttpMessageNotReadableException {
//                return StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8);
//            }
//
//            // 从响应体封装数据 对应 ResponseBody
//            @Override
//            protected void writeInternal(@NonNull Object o, @NonNull HttpOutputMessage outputMessage)
//                    throws IOException, HttpMessageNotWritableException {
//
//                String result = o.toString();
//                outputMessage.getBody().write(result.getBytes());
//            }
//        };
//    }
//
//    /**
//     * 配置自定义的消息转换器;注意会关闭默认转换器;简单添加使用extendMessageConverters()
//     *
//     * 通过@PutMapping(value = "/sec/{yy}",
//     *             produces = {"application/toString","application/json","application/xml"}) 配置返回类型
//     *
//     *    header头 Accept 的类型，即可获得不同形式的 Book 返回结果
//     *    ，可以是 application/toString，application/json，application/xml，都会对应各自的 HttpMessageConverter。
//     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(toStringHttpMessageConverter());
//    }


    /**
     * 添加多RequestBody解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new MultiRequestBodyResolver());
    }

    /**
     * string 消息转换器
     */
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter();
    }

    /**
     * 配置转换器 一般使用 extendMessageConverters 扩展转换器
     */
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

                //反序列化的时候如果多了其他属性,不抛出异常
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                //如果是空对象的时候,不抛异常
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

                //时间格式化
                objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));


                //全局配置序列化返回 JSON 处理
                SimpleModule simpleModule = new SimpleModule();
                //Long 类型js 精度丢失问题
                simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
                objectMapper.registerModule(simpleModule);

                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                converter.setObjectMapper(objectMapper);
                converters.set(i, converter);
                break;
            }
        }
    }

    //fast json 配置 防止//Long 类型js 精度丢失问题
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
//        FastJsonConfig fjc = new FastJsonConfig();
//        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
//        serializeConfig.put(Long.class , ToStringSerializer.instance);
//        serializeConfig.put(Long.TYPE , ToStringSerializer.instance);
//        fjc.setSerializeConfig(serializeConfig);
//        fastJsonConverter.setFastJsonConfig(fjc);
//        converters.add(fastJsonConverter);
//    }


    /**
     * 配合测试 @MatrixVariable 使 ; 不要被自动移除了：
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
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


