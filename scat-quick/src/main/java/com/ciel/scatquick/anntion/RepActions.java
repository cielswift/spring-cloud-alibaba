package com.ciel.scatquick.anntion;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)

@Inherited//注解继承
@interface RepActions {
    RepAction[] value();  //重复注解的容器
}