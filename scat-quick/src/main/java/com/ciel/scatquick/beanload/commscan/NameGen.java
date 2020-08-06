package com.ciel.scatquick.beanload.commscan;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

/**
 * 配合@ComponentScan 生成bean name
 */
public class NameGen implements BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {

        return definition.getBeanClassName();
    }
}
