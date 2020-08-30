package com.ciel.scatquick.mq.rocket.nati;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * mp 事务消息
 */
public class TranscationLin implements TransactionListener {

    private Map<String, Integer> map = new ConcurrentHashMap<>();

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {

        System.out.println("事务id:>>"+msg.getTransactionId());

        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {

        //如果 executeLocalTransaction 返回 UNKNOW ,这里会进行消息回查 ,判断消息是否需要回滚或提交
        //消息提交后才会发送给调用者
        System.out.println("消息回查>>>:"+msg);
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
