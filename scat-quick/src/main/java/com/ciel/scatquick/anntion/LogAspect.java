package com.ciel.scatquick.anntion;

import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scatquick.security.realm.ScaCusUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
//        if(((MethodSignature)point.getSignature()).getMethod().getAnnotations()[0] instanceof GetMapping){
//
//        }

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        ScaCusUser scaCusUser = (ScaCusUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //执行方法
        Object result = point.proceed();

        log.info("请求参数:{},请求方式:{},请求URL:{},请求IP:{}," +
                        "响应数据:{} ,当前用户:ID:{},当前用户:姓名:{}, 执行时间:{}ms",
                point.getArgs(),request.getMethod(),request.getRequestURI(),request.getRemoteAddr(),
                result.toString(), scaCusUser.getId(),scaCusUser.getUsername(),System.currentTimeMillis()-str);

        return result;
    }

}
