package com.ciel.scatquick.beanload;

import lombok.extern.slf4j.Slf4j;
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
 * ApplicationEventPublisher：事件发布者，调用ApplicationEventMulticaster中的multicastEvent方法
 * 触发广播器持有的监听器集合执行onApplicationEvent方法，从而完成事件发布
 * <p>
 * 只要发布 AppEvn 事件就会触发
 */
@Slf4j
public class AppListener implements ApplicationListener<AppEvn> {

    @Override
    public void onApplicationEvent(AppEvn appEvn) {

        System.out.println(String.format("class 收到事件: 名称%s ,源 %s ", appEvn.getName(), appEvn.getSource().getClass().getName()));

        /**
         * 扫描包下的class
         */
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath("com.ciel") + "/**/*.class";

            Resource[] resources = resourcePatternResolver.getResources(pattern);

            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

            for (Resource resource : resources) {
                if (resource.isReadable()) {

                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    //扫描到的class
                    String className = reader.getClassMetadata().getClassName();
                    Class<?> clazz = Class.forName(className);

                    log.info("HIT THIS CLASS : {}",clazz.getName());

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("READ CLASS DEFAULT",e);
        }
    }
}
