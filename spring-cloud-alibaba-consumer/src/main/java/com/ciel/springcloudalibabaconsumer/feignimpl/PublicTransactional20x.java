package com.ciel.springcloudalibabaconsumer.feignimpl;

import com.ciel.springcloudalibabaapi.feign.PublicTransactional;
import com.ciel.springcloudalibabaentity.ScaUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(contextId = "producer20x",name = "producer20")
public interface PublicTransactional20x extends PublicTransactional {

    @Override
    @GetMapping(value = "/producer20/transactional")
    boolean transactionPrice(@RequestParam("price")BigDecimal price, @RequestParam("sendUserId")Long sendUserId,
                             @RequestParam("receiveUserId")Long receiveUserId,@RequestParam("code") Integer code);


}
