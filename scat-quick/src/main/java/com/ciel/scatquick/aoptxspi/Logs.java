package com.ciel.scatquick.aoptxspi;

import java.lang.annotation.*;

/**
 * 打印日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Logs {

    /**
     * 打印前缀
     * @return
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
