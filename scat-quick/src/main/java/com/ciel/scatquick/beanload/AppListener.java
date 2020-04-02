package com.ciel.scatquick.beanload;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 事件监听器
 */
@Component
public class AppListener implements ApplicationListener<AppEvn> {

    @Override
    public void onApplicationEvent(AppEvn appEvn) {
        System.out.println(appEvn.getName());
    }
}
