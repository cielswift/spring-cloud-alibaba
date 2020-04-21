package com.ciel.scatquick.controller;

import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scatquick.anntion.Logs;
import com.ciel.scatquick.beanload.AppEvn;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Validated
public class HiController {

    protected ApplicationContext applicationContext;


    @EventListener  //代替事件监听器,不用写ApplicationListener
    public void listenHello(AppEvn event) {
        System.out.println(event.getName());
    }

    @GetMapping("/pu")
    public Result pu(){

        Map<String, String> map = new HashMap<>();
        map.put("name","xiapeixin");

        ScaUser scaUser = new ScaUser();

        String[] paramNames = { "staffName", "status" };
        Object[] paramValue = { "陈", 431173673996394496L };
        //最后一个参数是返回类型 null 则返回普通数组(可以传VO对象、Map.class)

        //applicationContext.publishEvent(new AppEvn(this,"aa")); //发布事件 方法1
        listenHello(new AppEvn(this,"aa")); //发布事件 方法2
        return Result.ok();
    }

    @Logs
    @GetMapping("/hi")
    public Result hi(String name){

        return Result.ok().data(name);
    }

    @GetMapping("/hk")
    public Result hk(String name, Integer age){
        long str = System.currentTimeMillis();

        String cc = name +age;

        System.out.println(System.currentTimeMillis()-str);
        return Result.ok().data(cc);
    }

    @PostMapping("/hkk")
    public Result hkk(@RequestBody Map<String, String> map){
        return Result.ok().data(map);
    }

    @PutMapping("/ms/{ms}/{ks}")
    public Result ms(@PathVariable("ms") String ms,@PathVariable("ks") String ks,
                     HttpServletResponse response){
        response.setHeader("Authentication", UUID.randomUUID().toString());

        return Result.ok().data(ms);
    }
}
