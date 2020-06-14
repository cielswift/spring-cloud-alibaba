package com.ciel.scatquick.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化
 */
@Order(3)
@Configuration
/**
 * ApplicationRunner 和  CommandLineRunner 基本相同
 * 需要bean被加载才会调用
 */
public class AppRunners implements ApplicationRunner,CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        System.out.println("INIT ORDER 3 -> CommandLineRunner");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("INIT ORDER 3 -> ApplicationRunner");
    }

}
