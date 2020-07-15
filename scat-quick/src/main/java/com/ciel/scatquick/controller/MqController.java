package com.ciel.scatquick.controller;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scatquick.mq.rocket.RocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mq")
public class MqController {

    @Autowired
    protected RocketService rocketService;

    @GetMapping("/send")
    public Result send() throws AlertException {
        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setName("lxw");
        scaGirls.setBirthday(Faster.now());
        rocketService.rocketSend(scaGirls);

        return Result.ok();
    }
}
