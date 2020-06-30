package com.ciel.scaproducer2.controller;

import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaUser;
import io.seata.core.context.RootContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@RestController
@Slf4j
@AllArgsConstructor
public class SeataTransactionController {

    protected IScaUserService userService;

    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/seata")
    public Result seata(@RequestParam("price") BigDecimal price,
                        @RequestParam("sendId") Long sendUserId,
                        @RequestParam("receiveId") Long receiveUserId) throws AlertException {

        String xid = RootContext.getXID();//分支事务id

        System.out.println("事务id" + xid);

        ScaUser user = userService.getById(receiveUserId);

        user.setPrice(user.getPrice().add(price));

        userService.updateById(user); //更新余额

        if (price.compareTo(BigDecimal.ONE) == 0) {
            throw new AlertException("1 元主动异常 测试回滚");
        }

        return Result.ok("账户加钱完成");
    }

}
