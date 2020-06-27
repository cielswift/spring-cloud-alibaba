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
 * springApplication.addInitializers(new AppInitializer()); //需要添加初始化
 */
@Order(2)
public class AppInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        System.out.println("INIT ORDER 2");

        //获取配置文件信息
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> map = new HashMap<>();
        map.put("demo", 1);

        MapPropertySource mapPropertySource = new MapPropertySource("demoInitializer", map);
        environment.getPropertySources().addLast(mapPropertySource);

        String property = environment.getProperty("server.port");

        System.out.println("当前运行端口:"+property);
    }
}
