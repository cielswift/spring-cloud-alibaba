package com.ciel.scatquick.mq.kafka.nativekafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class MyPartitioner implements Partitioner { //自定义分区选择器

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
      //  return Math.abs(key.hashCode()) % partitions.size();

        if(System.currentTimeMillis()%2==0){
            return 0;
        }else{
            return 1;
        }
    }
    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
