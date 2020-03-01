package com.ciel.springcloudalibabaproducer1.mq;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ciel.springcloudalibabaapi.crud.IScaUserService;
import com.ciel.springcloudalibabaapi.exception.AlertException;
import com.ciel.springcloudalibabaentity.entity.ScaUser;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RocketMQCCService {

    @Autowired
    protected IScaUserService userService;

    @Autowired
    protected RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    public boolean rocketMqTran(BigDecimal price,String txNo) {

        if (null == redisTemplate.opsForValue().get(txNo)) {

            boolean update = userService.update(new LambdaUpdateWrapper<ScaUser>()
                    .setSql("price = price-".concat(price.toString()))
                    .eq(ScaUser::getId, 425752943532056576L).ge(ScaUser::getPrice, price));

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
