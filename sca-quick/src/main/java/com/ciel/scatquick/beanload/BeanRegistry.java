package com.ciel.scatquick.beanload;

import com.xia.bean.Xiapeixinfks;
import com.xia.config.EnableXiapeixnTest;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * 是 BeanFactoryPostProcessor 的实现类
 * 可以动态注册bean
 */
@Configuration
public class BeanRegistry implements BeanDefinitionRegistryPostProcessor, PriorityOrdered, Ordered {

    /**
     * (先执行实现) PriorityOrdered
     * (后执行)Ordered ,
     * 最后执行没有实现顺序接口的;
     * <p>
     * 参考 org.springframework.context.support.AbstractApplicationContext.refresh()  invokeBeanFactoryPostProcessors
     *
     * 从容器中找出所有的后置处理器的名称
     * 对后置处理器进行分类，分为PriorityOrdered、Ordered和regular三类
     * 将实现PriorityOrdered接口的BeanPostProcessors实例化并注册到容器
     * 将实现Ordered接口的BeanPostProcessors实例化并注册到容器
     * 将常规的BeanPostProcessors实例化并注册到容器
     * 从新将所有内部的BeanPostProcessors注册到容器
     * 重新注册ApplicationListenerDetector后置处理器
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 先执行postProcessBeanDefinitionRegistry方法
     * 在执行postProcessBeanFactory方法
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        // 扫描自定义注解 EnableXiapeixnTest 的 bean
//        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
//        // bean 的名字生成规则在AnnotationBeanNameGenerator
//        scanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
//        // 设置哪些注解的扫描
//        scanner.addIncludeFilter(new AnnotationTypeFilter(EnableXiapeixnTest.class));
//        scanner.scan("com.ciel");

        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(Xiapeixinfks.class);   //设置类
        definition.setScope("singleton");    //设置scope
        definition.setLazyInit(false);   //设置是否懒加载
        definition.setAutowireCandidate(true);  //设置是否可以被其他对象自动注入

        MutablePropertyValues mpv = new MutablePropertyValues(); // 给属性赋值
        mpv.add("name", "xiapeixinfks");
        mpv.add("age", 24);
        definition.setPropertyValues(mpv);

        registry.registerBeanDefinition("xiapeixinfks", definition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        Xiapeixinfks bean = beanFactory.getBean(Xiapeixinfks.class);

        System.out.println("自定义注入的bean:" + bean);
    }
}
