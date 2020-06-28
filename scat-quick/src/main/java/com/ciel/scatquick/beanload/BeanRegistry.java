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
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * 是 BeanFactoryPostProcessor 的实现类
 * <p>
 * 可以动态注册bean
 */
@Configuration
public class BeanRegistry implements BeanDefinitionRegistryPostProcessor {

   protected volatile Object filed;

    /**
     * 先执行postProcessBeanDefinitionRegistry方法
     *     在bean 保存加载 但是没有初始化
     * 在执行postProcessBeanFactory方法
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        // 扫描自定义注解
//        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
//        // bean 的名字生成规则在AnnotationBeanNameGenerator
//        scanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
//        // 设置哪些注解的扫描
//          scanner.addIncludeFilter(new AnnotationTypeFilter(EnableXiapeixnTest.class));
//         scanner.scan("com.ciel");
        // 注意helloWord已经注册到容器中. 细看AnnotationBeanNameGenerator 的

        GenericBeanDefinition definition = new GenericBeanDefinition();
        //设置类
        definition.setBeanClass(Xiapeixinfks.class);
        //设置scope
        definition.setScope("singleton");
        //设置是否懒加载
        definition.setLazyInit(false);
        //设置是否可以被其他对象自动注入
        definition.setAutowireCandidate(true);

        // 给属性赋值
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("name", "xiapeixinfks");
        mpv.add("age", 24);
        definition.setPropertyValues(mpv);

        registry.registerBeanDefinition("xiapeixinfks", definition);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        filed = beanFactory.getBean("xiapeixinfas");
    }
}
