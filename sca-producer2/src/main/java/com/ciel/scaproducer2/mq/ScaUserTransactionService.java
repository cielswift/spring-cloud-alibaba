package com.ciel.scaproducer2.mq;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaentity.entity.ScaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ScaUserTransactionService {

    @Autowired
    protected IScaUserService userService;

    @Autowired
    protected RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    public boolean tranAdd(BigDecimal price,String txNo) throws AlertException {

        if (null == redisTemplate.opsForValue().get(txNo)) {

            boolean update = userService.update(new LambdaUpdateWrapper<ScaUser>()
                    .setSql("price = price+".concat(price.toString()))
                    .eq(ScaUser::getId, 425752880537804800L).ge(ScaUser::getPrice, price));

            if(price.compareTo(new BigDecimal("45.5"))==0){
                throw new AlertException("45.5人为异常");
            }

            if (update) {
                //添加事务日志
                redisTemplate.opsForValue().set(txNo, 1);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
