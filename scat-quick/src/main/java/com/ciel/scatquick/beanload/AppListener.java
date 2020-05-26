package com.ciel.scatquick.beanload;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * ApplicationListener：事件监听器接口，定义通用方法onApplicationEvent：
 * ApplicationEventMulticaster：事件广播器接口，用于事件监听器的注册和事件的广播
 * ApplicationEventPublisher：事件发布者，调用ApplicationEventMulticaster中的multicastEvent方法触发广播器持有的监听器集合执行onApplicationEvent方法，从而完成事件发布
 */
@Component
public class AppListener implements ApplicationListener<AppEvn> {

    @Override
    public void onApplicationEvent(AppEvn appEvn) {
        System.out.println(appEvn.getName());
    }
}
