package com.ciel.scatquick.beanload;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 全局bean加载的拦截方法
 *
 *  参考 org.springframework.context.support.AbstractApplicationContext.refresh()  preInstantiateSingletons
 *   getBean -> doGetBean -> createBean  已创建的bean 缓存到singletonObject (Map中)
 *  尝试给bean 创建代理bean
 *  org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#createBean(..) resolveBeforeInstantiation
 *
 *  在Spring的DefaultSingletonBeanRegistry类中，你会赫然发现类上方挂着这三个Map：
 * singletonObjects 它是我们最熟悉的朋友，俗称“单例池”“容器”，缓存创建完成单例Bean的地方。
 *
 * singletonFactories 映射创建Bean的原始工厂
 *
 * earlySingletonObjects 映射Bean的早期引用，也就是说在这个Map里的Bean不是完整的，甚至还不能称之为“Bean”，只是一个Instance.
 *
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
