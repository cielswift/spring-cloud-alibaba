package com.ciel.scatquick.mq.kafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfig {


    @Autowired
    private KafkaProperties properties;


    @Bean("myKafkaContainerFactory")
    @ConditionalOnBean(ConcurrentKafkaListenerContainerFactoryConfigurer.class)
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer) {

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        configurer.configure(factory, consumerFactory());
        return factory;
    }



    //获得创建消费者工厂
    public ConsumerFactory<Object, Object> consumerFactory() {

        KafkaProperties myKafkaProperties =
                JSON.parseObject(JSON.toJSONString(this.properties), KafkaProperties.class);
        //对模板 properties 进行定制化
        //....
        //例如：定制servers
        //myKafkaProperties.setBootstrapServers(myServers);
        return new DefaultKafkaConsumerFactory<>(myKafkaProperties.buildConsumerProperties());
    }

    @Bean
    public ConsumerFactory<String,String> fileConsumerFactory(){

        Map<String,Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,  "");
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, "");
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "");
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "");
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "");

        return new DefaultKafkaConsumerFactory<>(propsMap);
    }
}