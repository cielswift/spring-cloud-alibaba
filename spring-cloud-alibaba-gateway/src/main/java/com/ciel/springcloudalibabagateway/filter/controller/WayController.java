package com.ciel.springcloudalibabagateway.filter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WayController {

    @GetMapping("/mono")
    public Mono<String> mono() {
        return Mono.just("hello webflux");
    }
}
