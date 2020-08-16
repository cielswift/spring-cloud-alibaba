package com.ciel.scatquick.mq.rocket;

import com.ciel.scaentity.entity.ScaGirls;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 监听rocketMQ
 */
@Component
@RocketMQMessageListener(consumerGroup =  "consumer_1", topic = RocketListener.topic)
@Slf4j
public class RocketListener implements RocketMQListener<ScaGirls> {

    public static final String topic = "ciel-top-name";

    @Override
    public void onMessage(ScaGirls message) {

        log.info("收到消息-> {}",message);
    }
    

}