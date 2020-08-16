package com.ciel.scatquick.mq.kafka.nativekafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 拦截器
 */
public class MyInterceptor implements ProducerInterceptor<String,String> {

    /**
     * onSend(ProducerRecord)：该方法封装进KafkaProducer.send方法中，
     * 即它运行在用户主线程中的。Producer确保在消息被序列化以计算分区前调用该方法。
     * 用户可以在该方法中对消息做任何操作，但最好保证不要修改消息所属的topic和分区，否则会影响目标分区的计算
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {

        String value = record.value();

        Map<String, Object> map = JSON.parseObject(value, Map.class);
        map.put("拦截器添加header","for toy");

        return new ProducerRecord<String, String>(record.topic(),record.key(),JSON.toJSONString(map));
    }

    /**
     * 该方法会在消息被应答之前或消息发送失败时调用，并且通常都是在producer回调逻辑触发之前。
     * onAcknowledgement运行在producer的IO线程中，因此不要在该方法中放入很重的逻辑，否则会拖慢producer的消息发送效率
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

        if(null == metadata){
            System.out.println("失败-----------------------"+exception.getMessage());
        }else{
            System.out.println("成功-----------------------");
        }

    }

    /**
     * 主要用于执行一些资源清理工作
     */
    @Override
    public void close() {

    }

    /**
     * 获取配置信息和初始化数据
     */
    @Override
    public void configure(Map<String, ?> configs) {
        System.out.println(configs);
    }
}
