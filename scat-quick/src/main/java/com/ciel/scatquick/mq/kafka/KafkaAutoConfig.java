package com.ciel.scatquick.mq.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaAutoConfig {


    /**
     * kafka 事务管理器 和spring事务管理器冲突 不要用
     */
   // @Bean(name = "kafkaTransactionManager")
    public KafkaTransactionManager<String, Object> kafkaTransactionManager(ProducerFactory<String, Object> factory) {
        return new KafkaTransactionManager<String, Object>(factory);
    }


    @Bean("cusProducerFactory")
    @Primary
    public ProducerFactory<String, Object>
    producerFactory(@Autowired DefaultKafkaProducerFactory<String, Object> factory){

        Map<String, Object> properties = factory.getConfigurationProperties();

        properties.put("partitioner.class","org.apache.kafka.clients.producer.Partitioner"); //分区策略
        properties.put("interceptor.classes","org.apache.kafka.clients.producer.ProducerInterceptor"); //拦截器
        properties.put("compression.type", "gzip"); //开启压缩

        return new DefaultKafkaProducerFactory<String, Object>(properties);
    }


    @Bean
    @Primary
    public KafkaTemplate<String, Object> kafkaTemplate(@Qualifier("cusProducerFactory") ProducerFactory<String, Object> factory,
                                                       ProducerListener<String, Object> producerListener){


        KafkaTemplate<String, Object> template = new KafkaTemplate<>(factory);
        template.setProducerListener(producerListener); //设置回调
        return template;
    }

    /**
     * 回调器
     */
    @Bean
    @Primary
    public ProducerListener<String, Object> producerListener() {
        return new ProducerListener<String, Object>() {
            @Override
            public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
                log.info("发送成功 : " + producerRecord.toString());
            }
            @Override
            public void onError(ProducerRecord producerRecord, Exception exception) {
                log.info("发送错误 : " + producerRecord.toString());
            }
        };
    }

    /**
     * 分区器
     */
    public Partitioner partitioner() {
        return new Partitioner() {

            @Override
            public void configure(Map<String, ?> configs) {
            }

            @Override
            public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
                //返回分区
                return 0;
            }

            @Override
            public void close() {
            }
        };
    }

    /**
     * 拦截器
     */
    @Bean
    public ProducerInterceptor<String, Object> producerInterceptor() {
        return new ProducerInterceptor<String, Object>() {
            @Override
            public void configure(Map<String, ?> configs) {
            }

            @Override
            public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> record) {
                return new ProducerRecord<>(record.topic(), record.partition(), record.key(),
                        record.value() + "AA" + System.currentTimeMillis());
            }

            @Override
            public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
            }

            @Override
            public void close() {
            }
        };

    }
}
