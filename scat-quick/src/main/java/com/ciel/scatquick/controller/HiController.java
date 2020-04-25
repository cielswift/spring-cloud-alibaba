package com.ciel.scatquick.controller;

import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scatquick.anntion.Logs;
import com.ciel.scatquick.beanload.AppEvn;
import com.ciel.scatquick.security.SecurityUtil;
import com.ciel.scatquick.security.realm.ScaCusUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Data
@RestController
@AllArgsConstructor
@Validated
public class HiController {

    protected ApplicationContext applicationContext;

    protected IScaUserService scaUserService;

    protected RedisTemplate<String, Object> redisTemplate;

    @EventListener  //代替事件监听器,不用写ApplicationListener
    public void listenHello(AppEvn event) {
        System.out.println(event.getName());
    }

    @GetMapping("/pu")
    public Result pu(){

        Map<String, String> map = new HashMap<>();
        map.put("name","xiapeixin");

        ScaUser scaUser = new ScaUser();

        String[] paramNames = { "staffName", "status" };
        Object[] paramValue = { "陈", 431173673996394496L };
        //最后一个参数是返回类型 null 则返回普通数组(可以传VO对象、Map.class)

        //applicationContext.publishEvent(new AppEvn(this,"aa")); //发布事件 方法1
        listenHello(new AppEvn(this,"aa")); //发布事件 方法2
        return Result.ok();
    }


    @Logs
    @GetMapping("/hi")
    @PreAuthorize("hasAnyAuthority('add') and hasAnyRole('ADMIN')")
    public Result hi(String name){

        List<ScaUser> list = scaUserService.list();

        redisTemplate.opsForValue().set("name", list);

        List<ScaUser> result = (List<ScaUser>)redisTemplate.opsForValue().get("name");

        ScaCusUser scaUser = SecurityUtil.currentScaUser();

        return Result.ok().data(result);
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
