package com.ciel.scatquick.mq.kafka.nativekafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import javax.validation.constraints.NotEmpty;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

public class ConsumerKa {
    public static String TOPIC = "xiapeixin";

    public static void main(String[] args) {

        Properties props = new Properties();
        //设置kafka集群的地址
        props.put("bootstrap.servers", "127.0.0.1:9920,127.0.0.1:9921,127.0.0.1:9922");
        //设置消费者组，组名字自定义，组名字相同的消费者在一个组
        props.put("group.id", "my_group");
        //开启offset自动提交
        props.put("enable.auto.commit", "true");

        //自动提交时间间隔
        props.put("auto.commit.interval.ms", "1000");

        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("com.ciel.scatquick.mq.kafka.nativekafka.MyConsumerInterceptor");
        props.put("interceptor.classes", linkedList); //拦截器

        //    //fetch.min.bytes
//    //fetch.max.wait.ms //100 ms 后返回所有可用的数据
//    //session.timeout.ms //果消费者没有在 session.timeout.ms 指定的时间内发送心跳给群组协调器 死亡


        //消费者从服务器获取记录的最小字节数,足够的数据时才会把它返回给消费者
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 16384);

        /**
         * 默认latest; 如果同一个组设置earliest也不会重新读取;
         */
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); //从哪里开始消费,默认最新的"latest"

        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500); //每次拉取多少条数据

        //序列化器
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //实例化一个消费者  可以多个;
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        KafkaConsumer<String, String> consumer1 = new KafkaConsumer<>(props);

        //消费者订阅主题，可以订阅多个主题
       // consumer.subscribe(Arrays.asList(TOPIC));
       // consumer1.subscribe(Arrays.asList(TOPIC));

        //consumer.subscribe("test.*");

        ConsumerRebalanceListener listener = new ConsumerRebalanceListener(){

            // kafka 在有新消费者加入或者撤出时，会触发rebalance操作，
            //在subscibe订阅主题的时候，我们可以编写回掉函数，在触发rebalance操作之前和触发成功之后，
            //提交相应偏移量和获取拉取偏移量
            //在rebalance之前调用
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.println("新消费者加入");
            }

            //在rebalance之后调用
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.println("新消费者加入");
            }
        };

      //  consumer.subscribe(Arrays.asList(TOPIC),listener);
     //   consumer1.subscribe(Arrays.asList(TOPIC),listener);


        TopicPartition x0 = new TopicPartition(TOPIC, 0);
        TopicPartition x1 = new TopicPartition(TOPIC, 1);

        consumer.assign(Arrays.asList(x0));
        consumer1.assign(Arrays.asList(x1));

        consumer.seek(x0,0);
        consumer1.seek(x1,0);

        //死循环不停的从broker中拿数据

        //1 秒
        Duration duration = Duration.ofSeconds(1);

        new Thread(() -> {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(duration);

                for (ConsumerRecord<String, String> record : records) {

//                consumer.commitAsync(new OffsetCommitCallback() { //提交偏移
//                    @Override
//                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
//
//                    }
//                }); //提交偏移
                    //  consumer.commitSync(); //提交偏移


                    System.out.println("00000获取到数据>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                    System.out.println(record.partition());
                    System.out.println(record.offset());
                    System.out.println(record.key());
                    System.out.println(record.value());

                    System.out.println("00000<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<获取到数据");
                }
            }
        }).start();


        new Thread(() -> {
            while (true) {
                ConsumerRecords<String, String> records = consumer1.poll(duration);

                for (ConsumerRecord<String, String> record : records) {

                    System.out.println("1111获取到数据>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

                    System.out.println(record.partition());
                    System.out.println(record.offset());
                    System.out.println(record.key());
                    System.out.println(record.value());

                    System.out.println("1111<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<获取到数据");
                }
            }
        }).start();
    }
}


