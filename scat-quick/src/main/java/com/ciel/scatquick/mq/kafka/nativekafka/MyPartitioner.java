package com.ciel.scatquick.mq.kafka.nativekafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * 自定义分区选择器
 */
public class MyPartitioner implements Partitioner { //自定义分区选择器

    private LongAdder longAdder = new LongAdder();

    @Override
    public int partition(String topic, Object key, byte[] keyBytes,
                         Object value, byte[] valueBytes, Cluster cluster) {

        //获取分区
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);

        //key
        System.out.println(new String(keyBytes));
        System.out.println(key);

        //value
        System.out.println(new String(valueBytes));
        System.out.println(value);


        if(longAdder.intValue() == partitions.size()){
            longAdder.reset();
        }

        int i = longAdder.intValue();

        longAdder.increment();

        return i;
    }
    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {
        System.out.println(configs);
    }
}
