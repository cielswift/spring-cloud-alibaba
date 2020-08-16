package com.ciel.scatquick.controller;

import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scatquick.config.MultiRequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(value = "*") //允许这个controller所有跨域
public class ManyRqBodyController {

    /**
     *{
     *     "username": "xiapeixin",
     *     "price": "22.54"
     * }
     */
    @PostMapping("/bodyz")
    public Result bodyz(@MultiRequestBody ScaUser scaUser, @MultiRequestBody ScaGirls scaGirls){

        System.out.println(scaUser);
        System.out.println(scaGirls);

        return Result.ok("okk");
    }
}
