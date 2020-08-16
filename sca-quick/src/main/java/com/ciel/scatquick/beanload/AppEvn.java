package com.ciel.scatquick.beanload;

import org.springframework.context.ApplicationEvent;

/**
 * ApplicationEvent：事件抽象类，所有的具体事件类都得继承这个类，支持将数据设置到EventObject中：
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
