package com.ciel.scatquick.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ciel.scaapi.crud.IScaDictService;
import com.ciel.scaapi.crud.IScaGirlsService;
import com.ciel.scaapi.crud.IScaRoleService;
import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.FileUpload2Nginx;
import com.ciel.scaapi.util.SysUtils;
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

    protected IScaUserService iScaUserService;


    @GetMapping("/users")
    public Result users(){
        return Result.ok().data(iScaUserService.list());
    }

    @GetMapping("/girls/list")
    public Result girls(){
        QueryWrapper<ScaGirls> wrapper = SysUtils.autoCnd(ScaGirls.class);
        IPage<ScaGirls> page = SysUtils.autoPage(ScaGirls.class);
        IPage<ScaGirls> result = scaGirlsService.page(page, wrapper);
        return Result.ok().pageData(result);
    }


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

        Set<Long> collect = list.stream().map(ScaGirls::getId).collect(Collectors.toSet());

        scaGirlsService.remove(new LambdaQueryWrapper<ScaGirls>().in(ScaGirls::getId,collect));

        if(type.equals(2)){
            throw new AlertException("human exception");
        }

        return Result.ok().data(list);
    }


    @GetMapping("/hello")
    public Result hello(){

        ScaDict scaDict = new ScaDict();
        scaDict.setName("范围");
        scaDict.setValue("RANGE");
        scaDict.setDetail("RANGE");
        scaDictService.save(scaDict);

        for(int i = 0; i< 20 ; i++){
            ScaGirls scaGirls = new ScaGirls();
            scaGirls.setName(String.format("夏%s%s",i,i*7));
            scaGirls.setPrice(new BigDecimal(String.format("%.2f",Math.random()*100)));
            scaGirls.setUserId(System.currentTimeMillis()-7777);
            scaGirls.setBirthday(Faster.now());
            boolean save = scaGirlsService.save(scaGirls);
            System.out.println(save);
        }

        List<ScaGirls> price = scaGirlsService.list(new QueryWrapper<ScaGirls>()
                .between("price", "20.55", "60.55"));

        return Result.ok().data(price);
    }
}
