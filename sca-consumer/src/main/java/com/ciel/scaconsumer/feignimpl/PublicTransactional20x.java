package com.ciel.scaconsumer.feignimpl;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.feign.PublicTransactional;
import com.ciel.scaapi.retu.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(contextId = "producer20x",name = "producer20",
        configuration = FeignInterceptor.class,
        path = "/producer20")
public interface PublicTransactional20x extends PublicTransactional {

    @Override
    @GetMapping(value = "/transactional")
    boolean transactionPrice(@RequestParam("price")BigDecimal price, @RequestParam("sendUserId")Long sendUserId,
                             @RequestParam("receiveUserId")Long receiveUserId,@RequestParam("code") Integer code);

    @Override
    @PutMapping(value = "/hmily/{price}/{sendUserId}/{receiveUserId}/{code}")
    boolean hmilyTransaction(@PathVariable("price")  BigDecimal price, @PathVariable("sendUserId")  Long sendUserId,
                             @PathVariable("receiveUserId")  Long receiveUserId, @PathVariable("code")  Integer code) throws AlertException;

    @Override
    @PutMapping("/rocket_mq/{price}")
    boolean rocketMqTran(@PathVariable("price") BigDecimal price) throws AlertException;

    @PutMapping("/sec/{y}")
    Result sec(@PathVariable("y") String y);

}
