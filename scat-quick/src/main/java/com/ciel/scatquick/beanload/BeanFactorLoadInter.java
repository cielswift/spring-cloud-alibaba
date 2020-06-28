package com.ciel.scatquick.beanload;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 全局bean factor加载的拦截方法
 */
@Component
public class BeanFactorLoadInter implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        System.out.println(beanFactory.getBeanDefinitionNames());
    }
}
