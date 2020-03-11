package com.ciel.scaproducer2.config.oauth2;

import com.ciel.scaapi.retu.Result;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController

@AllArgsConstructor
public class Oauth2Controller {


    @GetMapping("/oat2/{aa}")
    public Result oat2(@PathVariable("aa")  String s){

        return Result.ok("aaa");
    }
}
