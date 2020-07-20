package com.ciel.scatquick.mq.kafka.nativekafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;

public class ConsumerKa {

    public static void main(String[] args) {

        Properties props = new Properties();
        //设置kafka集群的地址
        props.put("bootstrap.servers", "bd.cloud:9920");
        //设置消费者组，组名字自定义，组名字相同的消费者在一个组
        props.put("group.id", "my_group");
        //开启offset自动提交
        props.put("enable.auto.commit", "true");

        //自动提交时间间隔
        props.put("auto.commit.interval.ms", "1000");

        //max-poll-records
        /**
         * 默认latest; 如果同一个组设置earliest也不会重新读取;
         */
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //从哪里开始消费,默认最新的"latest"

        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,500); //每次拉取多少条数据

        LinkedList<String> linkedList = new LinkedList();;
        linkedList.add("com.ciel.provider.kafka.nativekafka.MyConsumerInterceptor");
        props.put("interceptor.classes",linkedList); //拦截器


        //序列化器
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //实例化一个消费者  可以多个;
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        KafkaConsumer<String, String> consumer1 = new KafkaConsumer<>(props);

        //消费者订阅主题，可以订阅多个主题
        consumer.subscribe(Arrays.asList("xiapeixin"));
        consumer1.subscribe(Arrays.asList("xiapeixin"));

//        consumer.subscribe(Arrays.asList("xiapeixin"), new ConsumerRebalanceListener() {
//
//            // kafka 在有新消费者加入或者撤出时，会触发rebalance操作，
//            //在subscibe订阅主题的时候，我们可以编写回掉函数，在触发rebalance操作之前和触发成功之后，提交相应偏移量和获取拉取偏移量
//            @Override
//            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
//                //在rebalance之前调用
//
//            }
//            @Override
//            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
//            //在rebalance之后调用
//
//            }
//        });

        //死循环不停的从broker中拿数据


        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(1000);

                    for (ConsumerRecord<String, String> record : records) {

//                consumer.commitAsync(new OffsetCommitCallback() { //提交偏移
//                    @Override
//                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
//
//                    }
//                }); //提交偏移
                        //  consumer.commitSync(); //提交偏移

                        System.out.printf("0号消费者==========parrtin = %d , offset = %d, key = %s, value = %s%n", record.partition(), record.offset(), record.key(), record.value());

                    }

                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    ConsumerRecords<String, String> records = consumer1.poll(1000);

                    for (ConsumerRecord<String, String> record : records) {

                        System.out.printf("1号消费者==========parrtin = %d , offset = %d, key = %s, value = %s%n",
                                record.partition(), record.offset(), record.key(), record.value());
                    }

                }

            }
        }).start();



    }
}
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    Properties properties = new Properties();
//        properties.put("bootstrap.server","192.168.1.9:9092");
//        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
//        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
//    KafkaConsumer<String,String> consumer = new KafkaConsumer<>(properties);
//
//        consumer.subscribe(Collections.singletonList("customerTopic"));
//
//    // consumer.subscribe("test.*");
//
//    //fetch.min.bytes  //消费者从服务器获取记录的最小字节数,足够的数据时才会把它返回给消费者
//    //fetch.max.wait.ms //100 ms 后返回所有可用的数据
//    //session.timeout.ms //果消费者没有在 session.timeout.ms 指定的时间内发送心跳给群组协调器 死亡
//    //auto.offset.reset  //latest  //earliest
//    //enable.auto.commit 提交偏移
//

//    acks 参数指定了要有多少个分区副本接收消息，生产者才认为消息是写入成功的。此参数对消息丢失的影响较大
//
//        如果 acks = 0，就表示生产者也不知道自己产生的消息是否被服务器接收了，它才知道它写成功了。如果发送的途中产生了错误，生产者也不知道，
//          它也比较懵逼，因为没有返回任何消息。这就类似于 UDP 的运输层协议，只管发，服务器接受不接受它也不关心。

//        如果 acks = 1，只要集群的 Leader 接收到消息，就会给生产者返回一条消息，告诉它写入成功。如果发送途中造成了网络异常或者
//          Leader 还没选举出来等其他情况导致消息写入失败，生产者会受到错误消息，这时候生产者往往会再次重发数据。
//          因为消息的发送也分为 同步 和 异步，Kafka 为了保证消息的高效传输会决定是同步发送还是异步发送。
//         如果让客户端等待服务器的响应（通过调用 Future 中的 get() 方法），显然会增加延迟，如果客户端使用回调，就会解决这个问题。

//        如果 acks = all，这种情况下是只有当所有参与复制的节点都收到消息时，生产者才会接收到一个来自服务器的消息。
//       不过，它的延迟比 acks =1 时更高，因为我们要等待不只一个服务器节点接收消息。
