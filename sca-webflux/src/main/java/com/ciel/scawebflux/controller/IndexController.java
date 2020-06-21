package com.ciel.scawebflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
public class IndexController {

    @GetMapping("/index")
    public Flux<String> index() {
       return Flux.just(UUID.randomUUID().toString());
    }

}
