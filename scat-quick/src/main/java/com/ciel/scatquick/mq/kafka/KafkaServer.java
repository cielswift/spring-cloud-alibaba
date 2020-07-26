package com.ciel.scatquick.mq.kafka;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.ciel.scaentity.entity.ScaGirls;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Service
@Slf4j
public class KafkaServer {

    
    public static final String TOPIC = "cielswift";

    //一个组里的消费者不能消费同一个分区的数据, 因为有offer

    //实际上所有的配置实现都是在org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration中完成

    /**
     * kafka 发送器
     */
    @Autowired
    protected KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * 事务消息 和spring事务管理器冲突 不要用
     */
    //@Transactional(transactionManager = "kafkaTransactionManager")
//    public void tranSend(ScaGirls scaGirls,
//                         Function<SendResult<String, Object>, String> success,Function<Exception, String> defeat) {
//
//        Long key = System.currentTimeMillis() + 7;
//
//        kafkaTemplate.executeInTransaction(ope -> {
//
//            ListenableFuture<SendResult<String, Object>> result =
//                    ope.send(TOPIC, key.intValue() % 2, String.valueOf(key), JSON.toJSONString(scaGirls));
//
//            try {
//                SendResult<String, Object> sendResult = result.get(); //阻塞
//
//                RecordMetadata metadata = sendResult.getRecordMetadata(); //元数据信息
//
//                System.out.println(sendResult);
//                return success.apply(sendResult);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return defeat.apply(e);
//            }
//        });
//    }


    public void send(ScaGirls scaGirls,Function<SendResult<String, Object>,String> success) throws Exception {
        Long key = System.currentTimeMillis() + 7;

        ListenableFuture<SendResult<String, Object>> send =
                kafkaTemplate.send("cielswift", key.intValue() % 2, String.valueOf(key), JSON.toJSONString(scaGirls));
        //send方法后面调用get方法即可 ,同步; 可以重载规定时间没有返回报错;
        //    SendResult<String, Object> stringObjectSendResult = obj.get();


        send.addCallback((suc) -> {
            log.info("消息投递成功: {}",suc);
        }, (def) -> {
            log.info("消息投递失败: {}",def.getMessage());
        });

        String apply = success.apply(send.get());
        System.out.println(apply);
    }


}
