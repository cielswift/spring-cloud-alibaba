package com.ciel.springcloudalibabaconsumer.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ciel.springcloudalibabaapi.crud.ApplicationServer;
import com.ciel.springcloudalibabaapi.feign.FuckMyLife;
import com.ciel.springcloudalibabaconsumer.feignimpl.FuckMyLifeXiaPeiXin;
import com.ciel.springcloudalibabaconsumer.feignimpl.PublicTransactional10x;
import com.ciel.springcloudalibabaconsumer.feignimpl.PublicTransactional20x;
import com.ciel.springcloudalibabaentity.ScaUser;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class RestRpcController {
    /**
     * 注入service,基于dubbo协议;
     */
    @Reference
    protected ApplicationServer applicationServer;

    @GetMapping("/")
    public Object index() {
        return List.of(Map.of("CODE", "200"),
                Map.of("MSG", "WELCOME-欢迎"));
    }

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected FuckMyLifeXiaPeiXin fuckMyLifeXiaPeiXin;

    /**
     * Sentinel降级
     * value 自定义资源名
     */
    @SentinelResource(value = "d1", blockHandler = "d2", fallback = "d3")
    @GetMapping("/d1")
    public Object d1(String name) {

        /**
         * dubbo调用
         */
        String select = applicationServer.select(name);

        /**
         * restTemplate 调用
         */
        String object = restTemplate.getForObject("http://127.0.0.1/", String.class);

        if("nginx".equals(object)){
            return "This nginx";
        }

        /**
         * feign调用
         */
        List<String> xiapeixin = fuckMyLifeXiaPeiXin.fml("xiapeixin");

        return List.of(Map.of("CODE", "200"),
                Map.of("MSG", select), Map.of("body", object),
                Map.of("oth", xiapeixin.toString()));
    }

    /**
     * 熔断降级,参数类型需要和原方法匹配,最后加上BlockException;
     * 如果使用其他类里的函数,使用blockHandlerClass 指定类, 但是方法必须是 static;
     */
    public Object d2(String name, BlockException be) {
        return "熔断降级??".contains(be.getRuleLimitApp().concat(be.getMessage()));
    }

    /**
     * 异常降级,抛出异常时调用,默认所有异常; 也有 fallbackClass
     * 可以使用 exceptionsToIgnore指定要忽略的异常;
     * <p>
     * exceptionsToTrace 来指定要跟踪的异常(默认所有异常);
     */
    public Object d3(String name, Throwable te) {
        return "异常降级??".contains(te.getClass().getName());
    }


    /*
    #####################################################################################################
     */

    @Autowired
    protected PublicTransactional10x transactional10x;

    @Autowired
    protected PublicTransactional20x transactional20x;



    @GetMapping("/transactional")
    public Object transactional(@RequestParam("money") BigDecimal money){

        boolean isOk =
                transactional10x.transactionPrice(money, 425752943532056576L,425752880537804800L,1);

        return Map.of("code",isOk);
    }



}
