package com.ciel.scatquick.quartz;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {
    /**
     * jdk 原生线程池
     * @return
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){

        return new ThreadPoolExecutor(32,64,2, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(1024),
                new ThreadFactoryBuilder().setNameFormat("CIEL-JDK-POOL-%d").build());
    }
    /**
     * spring线程池
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(64);
        // 最大线程数
        taskExecutor.setMaxPoolSize(128);
        // 线程队列最大线程数
        taskExecutor.setQueueCapacity(1024);

        taskExecutor.setKeepAliveSeconds(2);
        taskExecutor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("CIEL-SPRING-POOL-%d").build());
        // 初始化线程池
        taskExecutor.initialize();

        return taskExecutor;
    }

    /**
     * 初始化线程池
     */
    @PostConstruct
    public void init() {
        System.out.println("定时任务初始化完成");
    }
}
