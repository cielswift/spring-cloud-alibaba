package com.ciel.scatquick.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ciel.scaapi.crud.IScaDictService;
import com.ciel.scaapi.crud.IScaGirlsService;
import com.ciel.scaapi.crud.IScaRoleService;
import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.FileUpload2Nginx;
import com.ciel.scaentity.entity.ScaDict;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scaentity.entity.ScaRole;
import com.ciel.scaentity.entity.ScaUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sharding")
@Slf4j
@AllArgsConstructor
public class ShardingJDBCController {

    protected IScaGirlsService scaGirlsService;

    protected IScaUserService scaUserService;

    protected IScaRoleService scaRoleService;

    protected IScaDictService scaDictService;

    protected WebApplicationContext webApplicationContext;

    protected FileUpload2Nginx fileUpload2Nginx;


    @PostMapping("/upl")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        String img = fileUpload2Nginx.imgSaveReturn(file);
        return Result.ok().data(img);
    }


    @GetMapping("/tran")
    @Transactional(rollbackFor = Exception.class)
    // 支持TransactionType.LOCAL, TransactionType.XA, TransactionType.BASE
    //Sharding 事务注解
    @ShardingTransactionType(TransactionType.XA)
    public Result tran(@RequestParam(value = "type",required = false,defaultValue = "1") Integer type,
                       @RequestParam(value = "date",required = false,defaultValue = "2020-05-24") Date date,
                       ScaUser scaUser) throws AlertException {

        List<ScaGirls> list = scaGirlsService.list();

        Set<Long> collect = list.stream().map(l -> l.getId()).collect(Collectors.toSet());

        scaGirlsService.remove(new LambdaQueryWrapper<ScaGirls>().in(ScaGirls::getId,collect));

        if(type.equals(2)){
            throw new AlertException("human exception");
        }

        return Result.ok().data(list);
    }


    @GetMapping("/hello")
    public Result hello(){

        List<ScaUser> list = scaUserService.list();

        List<ScaRole> roles = scaRoleService.list();

        ScaDict scaDict = new ScaDict();
        scaDict.setName("全国");
        scaDict.setValue("COUNTRY");
        scaDict.setDetail("全国范围");

        scaDictService.save(scaDict);

        for(int i = 0; i< 10 ; i++){
            ScaGirls scaGirls = new ScaGirls();
            scaGirls.setName("洛丽塔");
            scaGirls.setPrice(new BigDecimal("55.49"));
            scaGirls.setUserId(System.currentTimeMillis());
            boolean save = scaGirlsService.save(scaGirls);
            System.out.println(save);
        }

        List<ScaGirls> price = scaGirlsService.list(new QueryWrapper<ScaGirls>()
                .between("price", "20.55", "60.55"));

        return Result.ok().data(price);
    }
}
