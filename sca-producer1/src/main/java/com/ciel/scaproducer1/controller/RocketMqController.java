package com.ciel.scaproducer1.controller;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scaproducer1.mq.RocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rocket")
public class RocketMqController {

    @Autowired
    protected RocketService rocketService;

    @GetMapping("/send")
    public Result send(@RequestParam("name")String name) throws AlertException {
        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setName(name);
        scaGirls.setBirthday(Faster.now());

        rocketService.rocketSend(scaGirls,"hello");

        return Result.ok();
    }
}
