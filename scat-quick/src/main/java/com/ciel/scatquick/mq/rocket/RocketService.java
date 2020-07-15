package com.ciel.scatquick.mq.rocket;

import com.ciel.scaentity.entity.ScaGirls;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
@Slf4j
public class RocketService {

    public static final String topic = "ciel-top-name";
    /**
     * rocket mq 发送器
     */
    @Autowired
    protected RocketMQTemplate rocketMQTemplate;

    protected SendCallback sendCallback;


    @PostConstruct
    public void init(){
        sendCallback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("success:"+sendResult);
            }
            @Override
            public void onException(Throwable throwable) {
                System.err.println("error:"+throwable.getMessage());
            }
        };
    }


    public void rocketSend(ScaGirls scaGirls){
//        rocketMQTemplate.asyncSend(topic,scaGirls,sendCallback,1000); //异步消息
//        rocketMQTemplate.convertAndSend(topic,scaGirls);
//        rocketMQTemplate.asyncSendOrderly(topic,new GenericMessage<ScaGirls>(scaGirls),
//                UUID.randomUUID().toString(),sendCallback);

        //发送事务消息
        TransactionSendResult paramg = rocketMQTemplate
                .sendMessageInTransaction(topic, new GenericMessage<ScaGirls>(scaGirls), "paramg");

        LocalTransactionState transactionState = paramg.getLocalTransactionState();

        log.info("事务执行结果: {}",transactionState.name());
    }



}