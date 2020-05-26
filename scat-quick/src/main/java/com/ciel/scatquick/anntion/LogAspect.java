package com.ciel.scatquick.anntion;

import com.ciel.scaapi.util.Faster;
import com.ciel.scatquick.security.realm.ScaCusUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component

@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.ciel.scatquick.anntion.Logs)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        long str = System.currentTimeMillis();

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, Serializable> info = new HashMap<>();

        try{
            ScaCusUser  scaCusUser = (ScaCusUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            info.put("name",scaCusUser.getUsername());
            info.put("id",scaCusUser.getId());
        }catch (Exception e){
            info.put("name","null");
            info.put("id",0);
        }

        //执行方法
        Object methodReturn = point.proceed();

        String result = "null";
        if(Faster.isNotNull(methodReturn)){
            result = methodReturn.toString();
        }

        log.info("请求参数:{},请求方式:{},请求URL:{},请求IP:{}," +
                        "响应数据:{} ,当前用户:ID:{},当前用户:姓名:{}, 执行时间:{}ms",
                point.getArgs(), request.getMethod(), request.getRequestURI(), request.getRemoteAddr(),
                result, info.get("id"), info.get("name"), System.currentTimeMillis() - str);

        return result;
    }

}
