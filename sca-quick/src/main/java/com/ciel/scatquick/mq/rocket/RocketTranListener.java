package com.ciel.scatquick.mq.rocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
@RocketMQTransactionListener
@Slf4j
public class RocketTranListener implements RocketMQLocalTransactionListener{

    // 用来执行本地事务
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        MessageHeaders headers = msg.getHeaders();
        log.info("执行本地事务-> {} {}",msg,arg);

        if("lxw".equals(arg)){
            // 模拟本地事务不通过
            return RocketMQLocalTransactionState.ROLLBACK;
        }else {
            return RocketMQLocalTransactionState.COMMIT;
        }

      //  return RocketMQLocalTransactionState.UNKNOWN;
    }

    // 本地事务的检查接口,即MQServer没有收到二次确认消息时调用的方法
    // 去检查本地事务到底有没有成功
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {

        // 模拟回查本地事务
        log.info("本地事务回查-> {}",msg);;

        return RocketMQLocalTransactionState.COMMIT;
    }
}
