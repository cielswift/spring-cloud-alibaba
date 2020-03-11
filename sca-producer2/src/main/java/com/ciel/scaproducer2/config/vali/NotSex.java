package com.ciel.scaproducer2.config.vali;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * 自定义校验注解
 */

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = AgeValidator.class) //它表示这个注解是一个验证注解，并且指定了一个实现验证逻辑的验证器
public @interface NotSex {

    String message() default "不能做爱"; //message()指明了验证失败后返回的消息，此方法为@Constraint要求

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}