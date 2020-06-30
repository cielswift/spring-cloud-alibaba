package com.ciel.scatquick.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

//@Configuration
public class Kafka {

    public static Logger log = LoggerFactory.getLogger(Kafka.class);
    ObjectMapper format = new ObjectMapper(); //序列化

    //一个组里的消费者不能消费同一个分区的数据

    //实际上所有的配置实现都是在org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration中完成
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(cron = "0/30 1 0/1 * * ?")
    public void send() throws JsonProcessingException {

        kafkaTemplate.setProducerListener(producerListener()); //设置回调
        // kafkaTemplate.set

        KafkaMsg message = new KafkaMsg();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        message.setDateTime(LocalDateTime.now());

        kafkaTemplate.executeInTransaction(t -> { //事务内发送
            try {
                if (message.getId() % 2 == 0) {

                    ListenableFuture<SendResult<String, Object>> obj =
                            kafkaTemplate.send("cielswift", 0, null, format.writeValueAsString(message));
                } else {

                    ListenableFuture<SendResult<String, Object>> obj =
                            kafkaTemplate.send("cielswift", 1, null, format.writeValueAsString(message));
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException("AA");
            }

            return true;
        });
        //send方法后面调用get方法即可 ,同步; 可以重载规定时间没有返回报错;
        return;
    }


    @Bean
    @Primary
    public ProducerListener producerListener() { //回调类
        return new ProducerListener() {
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

    @Bean
    @Primary
    public Partitioner partitioner() { //分区器
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

    @Bean
    public ProducerInterceptor producerInterceptor() { //拦截器
        return new ProducerInterceptor() {

            @Override
            public void configure(Map<String, ?> configs) {

            }

            @Override
            public ProducerRecord onSend(ProducerRecord record) {

                return new ProducerRecord(record.topic(), record.partition(), record.key(), record.value() + "AA" + System.currentTimeMillis());
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
