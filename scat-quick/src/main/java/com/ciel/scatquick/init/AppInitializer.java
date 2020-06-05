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
 */
@Order(Integer.MIN_VALUE)
public class AppInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> map = new HashMap<>();
        map.put("demo", 1);
        MapPropertySource mapPropertySource = new MapPropertySource("demoInitializer", map);
        environment.getPropertySources().addLast(mapPropertySource);
        System.out.println("应用程序初始化-------------------------------");
    }
}
