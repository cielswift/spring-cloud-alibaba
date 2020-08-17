package com.ciel.scaconsumer.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ciel.scaapi.dubbo.ApplicationServer;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.SysUtils;
import com.ciel.scaconsumer.brex.BlockAndException;
import com.ciel.scaconsumer.feignext.FeignUrl;
import com.ciel.scaconsumer.feignext.FuckMyLifeXiaPeiXin;
import com.ciel.scaconsumer.feignext.PublicTransactional10x;
import com.ciel.scaconsumer.feignext.PublicTransactional20x;
import com.ciel.scaentity.entity.ScaApplication;
import com.ciel.scaentity.entity.ScaGirls;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
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
     *   check是否检查 ;timeout 超时时间
     *   url = "127.0.0.1:20880"跳过注册中调用
     *   loadbalance  *负载均衡策略，合法值包括：随机，轮循，最少活跃; random, roundrobin, leastactive
     *
     *   cluster 集群容错模式:
     *   failover=org.apache.dubbo.rpc.cluster.support.FailoverCluster
     *      失败自动切换，在调用失败时，失败自动切换，当出现失败，重试其它服务器。通常用于读操作，但重试会带来更长延迟。
     *      可通过retries="2"来设置重试次数(不含第一次)。
     *
     *  failfast=org.apache.dubbo.rpc.cluster.support.FailfastCluster
     *      快速失败，只发起一次调用，失败立即报错。通常用于非幂等性的写操作，比如新增记录。
     *
     * failsafe=org.apache.dubbo.rpc.cluster.support.FailsafeCluster
     *  失败安全，出现异常时，直接忽略。通常用于写入审计日志等操作。
     *
     * failback=org.apache.dubbo.rpc.cluster.support.FailbackCluster
     *  失败自动恢复，后台记录失败请求，定时重发。通常用于消息通知操作。
     *
     * forking=org.apache.dubbo.rpc.cluster.support.ForkingCluster
     * 并行调用多个服务器，只要一个成功即返回。通常用于实时性要求较高的读操作，但需要浪费更多服务资源。
     * 可通过 forks="2" 来设置最大并行数。
     *
     * broadcast=org.apache.dubbo.rpc.cluster.support.BroadcastCluster
     *      广播调用所有提供者，逐个调用，任意一台报错则报错。通常用于通知所有提供者更新缓存或日志等本地资源信息
     *
     *  mock = "fail:return+null" 异常返回null
     */

    @Reference(check = false,timeout = 3000,loadbalance = "roundrobin",cluster = "failfast", retries = 0,
            parameters = {"addCircle.cluster", "failfast"} /*,mock = "fail:return+null" */)
    protected ApplicationServer applicationServer;

    @Autowired
    @Qualifier("restTemplateIp")
    protected RestTemplate restTemplateIp;

    @Autowired
    @Qualifier("restTemplateServer")
    protected RestTemplate restTemplateServer;

    @Autowired
    private LoadBalancerClient balancerClient; //ribbon 负载均衡器

    @Autowired
    protected FuckMyLifeXiaPeiXin fuckMyLifeXiaPeiXin;

    @Autowired
    protected FeignUrl feignUrl;
    /**
     * feign协议
     */
    @Autowired
    protected PublicTransactional20x publicTransactional20x;

    @Autowired
    protected ApplicationContext applicationContext;

    @GetMapping("/rpcs")
    public Result rpcs(){

        String ali = feignUrl.ali();
        System.out.println(ali);

        applicationContext.getEnvironment().getProperty("person.xiapeixin"); //获取配置

        List<String> xiapeixin = fuckMyLifeXiaPeiXin.format("xiapeixin");

        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setName("liuxuewen");
        scaGirls.setBirthday(Faster.now());
        scaGirls.setPrice(new BigDecimal("25.5"));

        String head =
                fuckMyLifeXiaPeiXin.head(SysUtils.currentRequest().getHeader("Authentication"));

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
     * Sentinel降级  (控制台热点规则) 可以配置例外情况 参数例外项
     * value 自定义资源名
     *  blockHandler :降级方法 ; 如果使用其他类里的函数,使用blockHandlerClass 指定类, 但是方法必须是 static; (控制台热点规则)
     *  fallback :异常方法; (同上配合fallbackClass指定类)
     *                  可以使用 exceptionsToIgnore指定要忽略的异常; exceptionsToTrace 来指定要跟踪的异常(默认所有异常);
     *  defaultFallback: 默认的 fallback 函数名称，可选项，通常用于通用的 fallback 逻辑,
     *                  其他类需要可以指定 fallbackClass 为对应的类的 Class
     */
   // @SentinelResource(value = "resource", blockHandler = "block", fallback = "fall",defaultFallback ="def" )
    @SentinelResource(value = "resource", blockHandlerClass = BlockAndException.class,blockHandler = "block",
            fallbackClass = BlockAndException.class  , fallback = "exception", defaultFallback ="def" )
    @GetMapping("/resource")
    public Result d1(@RequestParam("name") String name) {
        /**
         * dubbo调用
         */

        ContextUtil.getContext().getName(); //Sentinel 上下文

        ScaApplication scaApplication = new ScaApplication();
        scaApplication.setName("app");
        scaApplication.setCreateDate(Faster.now());
        ScaApplication select = applicationServer.select(scaApplication);


        /**
         * restTemplate 调用
         */
        String object = null;
        try{
             object = restTemplateIp.getForObject("https://cielswift.github.io/", String.class);
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
     * 熔断降级,参数类型需要和原方法匹配,最后加上BlockException; 返回类型需要与原方法相匹配
     */
    public Result block(String name, BlockException be) {
        String msg = be != null && be.getMessage() != null ? be.getMessage() : "null--";
        return Result.error("熔断降级").data(msg);
    }

    /**
     * 异常降级,抛出异常时调用,默认所有异常;
     */
    public Result fall(String name, Throwable te) {
        String msg = te != null && te.getMessage() != null ? te.getMessage() : "null--";
        return Result.error("异常降级").data(msg);
    }

    /**
     * 若同时配置了 fallback 和 defaultFallback，则只有 fallback 会生效
     * 方法参数列表需要为空，或者可以额外多一个 Throwable 类型的参数用于接收对应的异常
     */
    public Result def(Throwable te){
        String msg = te != null && te.getMessage() != null ? te.getMessage() : "null--";
        return Result.error("默认异常降级方法").data(msg);
    }

    /*
    #############################################################################
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
            isOk = transactional10x.hmilyTransaction(money, 425752943532056576L,
                    425752880537804800L,1);
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
