package com.xia.config;

import com.xia.bean.XiapeixinFms;
import com.xia.bean.XiapeixinFns;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;

public class ImportDefaultB implements DeferredImportSelector, Ordered {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{ XiapeixinFns.class.getName()};
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
