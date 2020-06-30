package com.ciel.scatquick.beanload;

import org.springframework.context.LifecycleProcessor;
import org.springframework.stereotype.Component;

/**
 * 生命周期组件
 */
@Component
public class LifePro implements LifecycleProcessor {

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
