package com.ciel.scaproducer1.controller;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scaproducer1.mq.kafka.KafkaServer;
import com.ciel.scaproducer1.mq.rocket.RocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mq")
public class MqController {

    @Autowired
    protected RocketService rocketService;

    @Autowired
    protected KafkaServer kafkaServer;

    @GetMapping("/rocket")
    public Result rocket(@RequestParam("name")String name) throws AlertException {
        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setName(name);
        scaGirls.setBirthday(Faster.now());
        rocketService.rocketSend(scaGirls);
        return Result.ok();
    }

    @GetMapping("/kafka")
    public Result kafka(@RequestParam("name")String name) throws AlertException {
        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setName(name);
        scaGirls.setBirthday(Faster.now());
        kafkaServer.send(scaGirls);
        return Result.ok();
    }


}
