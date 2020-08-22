package com.ciel.scareactive.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class LogtoAct {

    @Pointcut("@annotation(com.ciel.scareactive.aop.Logto)")
    public void logPointCut() {
    }


    /**
     * 环绕通知类型 AspectJAroundAdvice
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long str = System.currentTimeMillis();
        log.warn("开始计时:{}",str);

        Object proceed = point.proceed();

        log.warn("结束统计: {}",System.currentTimeMillis()-str);
        return proceed;

    }
}
