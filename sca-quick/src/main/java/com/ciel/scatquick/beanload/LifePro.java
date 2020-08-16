package com.ciel.scatquick.beanload;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.LifecycleProcessor;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 生命周期组件
 */
@Component
public class LifePro implements LifecycleProcessor {
    public static void main(String[] args) {


        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(64, 64,
                        0, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        new ThreadFactoryBuilder().setNameFormat("THREAD-POOL-%s").build(),
                        new ThreadPoolExecutor.DiscardPolicy());
    }

    @Override
    public void onRefresh() {
        System.out.println("onRefresh");
    }

    @Override
    public void onClose() {
        System.out.println("onClose");
    }

    @Override
    public void start() {
        System.out.println("start");
    }

    @Override
    public void stop() {
        System.out.println("stop");
    }

    @Override
    public boolean isRunning() {
        System.out.println("isRunning");
        return false;
    }

}
