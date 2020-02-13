package com.ciel.springcloudalibabaproducer1.controller;

import com.ciel.springcloudalibabaapi.feign.FuckMyLife;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeignController implements FuckMyLife {

    @Override
    @GetMapping("/cc")
    public List<String> fml(@RequestParam("name")String name) {
        return List.of("NAME",name.concat("_-_|"));
    }

}
