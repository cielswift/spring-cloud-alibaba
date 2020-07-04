package com.ciel.scaproducer1.controller;

import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scaproducer1.feign.TransactionConsumer;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.spring.boot.autoconfigure.properties.SeataProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/**
 * seate 控制分布式事务
 */
@RestController

@Slf4j
@AllArgsConstructor
public class SeataTransactionController {
    protected IScaUserService userService;
    protected TransactionConsumer transactionConsumer;

    protected ApplicationContext applicationContext;

    /**
     * 全局事务
     */
    @GlobalTransactional(timeoutMills = 60000)
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/seata")
    public Result seata(@RequestParam("price") BigDecimal price,
                        @RequestParam("sendId") Long sendUserId,
                        @RequestParam("receive") Long receiveUserId) throws AlertException, InterruptedException, SQLException {

        String xid = RootContext.getXID();//全局事务id
        System.out.println("事务id" + xid);

        ScaUser user = userService.getById(sendUserId);

        if (price.compareTo(user.getPrice()) == 1) {
            log.info("LAST MONEY IN NOT ");
            return Result.error("余额不足");
        }

        ScaUser scaUser = user.setPrice(user.getPrice().subtract(price));


        if (userService.updateById(user)) { //更新余额

            Result seata = transactionConsumer.seata(price, sendUserId, receiveUserId);

            if (!seata.isOk()) {
                throw new AlertException("对方平台发生异常: 取消交易");
            }
        }

        if (price.compareTo(BigDecimal.TEN)  == 0) {
            throw new AlertException("10元主动异常 ,测试其他平台全局事务是否回滚");
        }

        return Result.ok("交易完成");
    }


}
