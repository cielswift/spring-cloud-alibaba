package com.ciel.springcloudalibabaconsumer.feignimpl;

import com.alibaba.cloud.dubbo.annotation.DubboTransported;
import com.ciel.springcloudalibabaapi.feign.FuckMyLife;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "producer10",fallbackFactory = ProducerFallbackFactory.class)
/**
 * 注解即可将底层的 Rest 协议无缝切换成 Dubbo RPC 协议，进行 RPC 调用。
 */
//@DubboTransported(protocol = "dubbo")
public interface FuckMyLifeXiaPeiXin extends FuckMyLife {

    @GetMapping("/producer10/cc")
    List<String> fml(@RequestParam("name")String name);

}
