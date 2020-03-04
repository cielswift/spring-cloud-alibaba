package com.ciel.scaproducer1.mq;

import com.alibaba.fastjson.JSON;
import com.ciel.scaapi.exception.AlertException;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 事务消息回调类
 */
@Component
@RocketMQTransactionListener(txProducerGroup = "producer_group1")
public class RocketMQListener implements RocketMQLocalTransactionListener {

    @Autowired
    protected RocketMQCCService rocketMQCCService;

    @Autowired
    protected RedisTemplate redisTemplate;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        /**
         * 消息发送成功给mq 则回调;
         */
        HashMap<String, Object> jb = m2j(msg);

        long accountSend = (long) jb.get("ACCOUNT_SEND");
        BigDecimal price = (BigDecimal) jb.get("PRICE");
        String txNo = (String) jb.get("TXNO");

        boolean tran = false;
        try {
            tran = rocketMQCCService.rocketMqTran(price, txNo);
        } catch (AlertException e) {
            e.printStackTrace();
        }

        if (tran) {
            return RocketMQLocalTransactionState.COMMIT; //可消费
        }
        return RocketMQLocalTransactionState.ROLLBACK; //不可消费

    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        /**
         * 网络中断 事务状态回查
         */
        HashMap<String, Object> jb = m2j(msg);

        long accountSend = (long) jb.get("ACCOUNT_SEND");
        String txNo = (String) jb.get("TXNO");

        if (null != redisTemplate.opsForValue().get(txNo)) { //事务记录
            return RocketMQLocalTransactionState.COMMIT;
        }

        return RocketMQLocalTransactionState.UNKNOWN;

    }

    public HashMap<String, Object> m2j(Message msg) {
        return JSON.parseObject(new String((byte[]) msg.getPayload()), HashMap.class);
    }
}
