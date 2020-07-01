package com.ciel.scatquick.beanload;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;

/**
 * 全局bean加载的拦截方法
 *
 *  参考 org.springframework.context.support.AbstractApplicationContext.refresh()  preInstantiateSingletons
 *   getBean -> doGetBean -> createBean  已创建的bean 缓存到singletonObject (Map中)
 *  尝试给bean 创建代理bean
 *  org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#createBean(..) resolveBeforeInstantiation
 *
 *   InstantiationAwareBeanPostProcessor 是 BeanPostProcessor 的子接口
 *   SmartInstantiationAwareBeanPostProcessor 是 InstantiationAwareBeanPostProcessor  的子接口
 *------------------------------------------------------------------------------------------
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
public class BeanLoadInter  implements BeanPostProcessor,
        InstantiationAwareBeanPostProcessor,
        SmartInstantiationAwareBeanPostProcessor {

    /**
     * 初始化回调（例如InitializingBean的{@code afterPropertiesSet}或自定义的初始化方法）。 该bean将已经用属性值填充
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BEAN INIT LOAD BEFORE -> "+beanName);
        return bean;
    }

    /**
     * 该bean将已经用属性值填充
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BEAN INIT LOAD AFTER -> "+beanName);
        return bean;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 如果此方法返回一个非null对象，则Bean创建过程 会短路。唯一的后续处理是postProcessAfterInitialization
     *
     * 返回 null 继续进行默认实例化
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    /**
     * 但在发生Spring属性填充（通过显式属性或自动装配）之前
     * 这是在给定bean上执行自定义字段注入的理想回调
     * 正常返回true 返回false 也将阻止任何后续的InstantiationAwareBeanPostProcessor
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    /**
     * 在工厂应用它们之前对给定的属性值进行后处理到给定的bean
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * bean的类型；如果不可预测，则返回{@code null}
     */
    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        return beanClass;
    }

    /**
     * 返回候选构造函数，如果未指定，则为{@code null
     */
    @Override
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
        return beanClass.getConstructors();
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
