package com.ciel.scaconsumer.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ciel.scaapi.crud.ApplicationServer;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaconsumer.feignimpl.FuckMyLifeXiaPeiXin;
import com.ciel.scaconsumer.feignimpl.PublicTransactional10x;
import com.ciel.scaconsumer.feignimpl.PublicTransactional20x;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@RestController
public class RestRpcController {
    /**
     * 注入service,基于dubbo协议;
     */
    @Reference
    protected ApplicationServer applicationServer;

    @GetMapping("/")
    public Result index() {

        return Result.ok("olll").body("WELCOME-欢迎");
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
    public Result d1(String name) {

        /**
         * dubbo调用
         */
        String select = applicationServer.select(name);

        /**
         * restTemplate 调用
         */
        String object = null;
        try{
             object = restTemplate.getForObject("https://cielswift.github.io/", String.class);
        }catch (Exception e){
            return Result.error("restTemplate 调用失败;");
        }

        /**
         * feign调用
         */
        List<String> xiapeixin = fuckMyLifeXiaPeiXin.fml("xiapeixin");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("1",select);
        hashMap.put("2",object);
        hashMap.put("3",xiapeixin);

        return Result.ok("ok").body(hashMap);
    }

    /**
     * 熔断降级,参数类型需要和原方法匹配,最后加上BlockException;
     * 如果使用其他类里的函数,使用blockHandlerClass 指定类, 但是方法必须是 static;
     */
    public Result d2(String name, BlockException be) {
        String msg = null;
        if(null != be && !StringUtils.isEmpty(be.getRuleLimitApp())){
            msg = be.getRuleLimitApp();
        }
        return Result.error("熔断降级").body(msg);
    }

    /**
     * 异常降级,抛出异常时调用,默认所有异常; 也有 fallbackClass
     * 可以使用 exceptionsToIgnore指定要忽略的异常;
     * <p>
     * exceptionsToTrace 来指定要跟踪的异常(默认所有异常);
     */
    public Result d3(String name, Throwable te) {
        return Result.error("异常降级").body(te.getClass().getName());
    }


    /*
    #####################################################################################################
     */

    @Autowired
    protected PublicTransactional10x transactional10x;

    @Autowired
    protected PublicTransactional20x transactional20x;


    @GetMapping("/transactional")
    public Result transactional(@RequestParam("money") BigDecimal money){

        boolean isOk =
                transactional10x.transactionPrice(money, 425752943532056576L,425752880537804800L,1);

        return Result.ok("code").body(isOk);
    }


    @GetMapping("/hmily")
    public Result hmily(@RequestParam("money") BigDecimal money) throws AlertException {

        boolean isOk =
                false;
        try {
            isOk = transactional10x.hmilyTransaction(money, 425752943532056576L,425752880537804800L,1);
        } catch (AlertException e) {

           throw new AlertException("调用异常");
        }

        return Result.ok("code").body(isOk);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/rocket/{money}")
    public Result rocket(@PathVariable("money") BigDecimal money) throws AlertException {

        boolean mqTran = transactional10x.rocketMqTran(money);

        return Result.ok("ookk");
    }

}
