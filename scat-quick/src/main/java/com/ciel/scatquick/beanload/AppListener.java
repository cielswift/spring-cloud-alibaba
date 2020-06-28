package com.ciel.scatquick.beanload;

import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ApplicationListener：事件监听器接口，定义通用方法onApplicationEvent：
 * ApplicationEventMulticaster：事件广播器接口，用于事件监听器的注册和事件的广播
 * ApplicationEventPublisher：事件发布者，调用ApplicationEventMulticaster中的multicastEvent方法触发广播器持有的监听器集合执行onApplicationEvent方法，从而完成事件发布
 */
public class AppListener implements ApplicationListener<AppEvn> {

    @Override
    public void onApplicationEvent(AppEvn appEvn) {
        System.out.println(appEvn.getName());

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        final String RESOURCE_PATTERN = "/**/*.class";
        // 扫描的包名
        final String BASE_PACKAGE = "com.xxx";
        Map<String,Class<?>> classCache = new HashMap<>();

        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE)
                    + RESOURCE_PATTERN;

            Resource[] resources = resourcePatternResolver.getResources(pattern);

            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    //扫描到的class
                    String className = reader.getClassMetadata().getClassName();
                    Class<?> clazz = Class.forName(className);

                    System.out.println(String.format(">>>%s<<<",className));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("读取class失败");
        }
    }
}
