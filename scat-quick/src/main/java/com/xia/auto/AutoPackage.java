package com.xia.auto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * AutoConfigurationPackage注解的作用是将添加该注解的类所在的package 作为 自动配置package 进行管理
 */
@AutoConfigurationPackage
@Configuration
public class AutoPackage {

    @Autowired
    protected ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        List<String> list = AutoConfigurationPackages.get(applicationContext);
        System.out.println("测试AutoConfigurationPackages 功能");
        System.out.println(list);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        String cityPackage = AutoPackage.class.getPackage().getName();

        AutoConfigurationPackages.register(context, cityPackage);

        context.register(MongoAutoConfiguration.class, MongoDataAutoConfiguration.class);

        context.refresh();
    }
}
