package com.ciel.springcloudalibabaproducer1.feign;

import com.ciel.springcloudalibabaapi.exception.AlertException;
import com.ciel.springcloudalibabaapi.feign.PublicTransactional;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(contextId = "transactionConsumer",name = "producer20")
public interface TransactionConsumer extends PublicTransactional {

    @GetMapping(value = "/producer20/transactional")
    @Override
    boolean transactionPrice(@RequestParam("price") BigDecimal price, @RequestParam("sendUserId")Long sendUserId,
                             @RequestParam("receiveUserId") Long receiveUserId,@RequestParam("code") Integer code);


    @PutMapping(value = "/producer20/hmily/{price}/{sendUserId}/{receiveUserId}/{code}")
    @Override
    @Hmily //把事务带到下游微服务里
    boolean hmilyTransaction(@PathVariable("price") BigDecimal price, @PathVariable("sendUserId") Long sendUserId,
                             @PathVariable("receiveUserId")Long receiveUserId, @PathVariable("code") Integer code) throws AlertException;

}
