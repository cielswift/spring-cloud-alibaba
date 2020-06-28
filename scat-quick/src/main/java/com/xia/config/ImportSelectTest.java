package com.xia.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;


public class ImportSelectTest implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        System.out.println(importingClassMetadata);

        return new String[] {"com.xia.bean.XiapeixinFds"};
    }

    //abcdefghijklmnopqrstuvwxyz
}
