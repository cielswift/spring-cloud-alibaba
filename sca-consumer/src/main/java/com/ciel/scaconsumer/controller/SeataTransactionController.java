package com.ciel.scaconsumer.controller;

import com.ciel.scaapi.retu.Result;
import com.ciel.scaconsumer.feignext.PublicTransactional10x;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Slf4j
@AllArgsConstructor
public class SeataTransactionController {

    protected PublicTransactional10x transactional10x;

    @GetMapping("/seata")
    public Result seata(@RequestParam("money") BigDecimal price) {
       return  transactional10x.seata(price, 425752943532056576L, 425752880537804800L);

    }

}