package com.ciel.scatquick.mq.rocket.nati;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class Producer {

    public static void main(String[] args) throws MQClientException {

        DefaultMQProducer producer = new DefaultMQProducer("producer-group");

        producer.setNamesrvAddr("120.27.69.29:9876");
        producer.setInstanceName("producer");
        producer.start();

        try {
            for (int i = 0; i < 10; i++) {
                // Thread.sleep(1000); // 每秒发送一次MQ
                Message message = new Message();
                message.setTopic("ciel-top-name");
                message.setBody("fuck you mother".getBytes());

                SendResult sendResult = producer.send(message);
                System.out.println(sendResult.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.shutdown();

    }

}