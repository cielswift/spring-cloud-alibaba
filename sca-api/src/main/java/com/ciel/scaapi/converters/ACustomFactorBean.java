package com.ciel.scaapi.converters;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("customFactorBean")
public class ACustomFactorBean implements FactoryBean<LocalDateTime> {

    @Value("${ffc.info:'2020-12-20 20:22:20'}") //冒号后面是默认值
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
}
