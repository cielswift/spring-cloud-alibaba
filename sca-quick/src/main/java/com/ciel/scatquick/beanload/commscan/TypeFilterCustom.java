package com.ciel.scatquick.beanload.commscan;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * 配合@ComponentScan 扫描指定规则
 */
public class TypeFilterCustom implements TypeFilter {

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //metadataReader 读取到的正在扫描类的信息;
        //metadataReaderFactory 获取其他任何类的信息;
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata(); //获取注解
        ClassMetadata classMetadata = metadataReader.getClassMetadata(); //获取类信息
        Resource resource = metadataReader.getResource(); //获取资源信息,类路径
        return true;
    }

}