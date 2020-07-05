package com.ciel.scaproducer1.mq.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Cluster;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

import java.util.Map;

@Configuration
@Slf4j
public class KafkaAutoConfig {

    @Bean
    @Primary
    public KafkaTemplate<String, Object> kafkaTemplate(@Autowired ProducerFactory<String, Object> producerFactory){
        return new KafkaTemplate<String, Object>(producerFactory);
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
    @Bean
    @Primary
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
