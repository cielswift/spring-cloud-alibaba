package com.ciel.scaconsumer.feignext;

import feign.Logger;
import feign.slf4j.Slf4jLogger;
import org.springframework.cloud.openfeign.DefaultFeignLoggerFactory;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * feign全局配置
 *
 * FeignClientsConfiguration 是feign 的原生默认配置
 */
public class FeignGlobalConfig extends FeignClientsConfiguration {

    /**
     * feign 日志打印级别
     *
     * NONE：默认的，不显示任何日志；
     * BASIC：仅记录请求方法、URL、响应状态码及执行时间； 适合生产环境
     * HEADERS：除了BASIC中定义的信息之外，还有请求和响应的头信息；
     * FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据。
     */
    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }

    @Override
    public FeignLoggerFactory feignLoggerFactory() {

        Logger logger = new Slf4jLogger();
        FeignLoggerFactory loggerFactory = new DefaultFeignLoggerFactory(logger);
        return loggerFactory;
    }
}
