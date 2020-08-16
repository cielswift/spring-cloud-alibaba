package com.xia.config;

import com.xia.bean.XiapeixinFms;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 当@Import中有多个DeferredImportSelector接口的实现类时候，可以指定他们的顺序，指定顺序常见2种方式
 * 实现Ordered接口的方式
 * 实现Order注解的方式
 */
public class ImportDefaultA implements DeferredImportSelector , Ordered {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{ XiapeixinFms.class.getName()};
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
