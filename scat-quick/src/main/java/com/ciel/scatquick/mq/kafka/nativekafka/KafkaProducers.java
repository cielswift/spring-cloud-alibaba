package com.ciel.scatquick.mq.kafka.nativekafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaProducers {
    public static String TOPIC = "xiapeixin";

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Properties props = new Properties();
        // Kafka服务端的主机名和端口号
        props.put("bootstrap.servers", "127.0.0.1:9920,127.0.0.1:9921,127.0.0.1:9922");
        // 等待所有副本节点的应答
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
        props.put("acks", "1");
        // 消息发送最大尝试次数
        props.put("retries", 0);
        // 一批消息处理大小
        props.put("batch.size", 16384);
        // 请求延时
        props.put("linger.ms", 100);
        // 发送缓存区内存大小
        props.put("buffer.memory", 33554432);

        props.put("partitioner.class","com.ciel.scatquick.mq.kafka.nativekafka.MyPartitioner"); //分区策略

        LinkedList<String> linkedList = new LinkedList<>();

        linkedList.add("com.ciel.scatquick.mq.kafka.nativekafka.MyInterceptor");

        props.put("interceptor.classes",linkedList); //拦截器

        props.put("compression.type", "gzip"); //开启压缩

        props.put("retries", "10"); //重试次数

        // key序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        Callback callback = new Calm();

        for(int i=0; i<100; i ++){

            Map<String, String> map = new HashMap<>();
            map.put("name","xiapeixin");
            map.put("key",String.valueOf(i));

            ProducerRecord<String, String> record =
                    new ProducerRecord<>(TOPIC,String.valueOf(System.currentTimeMillis()),
                            JSON.toJSONString(map));


            Future<RecordMetadata> send = producer.send(record, callback);

            RecordMetadata metadata = send.get();

            System.out.println(metadata);
        }

    }

    public static class Calm implements Callback{

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {

            if(exception != null){

                System.out.println(metadata);
            }else{
                System.out.println("error");
            }
        }
    }

}

