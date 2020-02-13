package com.ciel.springcloudalibabaconsumer.feignimpl;

import com.ciel.springcloudalibabaapi.feign.FuckMyLife;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "producer10",fallbackFactory = ProducerFallbackFactory.class )
public interface FuckMyLifeXiaPeiXin extends FuckMyLife {

    @GetMapping("/producer10/cc")
    List<String> fml(@RequestParam("name")String name);

}
