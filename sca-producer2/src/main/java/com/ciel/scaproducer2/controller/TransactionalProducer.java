package com.ciel.scaproducer2.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ciel.scaapi.crud.AAASe;
import com.ciel.scaapi.crud.IScaOrderService;
import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.feign.PublicTransactional;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaOrder;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scaentity.type2.Person;
import com.ciel.scaproducer2.config.relm.CustomUser;
import com.ciel.scaproducer2.config.vali.NotSex;
import com.ciel.scaproducer2.config.vali.People;
import com.ciel.scaproducer2.feign.TransactionConsumer;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
//@Validated
public class TransactionalProducer implements PublicTransactional {

    @Autowired
    protected IScaUserService userService;

    @Autowired
    protected TransactionConsumer transactionConsumer;


    @PreAuthorize("hasAnyRole('MANAGER') and hasAnyAuthority('CHANGE')")
    @PutMapping(value = "/sec/{yy}",
            produces = {"application/toString","application/json","application/xml"})
    public Result sec(@PathVariable("yy") @NotSex String y){

        //需要在类上增加@Validated 注解
        //如果 yy中含有. 需要{yy:.+}
        CustomUser c =  (CustomUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return Result.ok("sec 访问成功"+y).data(c);
    }

    @GetMapping("/secn")
    public Result sec2(@Validated(People.Big.class) People people){

        // @Valid 不能识别 自定义注解,以及含有group的注解
        return Result.ok("sec 访问成功");
    }

    
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

        //String xid = RootContext.getXID();//分支事务id
        //System.out.println(xid);

        return true;
    }

    //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Autowired
    protected RedisTemplate redisTemplate;

    @PutMapping(value = "/hmily/{price}/{sendUserId}/{receiveUserId}/{code}")
    @Override
    //@Hmily(confirmMethod = "confirm",cancelMethod = "cancel")
    public boolean hmilyTransaction(@PathVariable("price") @NotEmpty(message="kong") BigDecimal price,
                                    @PathVariable("sendUserId") @NotNull(message="kong") Long sendUserId,
                                    @PathVariable("receiveUserId") @NotNull(message="kong") Long receiveUserId,
                                    @PathVariable("code") @NotNull(message="kong") Integer code) throws AlertException {

        System.out.println("p2 开启try");

        return true;
    }

    @Transactional(rollbackFor = AlertException.class)
    public boolean confirm(BigDecimal price,Long sendUserId, Long receiveUserId, Integer code) throws AlertException {

        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();

        Object confirmEx = redisTemplate.opsForValue().get("confirm_"+transId);
        if(null != confirmEx) {
            System.out.println("失败:confirm_ 已经执行");
            return false;
        }

        //加钱
        boolean update = userService.update(new LambdaUpdateWrapper<ScaUser>()
                .setSql("price = price+"+price.toString())
                .eq(ScaUser::getId, receiveUserId));

        if(!update){
            throw new AlertException("更新失败");
        }

        //插入confirm_记录
        redisTemplate.opsForValue().set("confirm_"+transId,1);
        System.out.println("p2 confirm_ 提交");
        return true;
    }

    public boolean cancel(BigDecimal price,Long sendUserId, Long receiveUserId, Integer code) throws AlertException{

        System.out.println("p2 开启cancel");
        return true;
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    @PutMapping("/rocket_mq/{price}")
    @Transactional(rollbackFor = Exception.class)
    public boolean rocketMqTran(@PathVariable("price") BigDecimal price) throws AlertException {

        return userService.update(new LambdaUpdateWrapper<ScaUser>()
                .setSql("price = price+".concat(price.toString()))
                .eq(ScaUser::getId, 425752880537804800L).ge(ScaUser::getPrice, price));
    }

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Autowired
    protected AAASe aaaSe;
    //@Transactional
    @GetMapping("/testtran")
    public Object testtran(String code){

        aaaSe.testCustomTransaction();

    //    aaaSe.testTransaction();

       // userService.testTransaction();

//        ScaUser user = userService.getById(425752880537804800L);
//        user.setImage(code);
//
//        userService.saveOrUpdate(user);
//
//        if("err".equals(code)){
//           throw new RuntimeException("err--");
//        }

        return Result.ok("msg");
    }


    @GetMapping("/te2")
    public Result te2(){

        ScaUser scaUser = new ScaUser();
        scaUser.setUsername("xiapeixin202");

        Person p = new Person();
        p.setName("xia");
        p.setAge(22);
        p.setGender(false);
        p.setByDate(LocalDateTime.now());

        scaUser.setPerson(p);

        userService.save(scaUser);

        return Result.ok("ok");
    }


    @GetMapping("/te2g")
    public Result te2g(){

        List<ScaUser> list = userService.list();

        return Result.ok("ok").data(list);
    }

    @Autowired
    protected IScaOrderService scaOrderService;

    @GetMapping("/dtt")
    public Result dtt(){

        ScaOrder scaOrder = new ScaOrder();
        scaOrder.setOrderNumber(System.currentTimeMillis());

        scaOrderService.save(scaOrder);

        return Result.ok("ok");
    }

    @GetMapping(value = "/dttg")
    public Result dttg(){

        //MediaType.APPLICATION_JSON_UTF8_VALUE
        //produces：它的作用是指定返回值类型，不但可以设置返回值类型还可以设定返回值的字符编码；
        //consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;

        LambdaQueryWrapper<ScaOrder> lambdaQueryWrapper = new LambdaQueryWrapper<ScaOrder>()
                .eq(ScaOrder::getOrderNumber, 123456)
                .or(i -> i.eq(ScaOrder::getId, 123).ne(ScaOrder::getId, 456))
                .and(i -> i.eq(ScaOrder::getId,25))
                .orderByAsc(ScaOrder::getOrderNumber);

        List<ScaOrder> list = scaOrderService.list();
        return Result.ok("ok").data(list);
    }


}
