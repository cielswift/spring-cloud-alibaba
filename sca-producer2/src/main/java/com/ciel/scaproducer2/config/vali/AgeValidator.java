package com.ciel.scaproducer2.config.vali;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证类
 */
public class AgeValidator implements ConstraintValidator<NotSex,String> {

    @Override
    public void initialize(NotSex constraintAnnotation) {
        //nitialize()可以在验证开始前调用注解里的方法，从而获取到一些注解里的参数，这里用不到

        System.out.println("init");
    }

    @Override //isValid()就是判断是否合法的地方
    public boolean isValid(String msg, ConstraintValidatorContext constraintValidatorContext) {
        return !msg.contains("sex");
    }
}