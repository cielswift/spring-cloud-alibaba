package com.ciel.scaproducer1.mq.kafka;

import com.alibaba.fastjson.JSON;
import com.ciel.scaentity.entity.ScaGirls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaServer {


    //一个组里的消费者不能消费同一个分区的数据
    //实际上所有的配置实现都是在org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration中完成

    /**
     * kafka 发送器
     */
    @Autowired
    protected KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 回调
     */
    @Autowired
    protected ProducerListener<String, Object> producerListener;

    @PostConstruct
    public void init() {
          kafkaTemplate.setProducerListener(producerListener); //设置回调
    }

    public void send(ScaGirls scaGirls) {

        long key = System.currentTimeMillis() + 7;

        ListenableFuture<SendResult<String, Object>> obj = null;

        if (key % 2 == 0) {
            obj = kafkaTemplate.send("cielswift", 0, String.valueOf(key), JSON.toJSONString(scaGirls));
        } else {
            obj = kafkaTemplate.send("cielswift", 1, String.valueOf(key), JSON.toJSONString(scaGirls));
        }

        //send方法后面调用get方法即可 ,同步; 可以重载规定时间没有返回报错;
        //    SendResult<String, Object> stringObjectSendResult = obj.get();

        obj.addCallback((succ) -> {
            System.out.println("success");
        }, (def) -> {
            System.out.println("defeat");
        });

        //  kafkaTemplate.executeInTransaction(t -> { //事务内发送

    }

    ;


}
