package com.ciel.scaproducer1.mq.rocket;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaentity.entity.ScaGirls;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class RocketService {

    /**
     * rocket mq 发送器
     */
    @Autowired
    protected RocketMQTemplate rocketMQTemplate;

    protected  SendCallback sendCallback;

    @PostConstruct
    public void init(){
        sendCallback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("success:"+sendResult);
            }
            @Override
            public void onException(Throwable throwable) {
                System.err.println("error:"+throwable.getMessage());
            }
        };
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean rocketSend(ScaGirls scaGirls) throws AlertException {

        rocketMQTemplate.asyncSend("ciel-top-name",scaGirls,sendCallback,5000);

       return true;
    }



}
