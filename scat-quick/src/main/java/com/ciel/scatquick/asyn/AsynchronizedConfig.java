package com.ciel.scatquick.asyn;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.NamedThreadFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync  //开启异步功能
@Slf4j
public class AsynchronizedConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {

//        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(20, 30,
//                1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(30));
//        return poolExecutor;

        // 自定义线程池
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(20);
        // 最大线程数
        taskExecutor.setMaxPoolSize(30);
        // 线程队列最大线程数
        taskExecutor.setQueueCapacity(1024);
        // 初始化线程池

        taskExecutor.setThreadFactory(new NamedThreadFactory("TASK-POOL-%d"));
        taskExecutor.initialize();
        taskExecutor.setKeepAliveSeconds(3);
        return taskExecutor;
    }

    /**
     * 发生异常 的处理
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

        return (ex, method, params) -> {
            log.error(ex.getMessage() + method.getName() + params);

            System.out.println(String.format("异步任务发生异常 异常信息: d%,异常方法: d% 参数 d%"
                    ,ex.getMessage(),method.getName(),params ));
        };
    }
}
