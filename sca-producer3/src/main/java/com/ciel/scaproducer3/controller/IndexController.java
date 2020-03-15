package com.ciel.scaproducer3.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.ciel.scaapi.crud.IScaGirlsService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaGirls;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController

@Slf4j
@AllArgsConstructor
public class IndexController {

    protected IScaGirlsService scaGirlsService;

    @GetMapping("/")
    @Transactional(rollbackFor = Exception.class)
    public Result index(Byte code) throws AlertException {

        List<ScaGirls> list = scaGirlsService.list();

//        UpdateWrapper<ScaGirls> up = new UpdateWrapper<ScaGirls>();
//        up.set("name","安妮海瑟薇").eq("name","特朗普");
//
//        boolean update1 = scaGirlsService.update(up);

        boolean update = scaGirlsService.update(new LambdaUpdateWrapper<ScaGirls>()
                .set(ScaGirls::getName, "安妮海瑟薇丝袜美腿")
                .eq(ScaGirls::getName, "安妮海瑟薇丝袜"));

        if(code==0){
            throw new AlertException("%2 ex");
        }

        return Result.ok("welcome").body(list);
    }

    @GetMapping("/cud")
    public Result cud(){

        for(int i = 0; i< 100 ; i++){
            ScaGirls scaGirls = new ScaGirls();
            scaGirls.setName("特朗普");
            scaGirls.setPrice(new BigDecimal("55.49"));
            scaGirls.setUserId(System.currentTimeMillis());

            boolean save = scaGirlsService.save(scaGirls);

            System.out.println(save);
        }

       return Result.ok();
    }

}
