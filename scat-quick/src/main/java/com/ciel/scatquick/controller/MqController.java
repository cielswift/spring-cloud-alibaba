package com.ciel.scatquick.controller;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scatquick.mq.kafka.KafkaServer;
import com.ciel.scatquick.mq.rocket.RocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
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

    @GetMapping("/send")
    public Result send(@RequestParam(value = "name",required = false,defaultValue = "lxw") String name) throws AlertException {
        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setName(name);
        scaGirls.setBirthday(Faster.now());
        rocketService.rocketSend(scaGirls);

        return Result.ok();
    }


    @GetMapping("/se")
    public Result se(@RequestParam(value = "name",required = false,defaultValue = "lxw") String name) throws Exception {

        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setName(name);
        scaGirls.setBirthday(Faster.now());

        kafkaServer.send(scaGirls, SendResult::toString);
        return Result.ok();
    }
}
