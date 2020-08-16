package com.ciel.scatquick.anntion;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
@Repeatable(value = RepActions.class)  //可重复注解,指定容器

//@Target(ElementType.TYPE_PARAMETER) //注解可以放在泛型上
//@Target(ElementType.TYPE_USE) //注解可以放在任何地方上

@Inherited //注解继承
public @interface RepAction {
    String [] value();
}

