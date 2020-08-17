package com.ciel.scatquick.quartz;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableScheduling //开启定时任务
@Configuration
public class ThreadPoolConfig {

    private static int cpus = Runtime.getRuntime().availableProcessors();
    /**
     * jdk 原生线程池
     */
    @Bean("jdkThreadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(cpus,cpus,2, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(1 << 12),
                new ThreadFactoryBuilder().setNameFormat("CIEL-JDK-POOL-%d").build(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    /**
     * spring线程池
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(cpus);
        // 最大线程数
        taskExecutor.setMaxPoolSize(cpus);
        // 线程队列最大线程数
        taskExecutor.setQueueCapacity(1 << 12);
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
