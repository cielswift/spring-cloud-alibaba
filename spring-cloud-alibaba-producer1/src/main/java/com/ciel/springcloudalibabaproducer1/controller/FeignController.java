package com.ciel.springcloudalibabaproducer1.controller;

import com.ciel.springcloudalibabaapi.crud.IScaUserService;
import com.ciel.springcloudalibabaapi.feign.FuckMyLife;
import com.ciel.springcloudalibabaapi.retu.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class FeignController implements FuckMyLife {

    @Autowired
    protected IScaUserService scaUserService;

    @Override
    @GetMapping("/cc")
    public List<String> fml(@RequestParam("name")String name) {

        LinkedList<String> strings = new LinkedList<>();
        strings.add("name-xiapeixin");
        return strings;
    }


    @GetMapping("/test")
    public Object testTransaction(){

        return scaUserService.testTransaction();
    }
}
