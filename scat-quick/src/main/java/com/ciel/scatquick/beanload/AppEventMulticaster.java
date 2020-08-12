package com.ciel.scatquick.beanload;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 事件广播器
 */
@Component("applicationEventMulticaster")
public class AppEventMulticaster extends SimpleApplicationEventMulticaster implements ApplicationEventMulticaster {

    /**
     * multicastEvent方法 executor不为空，监听器就会被异步调用，所以如果需要异步只需要让executor不为空就可以了，
     *      但是默认情况下executor是空的
     *
     *AbstractApplicationContext# initApplicationEventMulticaster 初始化广播器
     *   判断有没有applicationEventMulticaster的bean ,有就设置广播器
     */

    //事件监听器异步执行
    public AppEventMulticaster(@Qualifier("jdkThreadPoolExecutor") ThreadPoolExecutor poolExecutor){
        setTaskExecutor(poolExecutor);
    }



    public void test(){

        ApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

        //添加事件监听器
        eventMulticaster.addApplicationListener(new AppListener());

        //广播事件
        eventMulticaster.multicastEvent(new AppEvn(this, "make"));
    }

}
