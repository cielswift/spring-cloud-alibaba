package com.ciel.springcloudalibabaconsumer.feignimpl;

import com.ciel.springcloudalibabaapi.feign.PublicTransactional;
import com.ciel.springcloudalibabaentity.ScaUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(contextId = "producer10x",name = "producer10")
public interface PublicTransactional10x extends PublicTransactional {

    @Override
    @GetMapping(value = "/producer10/transactional")
    boolean transactionPrice(@RequestParam("price")BigDecimal price, @RequestParam("sendUserId")Long sendUserId,
                             @RequestParam("receiveUserId")Long receiveUserId,@RequestParam("code") Integer code);


}
