package com.ciel.scaproducer3.config;

import com.ciel.scaapi.exception.AlertException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 扫包
 */
public class ExcelImportExportNameAdapter {


    public static final Logger log = LoggerFactory.getLogger(ExcelImportExportNameAdapter.class);
    private static final String RESOURCE_PATTERN = "**/%s/**/*.class";
    private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();


    public static final Set<Class<?>> scan(String pkn, Class<? extends Annotation>... annotationTags) throws AlertException {
        Set<Class<?>> resClazzSet = new HashSet<>();

        List<AnnotationTypeFilter> typeFilters = new LinkedList<>();
        if (ArrayUtils.isNotEmpty(annotationTags)) {
            for (Class<? extends Annotation> annotation : annotationTags) {
                typeFilters.add(new AnnotationTypeFilter(annotation, false));
            }
        }

        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                String.format(RESOURCE_PATTERN, ClassUtils.convertClassNameToResourcePath(pkn));
        try {
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    if (ifMatchesEntityType(reader, readerFactory, typeFilters)) {
                        Class<?> curClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                        resClazzSet.add(curClass);
                    }
                }
            }
        } catch (Exception e) {
            //log.error("扫描提取[{}]包路径下，标记了注解[{}]的类出现异常", pattern, StringUtils.join(typeFilters, ","));
            throw new AlertException("扫包错误");
        }

        return resClazzSet;
    }


    /**
     * 检查当前扫描到的类是否含有任何一个指定的注解标记
     *
     * @param reader
     * @param readerFactory
     * @return ture/false
     */
    private static boolean ifMatchesEntityType(MetadataReader reader, MetadataReaderFactory readerFactory, List<AnnotationTypeFilter> typeFilters) {
        if (!CollectionUtils.isEmpty(typeFilters)) {
            for (TypeFilter filter : typeFilters) {
                try {
                    if (filter.match(reader, readerFactory)) {
                        return true;
                    }
                } catch (IOException e) {
                    log.error("过滤匹配类型时出错 {}", e.getMessage());
                }
            }
        }
        return false;
    }
}