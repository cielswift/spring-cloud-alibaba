package com.ciel.scatquick.beanload;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 通过工厂获取bean
 */

@Component("customFactorBean")
public class ACustomFactorBean implements FactoryBean<LocalDateTime> {

    @Value("${ffc.info:'2020-12-20 20:22:20'}") // @Value冒号后面是默认值
    private String info;

    @Override
    public LocalDateTime getObject() throws Exception {
        return LocalDateTime.parse(info, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public Class<LocalDateTime> getObjectType() {
        return LocalDateTime.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    protected AutowireCapableBeanFactory beanFactory;
    /**
     * BEAN (FactoryBean)工厂获取bean
     * @return
     */
    public void get(){
        //获取bean
        Object o1 = beanFactory.getBean("customFactorBean"); // LocalDateTime
        //获取工厂
        Object o2 = beanFactory.getBean("&customFactorBean"); //ACustomFactorBean

    }
}