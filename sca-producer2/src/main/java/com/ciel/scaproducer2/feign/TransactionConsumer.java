package com.ciel.scaproducer2.feign;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.feign.PublicTransactional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(contextId = "transactionConsumer",name = "producer10",path = "/producer10")
public interface TransactionConsumer extends PublicTransactional {

    @GetMapping(value = "/transactional")
    @Override
    boolean transactionPrice(@RequestParam("price") BigDecimal price, @RequestParam("sendUserId")Long sendUserId,
                             @RequestParam("receiveUserId") Long receiveUserId,@RequestParam("code") Integer code);

    @PutMapping(value = "/hmily/{price}/{sendUserId}/{receiveUserId}/{code}")
    @Override
    boolean hmilyTransaction(@PathVariable("price") BigDecimal price, @PathVariable("sendUserId") Long sendUserId,
                             @PathVariable("receiveUserId")Long receiveUserId, @PathVariable("code") Integer code) throws AlertException;


    @Override
    @PutMapping("/rocket_mq/{price}")
    boolean rocketMqTran(@PathVariable("price") BigDecimal price) throws AlertException;
}
