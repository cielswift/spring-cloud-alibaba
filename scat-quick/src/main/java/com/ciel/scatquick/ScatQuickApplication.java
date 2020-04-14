package com.ciel.scatquick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
@ComponentScan(basePackages = { "com.ciel" })
@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE)
public class ScatQuickApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScatQuickApplication.class, args);
    }

}
