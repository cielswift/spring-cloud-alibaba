package com.ciel.scaconsumer.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ciel.scaapi.dubbo.ApplicationServer;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaconsumer.feignext.FuckMyLifeXiaPeiXin;
import com.ciel.scaconsumer.feignext.PublicTransactional10x;
import com.ciel.scaconsumer.feignext.PublicTransactional20x;
import com.ciel.scaentity.entity.ScaApplication;
import com.ciel.scaentity.entity.ScaGirls;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 刷新配置文件
 */
@RefreshScope

@RestController
public class RestRpcController {
    /**
     * 注入service,基于dubbo协议;
     */
    @Reference
    protected ApplicationServer applicationServer;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected FuckMyLifeXiaPeiXin fuckMyLifeXiaPeiXin;
    /**
     * feign协议
     */
    @Autowired
    protected PublicTransactional20x publicTransactional20x;

    @Autowired
    protected ApplicationContext applicationContext;


    @GetMapping("/rpcs")
    public Result rpcs(){

        applicationContext.getEnvironment().getProperty("person.xiapeixin"); //获取配置

        List<String> xiapeixin = fuckMyLifeXiaPeiXin.format("xiapeixin");

        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setName("liuxuewen");
        scaGirls.setBirthday(Faster.now());
        scaGirls.setPrice(new BigDecimal("25.5"));

        String posts = fuckMyLifeXiaPeiXin.posts(scaGirls, 2L);

        String puts = fuckMyLifeXiaPeiXin.puts(scaGirls, 3L);

        String xiazhi = fuckMyLifeXiaPeiXin.delete(1L, "xiazhi");

        Map<String, Object> map = new HashMap<>();

        map.put("get",xiapeixin);
        map.put("post",posts);
        map.put("put",puts);
        map.put("del",xiazhi);


        ScaApplication scaApplication = new ScaApplication();
        scaApplication.setName("app");
        scaApplication.setCreateDate(Faster.now());


        ScaApplication select = applicationServer.select(scaApplication);

        map.put("dubbo",select);

        return Result.ok().data(map);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////

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
        ScaApplication scaApplication = new ScaApplication();
        scaApplication.setName("app");
        scaApplication.setCreateDate(Faster.now());
        ScaApplication select = applicationServer.select(scaApplication);

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
        List<String> xiapeixin = fuckMyLifeXiaPeiXin.format("xiapeixin");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("1",select);
        hashMap.put("2",object);
        hashMap.put("3",xiapeixin);

        return Result.ok("ok").data(hashMap);
    }

    /**
     *
     * 在 Sentinel 中所有流控降级相关的异常都是异常类 BlockException 的子类：
     *
     * 流控异常：FlowException
     * 熔断降级异常：DegradeException
     * 系统保护异常：SystemBlockException
     * 热点参数限流异常：ParamFlowException
     *
     * 熔断降级,参数类型需要和原方法匹配,最后加上BlockException;
     * 如果使用其他类里的函数,使用blockHandlerClass 指定类, 但是方法必须是 static;
     */
    public Result d2(String name, BlockException be) {
        String msg = null;
        if(null != be){
            msg = be.getClass().getName();
        }
        return Result.error("熔断降级").data(msg);
    }

    /**
     * 异常降级,抛出异常时调用,默认所有异常; 也有 fallbackClass
     * 可以使用 exceptionsToIgnore指定要忽略的异常;
     * <p>
     * exceptionsToTrace 来指定要跟踪的异常(默认所有异常);
     */
    public Result d3(String name, Throwable te) {
        return Result.error("异常降级").data(te.getClass().getName());
    }


    /*
    #####################################################################################################
     */

    @Autowired
    protected PublicTransactional10x transactional10x;

    @Autowired
    protected PublicTransactional20x transactional20x;



    @GetMapping("/hmily")
    public Result hmily(@RequestParam("money") BigDecimal money) throws AlertException {

        boolean isOk =
                false;
        try {
            isOk = transactional10x.hmilyTransaction(money, 425752943532056576L,425752880537804800L,1);
        } catch (AlertException e) {

           throw new AlertException("调用异常");
        }

        return Result.ok("code").data(isOk);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/rocket/{money}")
    public Result rocket(@PathVariable("money") BigDecimal money) throws AlertException {

        boolean mqTran = transactional10x.rocketMqTran(money);

        return Result.ok("ookk");
    }

}
