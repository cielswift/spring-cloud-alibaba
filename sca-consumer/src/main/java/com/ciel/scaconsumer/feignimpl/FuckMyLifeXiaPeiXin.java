package com.ciel.scaconsumer.feignimpl;

import com.ciel.scaapi.feign.FuckMyLife;
import org.apache.dubbo.rpc.service.EchoService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "producer10",name = "producer10",
        fallbackFactory = ProducerFallbackFactory.class,
        configuration = FeignInterceptor.class,
        path = "/producer10")   //被调用者的context-path
/**
 * 注解即可将底层的 Rest 协议无缝切换成 Dubbo RPC 协议，进行 RPC 调用。
 */
//@DubboTransported(protocol = "dubbo")
public interface FuckMyLifeXiaPeiXin extends FuckMyLife {

    @GetMapping(value = "/cc",
            headers = {"youmother=dead", "username=wangyong@xxx.com"})
    List<String> fml(@RequestParam("name")String name);

}
