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

        log.info("请求参数:{}", point.getArgs());
        log.info("请求方式:{}", request.getMethod());
        log.info("请求URL:{}", request.getRequestURI());
        log.info("请求IP:{}", request.getRemoteAddr());

        ScaCusUser scaCusUser = (ScaCusUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("当前用户:ID:{},USERNAME:{}", scaCusUser.getId(),scaCusUser.getUsername());

        //执行方法
        Object result = point.proceed();

        log.info("响应数据:{}", result.toString());
        //执行时长
        log.info("执行时间:{}ms", System.currentTimeMillis() - str);

        return result;
    }

}
