package com.ciel.scaconsumer.feignimpl;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.feign.PublicTransactional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(contextId = "producer20x",name = "producer20")
public interface PublicTransactional20x extends PublicTransactional {

    @Override
    @GetMapping(value = "/producer20/transactional")
    boolean transactionPrice(@RequestParam("price")BigDecimal price, @RequestParam("sendUserId")Long sendUserId,
                             @RequestParam("receiveUserId")Long receiveUserId,@RequestParam("code") Integer code);

    @Override
    @PutMapping(value = "/producer20/hmily/{price}/{sendUserId}/{receiveUserId}/{code}")
    boolean hmilyTransaction(@PathVariable("price")  BigDecimal price, @PathVariable("sendUserId")  Long sendUserId,
                             @PathVariable("receiveUserId")  Long receiveUserId, @PathVariable("code")  Integer code) throws AlertException;

    @Override
    @PutMapping("/producer20/rocket_mq/{price}")
    boolean rocketMqTran(@PathVariable("price") BigDecimal price) throws AlertException;
}
