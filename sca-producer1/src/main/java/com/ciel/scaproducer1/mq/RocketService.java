package com.ciel.scaproducer1.mq;

import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaentity.entity.ScaGirls;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
public class RocketService {

    @Autowired
    protected IScaUserService userService;

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
    public boolean rocketSend(ScaGirls scaGirls, String msg) throws AlertException {

        rocketMQTemplate.asyncSend("ciel-top-name",scaGirls,sendCallback,5000);

       return true;
    }



}
