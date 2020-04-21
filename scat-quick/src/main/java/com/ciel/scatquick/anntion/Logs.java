package com.ciel.scatquick.anntion;

/**
 * 打印日志
 */
public @interface Logs {

    /**
     * 打印前缀
     * @return
     */
    String prefix() default "默认日志:";

    /**
     * 日志级别
     * @return
     */
    String level() default "info";
}
