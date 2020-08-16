package com.xia.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;


public class ImportSelectTest implements ImportSelector {

    /**
     *   @importingClassMetadata：用来获取被@Import标注的类上面所有的注解信息
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        System.out.println(importingClassMetadata);

        return new String[] {"com.xia.bean.XiapeixinFds"};
    }

    //abcdefghijklmnopqrstuvwxyz
}
