package com.xia.config;

import com.xia.bean.XiapeixinFbs;
import org.springframework.context.annotation.Bean;

/**
 * 测试 @ImportAutoConfiguration(ImportAutoConfigurationTest.class)
 */
public class ImportAutoConfigurationTest {

    @Bean
    public XiapeixinFbs xiapeixinFbs(){
        return new XiapeixinFbs();
    }
}
