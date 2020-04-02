package com.ciel.scatquick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
public class ScatQuickApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScatQuickApplication.class, args);
    }

}
