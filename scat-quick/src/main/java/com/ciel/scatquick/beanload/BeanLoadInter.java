package com.ciel.scatquick.beanload;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 全局bean加载的拦截方法
 */
@Component
public class BeanLoadInter  implements BeanPostProcessor  {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        System.out.println("BEAN INIT LOAD BEFORE -> "+beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BEAN INIT LOAD AFTER -> "+beanName);
        return bean;
    }
}
