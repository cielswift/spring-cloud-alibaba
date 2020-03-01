package com.ciel.springcloudalibabaproducer1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ciel.springcloudalibabaapi.crud.IScaUserService;
import com.ciel.springcloudalibabaapi.exception.AlertException;
import com.ciel.springcloudalibabaapi.feign.PublicTransactional;
import com.ciel.springcloudalibabacommons.mapper.TableAT;
import com.ciel.springcloudalibabaentity.entity.ScaUser;
import com.ciel.springcloudalibabaproducer1.feign.TransactionConsumer;
//import io.seata.core.context.RootContext;
//import io.seata.spring.annotation.GlobalTransactional;
import com.google.gson.JsonObject;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

@RestController
public class TransactionalProducer implements PublicTransactional {

    @Autowired
    protected IScaUserService userService;

    @Autowired
    protected TransactionConsumer transactionConsumer;

    private static final Logger logger = LoggerFactory.getLogger(TransactionalProducer.class);

    /**
     * 全局事务
     */
    // @GlobalTransactional(timeoutMills = 60000)
    @Transactional
    @Override
    @GetMapping("/transactional")
    public boolean transactionPrice(@NotNull BigDecimal price, @NotNull Long sendUserId,
                                    @NotNull Long receiveUserId, @NotNull Integer code) {
        /**
         * 此平台用户
         */
        ScaUser thisPlatformUser;

        /**
         * 对方平台采取的状态
         */
        int shePlatform;

        if (code == 1) {

            thisPlatformUser = userService.getById(sendUserId);

            thisPlatformUser.setPrice(thisPlatformUser.getPrice().subtract(price));

            shePlatform = -1;
        } else if (code == -1) {

            thisPlatformUser = userService.getById(receiveUserId);

            thisPlatformUser.setPrice(thisPlatformUser.getPrice().add(price));

            shePlatform = -1;
        } else {
            throw new RuntimeException("不存在的执行方式-不转帐,不收款");
        }

        if (BigDecimal.ZERO.compareTo(thisPlatformUser.getPrice()) > 0) {
            throw new RuntimeException("余额不足");
        }

        boolean thisIsOk = userService.saveOrUpdate(thisPlatformUser);

        if (!thisIsOk) {
            throw new RuntimeException("此平台余额修改失败");
        }

        boolean shIsOk = transactionConsumer.transactionPrice(price, sendUserId, receiveUserId, shePlatform);

        if (!shIsOk) {
            throw new RuntimeException("对方平台余额修改失败");
        }

        // String xid = RootContext.getXID();//分支事务id
        //  System.out.println(xid);

        if (price.compareTo(BigDecimal.TEN) == 0) {

            throw new RuntimeException("10元主动异常 ,测试其他平台全局事务是否回滚");
        }

        return true;
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Autowired
    protected RedisTemplate redisTemplate;

    /**
     * tcc 的try 方法; 只要标记这个注解@Hmily
     * <p>
     * 在注解中指定confirm 和cancel ; 三个方法参数一致;cancel也要加@Transactional
     */
    @Hmily(confirmMethod = "confirm", cancelMethod = "cancel")

    @Transactional(rollbackFor = AlertException.class)
    @PutMapping(value = "/hmily/{price}/{sendUserId}/{receiveUserId}/{code}")
    @Override
    public boolean hmilyTransaction(@PathVariable("price") @NotEmpty(message = "kong") BigDecimal price,
                                    @PathVariable("sendUserId") @NotNull(message = "kong") Long sendUserId,
                                    @PathVariable("receiveUserId") @NotNull(message = "kong") Long receiveUserId,
                                    @PathVariable("code") @NotNull(message = "kong") Integer code) throws AlertException {

        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();

        /**
         * 幂等校验
         */
        Object tryEx = redisTemplate.opsForValue().get("try_" + transId);
        if (null != tryEx) {
            System.out.println("失败:try已经执行");
            return false;
        }

        /**
         * 悬挂处理 ,confirm 或 cancel 已经执行了 try不再执行;
         */
        Object confirmEx = redisTemplate.opsForValue().get("confirm_" + transId);
        Object cancelEx = redisTemplate.opsForValue().get("cancel_" + transId);
        if (null != confirmEx) {
            System.out.println("失败:confirm_ 已经执行");
            return false;
        } else if (null != cancelEx) {
            System.out.println("失败:cancel_ 已经执行");
            return false;
        }

        /**
         * 业务逻辑
         */
        boolean update = userService.update(new LambdaUpdateWrapper<ScaUser>()
                .setSql("price = price-" + price.toString())
                .eq(ScaUser::getId, sendUserId).ge(ScaUser::getPrice, price));

        if (!update) {
            throw new AlertException("失败:余额不足");
        }

        //插入try 执行记录
        redisTemplate.opsForValue().set("try_" + transId, 1);
        System.out.println("本地事务try已经执行");

        boolean b = transactionConsumer.hmilyTransaction(price, sendUserId, receiveUserId, code);

        if (!b) {
            throw new AlertException("对方异常");
        }

        return true;
    }


    public boolean confirm(BigDecimal price, Long sendUserId, Long receiveUserId, Integer code) throws AlertException {

        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();

        System.out.println("p1 confirm" + transId);

        return true;
    }

    @Transactional(rollbackFor = AlertException.class)
    public boolean cancel(BigDecimal price, Long sendUserId, Long receiveUserId, Integer code) throws AlertException {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();

        /**
         * 幂等校验
         */
        Object cancelEx = redisTemplate.opsForValue().get("cancel_" + transId);
        if (null != cancelEx) {
            System.out.println("失败:事务已经回滚");
            return false;
        }

        /**
         * 空回滚; try_没有执行,不允许cancel_执行
         */
        Object tryEx = redisTemplate.opsForValue().get("try_" + transId);
        if (null == tryEx) {
            System.out.println("失败:事务没有执行,不允许回滚");
            return false;
        }
        /**
         * 回滚余额
         */
        boolean update = userService.update(new LambdaUpdateWrapper<ScaUser>().
                setSql("price = price+" + price.toString())  //取反
                .eq(ScaUser::getId, sendUserId));
        if (!update) {
            throw new AlertException("失败:严重异常, 回滚失败, 需要人工介入:" + transId);
        }

        //插入cancel 执行记录
        redisTemplate.opsForValue().set("cancel_" + transId, 1);
        System.out.println("事务cancel已经执行");
        return true;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Autowired
    protected RocketMQTemplate rocketMQTemplate;


    @Override
    @PutMapping("/rocket_mq/{price}")
    public boolean rocketMqTran(@PathVariable("price") BigDecimal price) throws AlertException {

        HashMap<String, Object> jb = new HashMap<>(1 << 3);
        jb.put("ACCOUNT_SEND",425752943532056576L);
        jb.put("ACCOUNT_RECEIVE",425752880537804800L);
        jb.put("PRICE",price);
        //创建事务id
        jb.put("TXNO",UUID.randomUUID().toString());

        Message<String> msg = MessageBuilder.withPayload(JSON.toJSONString(jb)).build();

        TransactionSendResult result = //发送一条事务消息
                rocketMQTemplate.sendMessageInTransaction("producer_group1",
                        "top_1", msg, null);

        System.out.println(result.getSendStatus().toString());
        logger.info(result.getSendStatus().toString());

        return true;
    }



}
