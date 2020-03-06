//package com.ciel.scaproducer2.mq;
//
//import com.alibaba.fastjson.JSON;
//import com.ciel.scaapi.exception.AlertException;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//
///**
// * 监听rocketMQ
// */
//@Component
//@RocketMQMessageListener(consumerGroup = "consumer_1", topic = "top_1")
//public class RocketMQReceive implements RocketMQListener<String> {
//
//    @Autowired
//    protected ScaUserTransactionService userTransactionService;
//
//    @Override
//    public void onMessage(String message) {  //分布式事务失败重复执行消息(出现异常循环接收循环接收)
//        HashMap jb = JSON.parseObject(message, HashMap.class);
//
//        String txno = (String) jb.get("TXNO");
//        BigDecimal price = (BigDecimal) jb.get("PRICE");
//        long accountReceive = (long)jb.get("ACCOUNT_RECEIVE");
//
//        try {
//            boolean b = userTransactionService.tranAdd(price, txno);
//        } catch (AlertException e) {
//           throw new RuntimeException("AAA");
//        }
//
//    }
//
//
//}
