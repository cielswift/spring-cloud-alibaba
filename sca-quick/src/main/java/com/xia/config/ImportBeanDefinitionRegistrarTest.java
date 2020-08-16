package com.xia.config;

import com.xia.bean.XiapeixinFes;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;


public class ImportBeanDefinitionRegistrarTest implements ImportBeanDefinitionRegistrar {

    /**
     * importingClassMetadata
           AnnotationMetadata类型的，通过这个可以获取被@Import注解标注的类所有注解的信息
     registry
        BeanDefinitionRegistry类型，是一个接口，内部提供了注册bean的各种方法

     importBeanNameGenerator
     BeanNameGenerator类型，是一个接口，内部有一个方法，用来生成bean的名称。

     *
     *
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {

        //需要包装一下才能放进容器
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(XiapeixinFes.class);
        registry.registerBeanDefinition("xiapeixinfes", rootBeanDefinition);


//        从容器中尝试获取Cat和Dog，如果两者都存在，那么就向容器中注入Pig类。有条件，有选择性的向容器中注入Bean
        // 获取容器中已经存在的Bean的名字
//        boolean definition1 = registry.containsBeanDefinition("com.nmys.story.springCore.springioc.importBean.Cat");
//        boolean definition2 = registry.containsBeanDefinition("com.nmys.story.springCore.springioc.importBean.Dog");
//
       // registry.removeBeanDefinition("cat"); //移除bean

        /**
         *      * 注册一个新的bean定义
         *      * beanName：bean的名称
         *      * beanDefinition：bean定义信息
         *
         *void registerBeanDefinition (String beanName, BeanDefinition beanDefinition)
         *             throws BeanDefinitionStoreException;
         *
         *      * 通过bean名称移除已注册的bean
         *      * beanName：bean名称
         *
         *void removeBeanDefinition (String beanName) throws NoSuchBeanDefinitionException;
         *
         *      * 通过名称获取bean的定义信息
         *      * beanName：bean名称
         *
         *BeanDefinition getBeanDefinition (String beanName) throws NoSuchBeanDefinitionException;
         *
         *      * 查看beanName是否注册过
         *
         *boolean containsBeanDefinition (String beanName);
         *
         *      * 获取已经定义（注册）的bean名称列表
         *
         *String[] getBeanDefinitionNames ();
         *
         *
         *      * 返回注册器中已注册的bean数量
         *
         *int getBeanDefinitionCount ();
         *
         *      * 确定给定的bean名称或者别名是否已在此注册表中使用
         *      * beanName：可以是bean名称或者bean的别名
         *
         *boolean isBeanNameInUse (String beanName);
         */
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {


    }
}
