package com.ciel.scatquick.mq.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class KafkaConfig {

    /**
     * kafka 手动配置
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
    kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setPollTimeout(1500);
        //配置手动提交offset
        factory.getContainerProperties().setAckMode((ContainerProperties.AckMode.MANUAL));

        //    RECORD
//            每处理一条commit一次
//    BATCH(默认)
//每次poll的时候批量提交一次，频率取决于每次poll的调用频率
//        TIME
//        每次间隔ackTime的时间去commit
//        COUNT
//        累积达到ackCount次的ack去commit
//        COUNT_TIME
//        ackTime或ackCount哪个条件先满足，就commit
//        MANUAL
//        listener负责ack，但是背后也是批量上去
//        MANUAL_IMMEDIATE
//        listner负责ack，每调用一次，就立即commit

        return factory;
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