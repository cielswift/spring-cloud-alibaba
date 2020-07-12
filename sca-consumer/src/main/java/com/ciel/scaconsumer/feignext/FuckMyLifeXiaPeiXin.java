package com.ciel.scaconsumer.feignext;

import com.ciel.scaapi.feign.FuckMyLifeFeign;
import com.ciel.scaentity.entity.ScaGirls;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * contextId : bean 的名称
 * name: 微服务的名称
 * fallbackFactory: 异常处理工厂
 * configuration : 配置类
 * path: 被调用者的context-path 项目根路径
 *
 * 可以用 name = "${spring.application.name}" 从配置文件读取
 */
@FeignClient(contextId = "producer10",name = "producer10",
        fallbackFactory = ProducerFallbackFactory.class,
        configuration = FeignInterceptor.class,
        path = "/producer10")
/**
 * 注解即可将底层的 Rest 协议无缝切换成 Dubbo RPC 协议，进行 RPC 调用
 *
 */
//@DubboTransported(protocol = "dubbo")
public interface FuckMyLifeXiaPeiXin extends FuckMyLifeFeign {

    /**
     *  GET 提交方式
     * feign传参必须要加@RequestParam注解 ,不能传递pojo,只能单个传递
     */
    @Override
    @GetMapping(value = "/get", headers = {"mother=fuck","name=ciel"})
    List<String> format(@RequestParam("name")String name);

    /**
     * get 请求传递对象 使用SpringQueryMap
     */
    @Override
    @GetMapping(value = "/get/map")
    String getQueryMap(@SpringQueryMap ScaGirls scaGirls);

    /**
     *Post方式可以传递对象需要使用@RequestBody
     */
    @Override
    @PostMapping(value = "/post")
    String posts(@RequestBody ScaGirls scaGirls,@RequestParam("id") Long id);

    /**
     * put 方式
     */
    @Override
    @PutMapping(value = "/put")
    String puts(@RequestBody ScaGirls scaGirls,@RequestParam("id") Long id);

    /**
     * del 方式
     */
    @Override
    @DeleteMapping(value = "/del/{id}/{name}")
    String delete(@PathVariable("id")Long id, @PathVariable("name")String name);
}
