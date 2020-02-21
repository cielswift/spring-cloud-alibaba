package com.ciel.springcloudalibabaproducer1.controller;

import com.ciel.springcloudalibabaapi.crud.IScaUserService;
import com.ciel.springcloudalibabaapi.feign.PublicTransactional;
import com.ciel.springcloudalibabaentity.ScaUser;
import com.ciel.springcloudalibabaproducer1.feign.TransactionConsumer;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@RestController("transactionProducer")
public class TransactionalProducer implements PublicTransactional {

    @Autowired
    protected IScaUserService userService;

    @Autowired
    protected TransactionConsumer transactionConsumer;

    /**
     *全局事务
     */
    @GlobalTransactional(timeoutMills = 60000)
    @Transactional
    @Override
    @GetMapping("/transactional")
    public boolean transactionPrice(@NotNull BigDecimal price, @NotNull Long sendUserId,
                                    @NotNull Long receiveUserId,@NotNull Integer code) {
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

        boolean shIsOk = transactionConsumer.transactionPrice(price, sendUserId, receiveUserId, shePlatform);

        if(!shIsOk){
            throw new RuntimeException("对方平台余额修改失败");
        }

        String xid = RootContext.getXID();//分支事务id
        System.out.println(xid);

        if(price.compareTo(BigDecimal.TEN) == 0){

            throw new RuntimeException("10元主动异常 ,测试其他平台全局事务是否回滚");
        }

        return true;
    }

}
