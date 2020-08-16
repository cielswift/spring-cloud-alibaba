package com.ciel.scatquick.aoptxspi.nati;

import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

public class ExceptionAdvice implements ThrowsAdvice {
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex){
        System.err.println(ex);
    }
}
