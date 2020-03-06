package com.ciel.scaproducer1.controller;

import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scaproducer1.feign.TransactionConsumer;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * seate 控制分布式事务
 */
@RestController
@Slf4j
@AllArgsConstructor
public class SeataTransactionController {

    protected IScaUserService userService;

    protected TransactionConsumer transactionConsumer;

    /**
     * 全局事务
     */

    @GlobalTransactional(timeoutMills = 60000)
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/seata")
    public Result seata(@NotNull BigDecimal price, @NotNull Long sendUserId, @NotNull Long receiveUserId) {

        String xid = RootContext.getXID();//分支事务id
        System.out.println("事务id" + xid);

        ScaUser user = userService.getById(sendUserId);

        if (price.compareTo(user.getPrice()) > 0) {

            System.out.println("余额不足");
            return Result.error("余额不足");
        }

        user.setPrice(user.getPrice().multiply(price));

        if(userService.updateById(user)){

            Result seata = transactionConsumer.seata(price, sendUserId, receiveUserId);
            if(!seata.isOk()){
                throw new RuntimeException("对方平台发生异常: 取消交易");
            }

        }else{
            throw new RuntimeException("本地错误");
        }

        if (price.compareTo(BigDecimal.TEN) == 0) {

            throw new RuntimeException("10元主动异常 ,测试其他平台全局事务是否回滚");
        }

        return Result.ok("交易完成");
    }

}
