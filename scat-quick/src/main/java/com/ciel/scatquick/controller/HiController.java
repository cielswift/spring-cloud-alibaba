package com.ciel.scatquick.controller;

import com.ciel.scaapi.retu.Result;
import com.ciel.scatquick.beanload.AppEvn;
import com.ciel.scatquick.s2d.ZX;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class HiController {

    protected ApplicationContext applicationContext;
    protected ZX zx;


    @EventListener  //代替事件监听器,不用写ApplicationListener
    public void listenHello(AppEvn event) {
        System.out.println(event.getName());
    }


    @GetMapping("/pu")
    public Result pu(){

        //applicationContext.publishEvent(new AppEvn(this,"aa")); //发布事件 方法1

        listenHello(new AppEvn(this,"aa")); //发布事件 方法2
        return Result.ok();
    }


    @GetMapping("/hi")
    public Result hi(String name){
        int a = 10 /0;
        return Result.ok().data(name);
    }

    @PostMapping("/hk")
    public Result hk(String name, Integer age){
        return Result.ok().data(name+age);
    }


    @PutMapping("/ms/{ms}")
    public Result ms(@PathVariable("ms") String ms){
        return Result.ok().data(ms);
    }
}
