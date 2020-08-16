package com.xia.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 测试自动装配
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableImportSelect.class)
public @interface EnableXiapeixnTest {


}
