package com.ciel.scaproducer1.config;

import org.dromara.hmily.common.config.HmilyRedisConfig;
import org.dromara.hmily.core.bootstrap.HmilyTransactionBootstrap;
import org.dromara.hmily.core.service.HmilyInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class DatabaseManyConfig {

    /**
     * 多数据源
     * @return
     */

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.ds0")
//    public DataSource ds0(){
//        return null;
//    }

    /**
     * 上下文环境
     */
    @Autowired
    protected ConfigurableApplicationContext context;


    @Bean
    @Primary
    public HmilyTransactionBootstrap hmilyTransactionBootstrap(HmilyInitService hmilyInitService){
        HmilyTransactionBootstrap hmilyTransactionBootstrap = new HmilyTransactionBootstrap(hmilyInitService);

        ConfigurableEnvironment env = context.getEnvironment();

        hmilyTransactionBootstrap.setSerializer(env.getProperty("org.dromara.hmily.serializer"));
        hmilyTransactionBootstrap.setRecoverDelayTime(Integer.parseInt(env.getProperty("org.dromara.hmily.recoverDelayTime")));
        hmilyTransactionBootstrap.setRetryMax(Integer.parseInt(env.getProperty("org.dromara.hmily.retryMax")));
        hmilyTransactionBootstrap.setScheduledDelay(Integer.parseInt(env.getProperty("org.dromara.hmily.scheduledDelay")));
        hmilyTransactionBootstrap.setScheduledThreadMax(Integer.parseInt(env.getProperty("org.dromara.hmily.scheduledThreadMax")));
        hmilyTransactionBootstrap.setRepositorySupport(env.getProperty("org.dromara.hmily.repositorySupport"));
        hmilyTransactionBootstrap.setStarted(Boolean.parseBoolean(env.getProperty("org.dromara.hmily.started")));

//        HmilyDbConfig hmilyDbConfig = new HmilyDbConfig();
//        hmilyDbConfig.setDriverClassName(env.getProperty("org.dromara.hmily.hmilyDbConfig.driverClassName"));
//        hmilyDbConfig.setUrl(env.getProperty("org.dromara.hmily.hmilyDbConfig.url"));
//        hmilyDbConfig.setUsername(env.getProperty("org.dromara.hmily.hmilyDbConfig.username"));
//        hmilyDbConfig.setPassword(env.getProperty("org.dromara.hmily.hmilyDbConfig.password"));
//        hmilyTransactionBootstrap.setHmilyDbConfig(hmilyDbConfig);


        HmilyRedisConfig redisConfig = new HmilyRedisConfig();
        redisConfig.setHostName(env.getProperty("org.dromara.hmily.hmilyRedisConfig.hostName"));
        redisConfig.setPassword(env.getProperty("org.dromara.hmily.hmilyRedisConfig.password"));
        redisConfig.setPort(Integer.parseInt(env.getProperty("org.dromara.hmily.hmilyRedisConfig.port")));

        hmilyTransactionBootstrap.setHmilyRedisConfig(redisConfig);
        return hmilyTransactionBootstrap;
    }
}
