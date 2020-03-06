package com.ciel.scagateway.filter.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ignoreUnknownFields：忽略未知的字段。默认true;
 * ignoreInvalidFields：是否忽略验证失败的字段。比如我们在配置文件中配置了一个字符串类型的变量，
 * 类中的字段是int类型，那肯定会报错的。如果出现这种情况我们可以容忍，则需要配置该属性值为true。该参数值默认为false
 *
 */

@ConfigurationProperties(prefix = "newconfig")
@Data
@Slf4j
/**
 * 通过构造函数注入而不是set方法
 */
@ConstructorBinding
/**
 * @ConfigurationPropertiesScan("com.ciel.scagateway.filter.config")
 * //自动扫描配置类,不再需要@Configuration或者@Component
 */
public class NewConfig {

    public NewConfig(String rad, Integer ca) {
        this.rad = rad;
        this.ca = ca;
    }

    private String rad;

    private Integer ca;

    @Bean
    @ConfigurationProperties(prefix = "whitegirl") //另一个配置对象
    public GirlWhite girlWhite(){
        return new GirlWhite();
    }

    @PostConstruct
    public void init(){

        System.out.println("===================================================");

        System.out.println(rad);
        System.out.println(ca);

        System.out.println("===================================================");
    }
}
