package com.ciel.scatquick.quartz;

import com.alibaba.fastjson.JSON;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.JWTUtils;
import com.ciel.scatquick.beanload.AppEventPush;
import com.ciel.scatquick.beanload.AppEvn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Configuration
public class TaskConfig {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Autowired
    protected ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    protected ThreadPoolTaskExecutor taskExecutor;

    /**
     * 事件发布器 自定义
     */
    @Autowired
    protected AppEventPush appEventPush;

    @Autowired
    protected ApplicationContext applicationContext;

    /**
     * 事件发布器 spring
     */
    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;

    /**
     * 发布事件
     */
    @Scheduled(cron = "1/30 * * * 1 ?")
    public void tes(){
        applicationEventPublisher.publishEvent(new AppEvn(applicationContext,"appEvn发布事件"));
        applicationContext.publishEvent(new AppEvn(applicationContext,"app发布事件"));
        appEventPush.sendEmail("japan tokyo");
    }

    /**
     * 代替事件监听器,不用写ApplicationListener
     */
    @EventListener(AppEvn.class)
    @Order(1) //监听顺序
    public void listenHello(AppEvn event) {

        System.out.println("当前线程:"+Thread.currentThread().getName());
        System.out.println(String.format("@EventListener 收到事件: 名称%s ,源%s ",
                event.getName(),event.getSource().getClass().getName()));
    }

    @Autowired
    protected RestTemplate restTemplate;


    @Scheduled(cron = "1/1 * * * 10 ?")
    public void rest() throws InterruptedException, ExecutionException, TimeoutException {
        Result result = threadPoolExecutor.submit(() -> {
            String ali = restTemplate.getForObject("http://120.27.69.29:3000/", String.class);
            String baid = restTemplate.getForObject("http://106.12.213.120:3000/", String.class);
            Result data = Result.ok().data(ali + baid);
            String token = JWTUtils.createToken(data);
            String md5 = JWTUtils.md5(token.getBytes());
            redisTemplate.opsForValue().set(md5,token);
            return data;
        }).get(1, TimeUnit.SECONDS);
        System.out.println(JSON.toJSONString(result));
    }
}
