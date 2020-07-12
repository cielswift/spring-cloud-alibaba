package com.ciel.scaconsumer.feignext;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 调用url
 * 直接调用url地址
 */
@FeignClient(contextId = "aliCloud",url = "http://120.27.69.29:3000")
public interface FeignUrl {


    @GetMapping("/ali")
    public String ali();
}
