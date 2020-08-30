package com.ciel.scatquick.mq.rocket.nati;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

public class ConsumerB {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group-b");

        consumer.setNamesrvAddr("120.27.69.29:9876");
        consumer.setInstanceName("consumer");

        consumer.setConsumeMessageBatchMaxSize(100); //最大拉取条数

        //consumer.setMessageModel(MessageModel.BROADCASTING); //广播消费模式 默认集群

        consumer.subscribe("ciel-top-name", "xia");
        //指定主题和过滤规则 * 全部获取

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("消息id:"+msg.getMsgId() + "---" + new String(msg.getBody()));

                }
                //ConsumeConcurrentlyStatus.RECONSUME_LATER //重试
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
            }
        });

        consumer.setMessageListener(new MessageListenerOrderly() { //有序消费
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

                for (MessageExt msg : msgs) {
                    System.out.println("有序消费:"+msg);
                }


                return ConsumeOrderlyStatus.SUCCESS; //消费成功
            }
        });
        consumer.start();
    }

}