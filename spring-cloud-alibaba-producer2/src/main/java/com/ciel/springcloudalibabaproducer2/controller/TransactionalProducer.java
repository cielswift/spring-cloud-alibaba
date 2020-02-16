package com.ciel.springcloudalibabaproducer2.controller;

import com.ciel.springcloudalibabaapi.crud.IScaUserService;
import com.ciel.springcloudalibabaapi.feign.PublicTransactional;
import com.ciel.springcloudalibabaentity.ScaUser;
import com.ciel.springcloudalibabaproducer2.feign.TransactionConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

@RestController("transactionProducer")
public class TransactionalProducer implements PublicTransactional {

    @Autowired
    protected IScaUserService userService;

    @Autowired
    protected TransactionConsumer transactionConsumer;

    @Transactional
    @Override
    @GetMapping("/transactional")
    public boolean transactionPrice(BigDecimal price, Long sendUserId, Long receiveUserId, Integer code) {
        /**
         * 此平台用户
         */
        ScaUser thisPlatformUser;

        /**
         * 对方平台采取的状态
         */
        int shePlatform ;

        if(code == 1) {

            thisPlatformUser = userService.getById(sendUserId);

            thisPlatformUser.setPrice(thisPlatformUser.getPrice().subtract(price));

            shePlatform = -1;
        }else if(code == -1){

            thisPlatformUser = userService.getById(receiveUserId);

            thisPlatformUser.setPrice(thisPlatformUser.getPrice().add(price));

            shePlatform = -1;
        }else{
            throw new RuntimeException("不存在的执行方式-不转帐,不收款");
        }

        if(BigDecimal.ZERO.compareTo(thisPlatformUser.getPrice()) > 0){
            throw new RuntimeException("余额不足");
        }

        boolean thisIsOk = userService.saveOrUpdate(thisPlatformUser);

        if (!thisIsOk) {
            throw new RuntimeException("此平台余额修改失败");
        }

        return true;
    }

    @Transactional
    @GetMapping("/testtran")
    public Object testtran(String code){

        ScaUser user = userService.getById(425752880537804800L);
        user.setImage(code);

        userService.saveOrUpdate(user);

        if("err".equals(code)){
           throw new RuntimeException("err--");
        }

        return Map.of("msg",true);
    }

}
