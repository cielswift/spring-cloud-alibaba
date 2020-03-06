package com.ciel.scaconsumer.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebCustomMvcConfig implements WebMvcConfigurer {

    protected RedisTemplate<String, String> redisTemplate;

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

    @Override  //注册拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new InterceptorMain(redisTemplate)).addPathPatterns("/**").excludePathPatterns("/error/**");
        //拦截的地址和排除的地址
    }
}
