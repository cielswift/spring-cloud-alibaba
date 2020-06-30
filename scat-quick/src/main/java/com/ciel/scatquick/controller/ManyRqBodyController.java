package com.ciel.scatquick.controller;

import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scatquick.config.MultiRequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManyRqBodyController {

    @PostMapping("/bodyz")
    public Result bodyz(@MultiRequestBody ScaUser scaUser, @MultiRequestBody ScaGirls scaGirls){

        System.out.println(scaUser);
        System.out.println(scaGirls);

        return Result.ok("okk");
    }
}
