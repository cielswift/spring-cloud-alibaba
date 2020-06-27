package com.ciel.scaapi.anntion;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

/**
 * 自定义的事务 aop 注解
 */
public @interface TransactionXiaPeiXin {

    /**
     * 需要回滚的异常
     */
    Class<? extends Throwable> rollback() default Exception.class;
}
