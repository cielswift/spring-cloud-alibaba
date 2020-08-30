package com.ciel.scatquick.mq.rocket.nati;

import com.ciel.scatquick.thread.ThreadPoolCompletableFuture;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Producer {

    public static String ADDR = "127.0.0.1:9876";

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {

        //DefaultMQProducer producer = new DefaultMQProducer("producer-group");
        TransactionMQProducer transactionMQProducer = new TransactionMQProducer("tran-pr-gr"); //事务

        TranscationLin transcationLin = new TranscationLin();

        //producer.setNamesrvAddr("120.27.69.29:9876");
       // producer.setInstanceName("producer");
       // producer.start();

        transactionMQProducer.setNamesrvAddr("120.27.69.29:9876");
        transactionMQProducer.setInstanceName("producer");

        transactionMQProducer.setTransactionListener(transcationLin);
        transactionMQProducer.setExecutorService(ThreadPoolCompletableFuture.CPU_THREAD_POOL);

        transactionMQProducer.start();

        Message message = new Message();
        message.setKeys(UUID.randomUUID().toString()); //消息唯一值
        message.setTopic("ciel-top-name");
        message.setTags("xia"); //消息过滤
        message.setBody(String.valueOf(System.currentTimeMillis()).getBytes());

        // SendResult sendResult = producer.send(message);

       // SendResult sendResult = transactionMQProducer.send(message);

        //队列选择器
        MessageQueueSelector selector =  new MessageQueueSelector(){
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                return mqs.get(Integer.parseInt(arg.toString()));
            }
        };

        //第三个参数就是select的第三个参数 ,指定消息发送的分区
        //SendResult send = transactionMQProducer.send(message, selector, 0);

        TransactionSendResult sendResult = transactionMQProducer.sendMessageInTransaction(message, "aa");

        //transactionMQProducer.send(Arrays.asList(message)); //批量发送消息

        System.out.println("事务状态:"+sendResult.getLocalTransactionState());

        System.out.println(sendResult.toString());


       // transactionMQProducer.shutdown();

    }

}