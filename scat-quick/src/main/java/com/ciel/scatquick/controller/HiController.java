package com.ciel.scatquick.controller;

import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scatquick.anntion.Logs;
import com.ciel.scatquick.beanload.AppEvn;
import com.ciel.scatquick.oauth2.Constants;
import com.ciel.scatquick.oauth2.HttpClientUtils;
import com.ciel.scatquick.oauth2.URLEncodeUtil;
import com.ciel.scatquick.security.SecurityUtil;
import com.ciel.scatquick.security.realm.ScaCusUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Data
@RestController
@AllArgsConstructor
@Validated
public class HiController {

    protected ApplicationContext applicationContext;

    protected WebApplicationContext webApplicationContext;

    protected IScaUserService scaUserService;

    protected RedisTemplate<String, Object> redisTemplate;

    @EventListener  //代替事件监听器,不用写ApplicationListener
    public void listenHello(AppEvn event) {
        System.out.println(event.getName());
    }

    @GetMapping("/pu")
    public Result pu(){
        //applicationContext.publishEvent(new AppEvn(this,"aa")); //发布事件 方法1
        listenHello(new AppEvn(this,"aa")); //发布事件 方法2
        return Result.ok();
    }

    /**
     * 获取验证码
     */
    @GetMapping("/sms")
    public void sms(@RequestParam("username") String username,
                    @RequestParam("phone") String phone){
        //发送验证码逻辑;

        redisTemplate.opsForValue().set("sms_".concat(username),"xia123",30, TimeUnit.SECONDS);
    }

    @Autowired
    protected Constants constants;

    @GetMapping("/qq")
    public Result qq(@NotNull String str) throws Exception {

        return Result.ok().data("");
    }


    @Logs
    @GetMapping("/hi")
    @PreAuthorize("hasAnyAuthority('add') and hasAnyRole('ADMIN')")
    public Result hi(String name){

        List<ScaUser> lists = scaUserService.lists(name);

//        List<ScaUser> list = scaUserService.list();
//
//        redisTemplate.opsForValue().set("name", list);
//
//        List<ScaUser> result = (List<ScaUser>)redisTemplate.opsForValue().get("name");
//
//        ScaCusUser scaUser = SecurityUtil.currentScaUser();

        return Result.ok().data(lists);
    }


    @Logs
    @GetMapping("/ha")
    @PreAuthorize("hasAnyAuthority('add') and hasAnyRole('ADMIN')")
    public Result ha(String name){
        boolean byName = scaUserService.deleteByName(name);
        return Result.ok().data(byName);
    }


    @GetMapping("/hk")
    public Result hk(String name, Integer age){
        long str = System.currentTimeMillis();

        String cc = name +age;

        System.out.println(System.currentTimeMillis()-str);
        return Result.ok().data(cc);
    }

    @PostMapping("/hkk")
    public Result hkk(@RequestBody Map<String, String> map){
        return Result.ok().data(map);
    }

    @PutMapping("/ms/{ms}/{ks}")
    public Result ms(@PathVariable("ms") String ms,@PathVariable("ks") String ks,
                     HttpServletResponse response){
        response.setHeader("Authentication", UUID.randomUUID().toString());

        return Result.ok().data(ms);
    }
}