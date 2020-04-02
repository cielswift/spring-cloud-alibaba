package com.ciel.scaapi.converters.beanload;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.*;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * spring 的 bean 加载流程
 */
@Component
public class MEBeanLoad implements BeanPostProcessor, InitializingBean, //加载
        DisposableBean, //销毁
        BeanNameAware , //bean Name
        BeanClassLoaderAware, //加载器
        BeanFactoryAware, //factor
        ApplicationContextAware,
        MessageSourceAware,
        ApplicationEventPublisherAware, //发布事件
        ResourceLoaderAware
{


    @Autowired
    private Cik cik;

    //@EventListener
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        System.out.println("BeanPostProcessor before");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        System.out.println("BeanPostProcessor after");
        return bean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean");
    }


    @PostConstruct
    public void init() {

        System.out.println("PostConstruct init");
    }

    /**
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    @PreDestroy
    public void dead() {
        System.out.println("PreDestroy dead");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy dead");
    }

    /**
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    @Override
    public void setBeanName(String s) {

        //依赖注入一旦结束，BeanNameAware.setBeanName()会被调用，它设置该 bean 在 Bean Factory 中的名称
        System.out.println("setBeanName  "+s);
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        //为 bean 实例提供类加载器，
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        // 会被调用为 bean 实例提供其所拥有的 factory
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //同上，在BeanFactory 和 ApplicationContext 的区别 中已明确说明
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        //获取 Message Source 相关文本信息
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        //发布事件
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        //获取资源加载器，这样获取外部资源文件
    }
}
