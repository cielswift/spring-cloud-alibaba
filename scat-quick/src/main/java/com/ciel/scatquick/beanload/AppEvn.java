package com.ciel.scatquick.beanload;

import org.springframework.context.ApplicationEvent;

/**
 * 事件类
 */
public class AppEvn extends ApplicationEvent {

    private String name;

    public AppEvn(Object source, String name) {
        super(source);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
