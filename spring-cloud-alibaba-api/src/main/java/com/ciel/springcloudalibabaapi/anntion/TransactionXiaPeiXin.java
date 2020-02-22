package com.ciel.springcloudalibabaapi.anntion;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TransactionXiaPeiXin {

    /**
     * 需要回滚的异常
     */
    Class<? extends Throwable> rollback() default Exception.class;
}
