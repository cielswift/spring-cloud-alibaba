package com.ciel.scatquick.beanload;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * spring 的 bean 加载流程
 */
@Component
public class MyBean implements InitializingBean, DisposableBean, //加载 //销毁
        BeanNameAware , //bean Name
        BeanClassLoaderAware, //加载器
        BeanFactoryAware, //factor
        ApplicationContextAware,
        MessageSourceAware,
        ApplicationEventPublisherAware, //发布事件
        ResourceLoaderAware {

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
}
