package com.ciel.scatquick.mq.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListenerQi {

    //有几个分区就应该有几个消费者 提高并发

    @KafkaListener(topics = "cielswift",groupId = "ciel-group",
            topicPartitions=@TopicPartition(topic = "cielswift", //主题
                                     partitionOffsets = @PartitionOffset(partition = "0",initialOffset = "0") //分区 偏移量
            )) //监听哪一个分区
    public void zeroMessage(ConsumerRecord<String,Object> message){
        
        log.info("==============================================================");
        log.info("0号分区的数据:"+message.offset()+"=="+message.value());
        log.info("==============================================================");

        //ack.acknowledge(); 手动提交
    }

    @KafkaListener(topics = "cielswift", groupId = "ciel-group",
            topicPartitions=@TopicPartition(topic = "cielswift",
                             partitionOffsets = @PartitionOffset(partition = "1",initialOffset = "0")
            )) //监听哪一个分区
    public void oneMessage(ConsumerRecord<String,Object> message){

        log.info("==============================================================");
        log.info("1号分区的数据:"+message.offset()+"=="+message.value());
        log.info("==============================================================");
    }
}