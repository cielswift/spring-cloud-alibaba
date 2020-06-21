package com.ciel.scawebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ciel")
public class ScaWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScaWebfluxApplication.class, args);
    }

}
