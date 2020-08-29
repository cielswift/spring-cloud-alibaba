package com.ciel.scatquick.quartz;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableScheduling //开启定时任务
@Configuration
public class TPoolSchedulingConfig implements SchedulingConfigurer {
    /**
     * 初始化线程池
     */
    @PostConstruct
    public void init() {
        System.out.println("定时任务初始化完成");
    }

    private ScheduledTaskRegistrar registrar;


    @Bean
    //@Role(BeanDefinition.ROLE_INFRASTRUCTURE) //用于标识Bean的分类
    public Runnable taskOne(RedisTemplate<String, Object> redisTemplate){

        return () -> {
            redisTemplate.opsForValue().set("xia","cc");

            System.out.println("aaaaaa");
        };
    }


    @Autowired
    @Qualifier("taskOne")
    private Runnable to;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        this.registrar = taskRegistrar;

        CronTrigger cronTrigger = new CronTrigger("10 * * * * ?");

        CronTask cronTask = new CronTask(to,cronTrigger);

        taskRegistrar.addCronTask(cronTask);

        taskRegistrar.addTriggerTask( () -> {

        },tg -> new CronTrigger("").nextExecutionTime(tg));

    }

    @Scheduled(cron = "1/30 * * * * ?")
    public void aac(){
        System.out.println("ffffff");
    }

    private static int cpus = Runtime.getRuntime().availableProcessors();

    /**
     * jdk 原生线程池
     */
   // @Bean(name = ScheduledAnnotationBeanPostProcessor.DEFAULT_TASK_SCHEDULER_BEAN_NAME)
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

}
