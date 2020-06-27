package com.xia.config;

import com.xia.bean.XiapeixinFfs;
import org.springframework.context.annotation.Bean;

public class ConfigurationTest {

    @Bean
    public XiapeixinFfs xiapeixinFfs(){
        return new XiapeixinFfs();
    }
}
