package com.ciel.scatquick.eljob;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

@Component
public class ElasticSchedulerAspect implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;
    @Autowired
    private ElasticJobHandler elasticJobHandler;
    @Override
    public void afterPropertiesSet() throws Exception {
        registrJob(applicationContext);
    }
 
    /**
     * 解析context信息，开始注册
     * @param applicationContext
     */
    private void registrJob(ApplicationContext applicationContext) {
        String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(ElasticScheduler.class);
        for (String beanName : beanNamesForAnnotation) {
            Class<?> handlerType = applicationContext.getType(beanName);
            Object bean = applicationContext.getBean(beanName);
            ElasticScheduler annotation = AnnotationUtils.findAnnotation(handlerType, ElasticScheduler.class);
            addJobToContext(annotation,bean);
        }
    }
 
    /**
     * 将任务添加到容器中
     * @param elasticScheduler
     * @param bean
     */
    private void addJobToContext(ElasticScheduler elasticScheduler, Object bean) {
        String cron = elasticScheduler.cron();
        String name = elasticScheduler.name();
        String description = elasticScheduler.description();
        String shardingItemParameters = elasticScheduler.shardingItemParameters();
        Integer shardingTotalCount = elasticScheduler.shardingTotalCount();
        String jobParamters = elasticScheduler.jobParameters();
        try {
            elasticJobHandler.addJob((SimpleJob) bean,cron,shardingTotalCount,shardingItemParameters,jobParamters);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}