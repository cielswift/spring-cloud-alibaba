package com.ciel.scaproducer2.mq.rocket;

import com.ciel.scaentity.entity.ScaGirls;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 监听rocketMQ
 */
@Component
@RocketMQMessageListener(consumerGroup = "consumer_1", topic = "ciel-top-name")
public class RocketListener implements RocketMQListener<ScaGirls> {

    @Override
    public void onMessage(ScaGirls message) {

        System.out.println("收到消息:"+message);
    }
}
