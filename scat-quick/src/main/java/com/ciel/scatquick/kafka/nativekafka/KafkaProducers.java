package com.ciel.scatquick.kafka.nativekafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.LinkedList;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Future;

//@Component
public class KafkaProducers {

    public static Integer val = 0;

    public static void main(String[] args) throws InterruptedException {

        Properties props = new Properties();
        // Kafka服务端的主机名和端口号
        props.put("bootstrap.servers", "bd.cloud:9920");
        // 等待所有副本节点的应答
        props.put("acks", "all");
        // 消息发送最大尝试次数
        props.put("retries", 0);
        // 一批消息处理大小
        props.put("batch.size", 16384);
        // 请求延时
        props.put("linger.ms", 100);
        // 发送缓存区内存大小
        props.put("buffer.memory", 33554432);

        props.put("partitioner.class","com.ciel.provider.kafka.nativekafka.MyPartitioner"); //分区策略

        LinkedList<String> linkedList = new LinkedList();;

        linkedList.add("com.ciel.provider.kafka.nativekafka.MyInterceptor");

        props.put("interceptor.classes",linkedList); //拦截器

        props.put("compression.type", "gzip"); //开启压缩

        props.put("retries", "10"); //重试次数

        // key序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        org.apache.kafka.clients.producer.KafkaProducer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);

        while (true){
            Future<RecordMetadata> future =
                    producer.send(new ProducerRecord<String, String>("xiapeixin", UUID.randomUUID().toString(), String.valueOf(val++) )
                                  ,(metadata,exception) ->{
                                System.out.println(metadata.toString()); //异步发送消息回调
                            }
                    );


            Thread.sleep(1000);
        }


//        RecordMetadata recordMetadata = //同步发送消息
//                producer.send(new ProducerRecord<String, String>("xiapeixin", UUID.randomUUID().toString(), UUID.randomUUID().toString())).get();


//        ProducerRecord<String, String> producerRecord =
//                new ProducerRecord<String, String>("CustomerCountry", "Huston", "America");
//
//
//
//        producer.close();


    }


}

