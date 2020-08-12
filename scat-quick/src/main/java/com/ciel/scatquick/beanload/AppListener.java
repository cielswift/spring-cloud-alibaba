package com.ciel.scatquick.beanload;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
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
 *  ApplicationListener：事件监听器接口，定义通用方法onApplicationEvent： 订阅事件
 *  ApplicationEventMulticaster：事件广播器接口，用于事件监听器的注册和事件的广播
 *  ApplicationEventPublisher：事件发布者，调用ApplicationEventMulticaster中的multicastEvent方法
 *  触发广播器持有的监听器集合执行onApplicationEvent方法，从而完成事件发布
 *
 * 只要发布 AppEvn 事件就会触发
 *
 * 接下来罗列下spring主要的内置事件：
 *
 * ContextRefreshedEvent
 * ApplicationContext 被初始化或刷新时，该事件被发布。这也可以在 ConfigurableApplicationContext接口中使用
 * refresh() 方法来发生。此处的初始化是指：所有的Bean被成功装载，后处理Bean被检测并激活，
 * 所有Singleton Bean 被预实例化，ApplicationContext容器已就绪可用。
 *
 * ContextStartedEvent
 * 当使用 ConfigurableApplicationContext （ApplicationContext子接口）接口中的 start() 方法启动
 * ApplicationContext 时，该事件被发布。你可以调查你的数据库，或者你可以在接受到这个事件后重启任何停止的应用程序。
 *
 * ContextStoppedEvent
 * 当使用 ConfigurableApplicationContext 接口中的 stop() 停止 ApplicationContext 时，
 * 发布这个事件。你可以在接受到这个事件后做必要的清理的工作
 *
 * ContextClosedEvent
 * 当使用 ConfigurableApplicationContext接口中的 close()方法关闭 ApplicationContext 时，
 * 该事件被发布。一个已关闭的上下文到达生命周期末端；它不能被刷新或重启
 *
 * RequestHandledEvent
 * 这是一个 web-specific 事件，告诉所有 bean HTTP 请求已经被服务。
 * 只能应用于使用DispatcherServlet的Web应用。在使用Spring作为前端的MVC控制器时，
 * 当Spring处理用户请求结束后，系统会自动触发该事件
 *
 */
@Slf4j
public class AppListener implements ApplicationListener<AppEvn> , Ordered {

    /**
     * 监听顺序
     */
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void onApplicationEvent(AppEvn appEvn) {

        System.out.println("当前线程:"+Thread.currentThread().getName());
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
