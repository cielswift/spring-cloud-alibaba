package com.ciel.scatquick.anntion;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;

@RepAction(value = "bb")
@RepAction(value = "aa")
public class TestQA<@RepAction("cc") T> {
    public static void main(String[] args) throws IOException {

        RepAction annotation = TestQA.class.getAnnotation(RepAction.class); //null

        RepAction[] annotationsByType = TestQA.class.getAnnotationsByType(RepAction.class); //获取多个重复的注解

        Annotation[] annotations = TestQA.class.getAnnotations(); //这里看到 其实是RepActions ,重复注解的容器

        System.out.println(annotation);

        Arrays.stream(annotationsByType).forEach(System.out::println);

        Arrays.stream(annotations).forEach(System.out::println);

    }

}