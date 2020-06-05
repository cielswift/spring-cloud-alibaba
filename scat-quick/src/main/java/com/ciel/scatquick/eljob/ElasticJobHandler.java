package com.ciel.scatquick.eljob;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ElasticJobHandler {
    @Autowired
    private ZookeeperRegistryCenter regCenter;
    /**
     * 监听job启动和完毕时间
     */
    @Resource
    private ElasticJobListener elasticJobListener;
 
    /**
     * 事件追踪
     */
    @Resource
    private JobEventConfiguration jobEventConfiguration;
 
 
    /**
     * @Description 任務配置類
     */
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters,
                                                         final String jobParameters) {
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
                JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
                        .shardingItemParameters(shardingItemParameters).jobParameter(jobParameters).build()
                , jobClass.getCanonicalName())
        ).overwrite(true).build();
    }
 
    /**
     * 抽象添加job方法
     * @param simpleJob
     * @param cron
     * @param shardingTotalCount
     * @param shardingItemParameters
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void addJob(final SimpleJob simpleJob,
                       final String cron,
                       final Integer shardingTotalCount,
                       final String shardingItemParameters,
                       final String jobParameters)
            throws IllegalAccessException, InstantiationException {
 
        LiteJobConfiguration jobConfig =
                getLiteJobConfiguration(simpleJob.getClass(), cron, shardingTotalCount, shardingItemParameters,jobParameters);
 
        new SpringJobScheduler(simpleJob, regCenter, jobConfig, jobEventConfiguration, elasticJobListener).init();
    }
}
