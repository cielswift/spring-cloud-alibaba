package com.ciel.scatquick.beanload;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.*;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * spring 的 bean 加载流程
 *
 * 参考 org.springframework.context.support.AbstractApplicationContext.refresh()  registerBeanPostProcessors
 */
@Component
public class MyBean implements InitializingBean, DisposableBean, //加载 //销毁

        //实现该接口后，当所有单例 bean 都初始化完成以后， 容器会回调该接口的方法 afterSingletonsInstantiated。
        //主要应用场合就是在所有单例 bean 创建完成之后，可以在该回调中做一些事情
        SmartInitializingSingleton,

        //带Aware 都是感知器
        BeanNameAware , //bean Name 感知
        EnvironmentAware, //获得系统内的所有参数
        BeanClassLoaderAware, //bean加载 感知
        BeanFactoryAware, //factor
        ApplicationContextAware,
        MessageSourceAware,
        ApplicationEventPublisherAware, //发布事件
        ResourceLoaderAware {// 用于获取ResourceLoader的一个扩展类，
    // ResourceLoader可以用于获取classpath内所有的资源对象，可以扩展此类来拿到ResourceLoader对象  {

    /**
     * Aware  //Aware接口也是为了能够感知到自身的一些属性。
     * 比如实现了ApplicationContextAware接口的类，能够获取到ApplicationContext
     * 实现BeanNameAware 可以获取 bean的名称
     * 实现了BeanFactoryAware接口的类，能够获取到BeanFactory对象
     *
     * 参考 org.springframework.context.support.ApplicationContextAwareProcessor.invokeAwareInterfaces(..)方法
     */

    /**
     *
    private Cik cik;

    public MEBean(Cik cik){
        this.cik=cik;
    }
     */

    public MyBean(){ //多个构造函数 优先加载无参构造函数
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("属性 set 方法执行之后");
    }

    /**
     * 是在postProcessBeforeInitialization之后，InitializingBean.afterPropertiesSet之前
     *
     */
    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct 方法初始化");
    }

    @PreDestroy
    public void dead() {
        System.out.println("@PreDestroy 销毁方法");
    }


    @Override
    public void destroy() throws Exception {
        System.out.println("销毁方法");
    }

    @Override
    public void afterSingletonsInstantiated() {

        System.err.println("===================当所有单例 bean 都初始化完成以后 bean 加载完成 后续处理=======================");
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void setBeanName(String s) {
        //依赖注入一旦结束，BeanNameAware.setBeanName()会被调用，它设置该 bean 在 Bean Factory 中的名称
        System.out.println("bean的name属性:"+s);
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("bean加载器:"+classLoader.getClass().getName());
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

    @Override
    public void setEnvironment(Environment environment) {

    }
}
