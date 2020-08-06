package com.ciel.scatquick.aoptxspi;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 打印日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogsAnnal {

    /**
     * 很多注解的不同属性起着相同的作用
     */
    @AliasFor(annotation = LogsAnnal.class, value = "prefix")
    String value();
    /**
     * 打印前缀
     */
    String prefix() default "默认日志->";

    /**
     * 日志级别
     * @return
     */
    Level level() default Level.INFO;

    public static enum Level{
        DEBUG,
        INFO,
        WARNING,
        ERROR
    }
}
