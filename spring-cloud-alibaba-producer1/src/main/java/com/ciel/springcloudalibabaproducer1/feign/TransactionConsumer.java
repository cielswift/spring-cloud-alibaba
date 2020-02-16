package com.ciel.springcloudalibabaproducer1.feign;

import com.ciel.springcloudalibabaapi.feign.PublicTransactional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(contextId = "transactionConsumer",name = "producer20")
public interface TransactionConsumer extends PublicTransactional {

    @GetMapping(value = "/producer20/transactional")
    @Override
    boolean transactionPrice(@RequestParam("price") BigDecimal price, @RequestParam("sendUserId")Long sendUserId,
                             @RequestParam("receiveUserId") Long receiveUserId,@RequestParam("code") Integer code);


}
