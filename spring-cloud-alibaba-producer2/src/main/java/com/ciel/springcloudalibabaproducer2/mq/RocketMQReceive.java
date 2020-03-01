package com.ciel.springcloudalibabaproducer2.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 监听rocketMQ
 */
@Component
@RocketMQMessageListener(consumerGroup = "consumer_1", topic = "top_1")
public class RocketMQReceive implements RocketMQListener<String> {

    @Autowired
    protected ScaUserTransactionService userTransactionService;

    @Override
    public void onMessage(String message) {

        HashMap jb = JSON.parseObject(message, HashMap.class);

        String txno = (String) jb.get("TXNO");
        BigDecimal price = (BigDecimal) jb.get("PRICE");
        long accountReceive = (long)jb.get("ACCOUNT_RECEIVE");

        boolean b = userTransactionService.tranAdd(price, txno);

    }


}
