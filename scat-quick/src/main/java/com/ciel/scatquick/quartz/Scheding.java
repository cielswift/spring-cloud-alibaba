package com.ciel.scatquick.quartz;

import com.ciel.scatquick.beanload.AppEventPush;
import com.ciel.scatquick.beanload.AppEvn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Configuration
public class Scheding {

    @Autowired
    protected AppEventPush appEventPush;

    @Scheduled(cron = "1 5 * * * ?")
    public void tes(){
        appEventPush.sendEmail("aaaa");
    }

    @EventListener  //代替事件监听器,不用写ApplicationListener
    public void listenHello(AppEvn event) {
        System.out.println(event.getName());
    }

    /**
     * 线程池使用线程池执行, 提高效率
     */
    protected ThreadPoolExecutor poolExecutor;
    /**
     * spring线程池
     */
    protected ThreadPoolTaskExecutor taskExecutor;
    /**
     * 初始化线程池
     */
    @PostConstruct
    public void init() {
        this.poolExecutor = new ThreadPoolExecutor(64, 64,
                5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1024));


        taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(64);
        // 最大线程数
        taskExecutor.setMaxPoolSize(64);
        // 线程队列最大线程数
        taskExecutor.setQueueCapacity(1024);
        // 初始化线程池
        taskExecutor.initialize();
    }

}
