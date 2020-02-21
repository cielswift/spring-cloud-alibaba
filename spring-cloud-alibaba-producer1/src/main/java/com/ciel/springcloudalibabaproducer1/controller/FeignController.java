package com.ciel.springcloudalibabaproducer1.controller;

import com.ciel.springcloudalibabaapi.crud.IScaUserService;
import com.ciel.springcloudalibabaapi.feign.FuckMyLife;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeignController implements FuckMyLife {

    @Autowired
    protected IScaUserService scaUserService;

    @Override
    @GetMapping("/cc")
    public List<String> fml(@RequestParam("name")String name) {
        return List.of("NAME",name.concat("_-_|"));
    }


    @GetMapping("/test")
    public Object testTransaction(){

        return scaUserService.testTransaction();
    }
}
