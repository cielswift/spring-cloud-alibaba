package com.xia.config;

import com.xia.bean.XiapeixinFes;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;


public class ImportBeanDefinitionRegistrarTest implements ImportBeanDefinitionRegistrar {

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
    }
}
