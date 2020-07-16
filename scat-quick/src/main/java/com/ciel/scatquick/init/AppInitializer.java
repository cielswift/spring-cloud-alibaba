package com.ciel.scatquick.init;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 初始化
 * spring上下文初始化的回调函数在上下文（ConfigurableApplicationContext）刷新（refresh）之前调用。
 *
 * 这是整个spring容器在刷新之前初始化ConfigurableApplicationContext的回调接口，
 * 简单来说，就是在容器刷新之前调用此类的initialize方法。这个点允许被用户自己扩展。
 * 用户可以在整个spring容器还没被初始化之前做一些事情
 *
 * springApplication.addInitializers(new AppInitializer()); //需要添加初始化
 */
@Order(2)
public class AppInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        System.out.println("START ORDER 2");

        //获取配置文件信息
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        //动态添加配置
        Map<String, Object> map = new HashMap<>();
        map.put("name-d", "transcation");
        MapPropertySource mapPropertySource = new MapPropertySource("cus-cc", map);
        environment.getPropertySources().addLast(mapPropertySource);


        System.out.println("当前运行端口:"+environment.getProperty("server.port"));
    }
}
