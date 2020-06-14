//package com.ciel.scatquick.eljob;
//
//import com.dangdang.ddframe.job.event.JobEventConfiguration;
//import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
//import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
//import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
//import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class JobRegistryCenterConfig {
//
//    @Bean(initMethod = "init")
//    public ZookeeperRegistryCenter regCenter() {
//        ZookeeperConfiguration configuration =
//                new ZookeeperConfiguration("127.0.0.1:21890", "el-job");
//        configuration.setBaseSleepTimeMilliseconds(1000);
//        return new ZookeeperRegistryCenter(configuration);
//    }
//
//    @Autowired
//    protected DataSource dataSource;
//
//    /**
//     * 配置任务监听器
//     * @return
//     */
//    @Bean
//    public ElasticJobListener elasticJobListener() {
//        return new MyElasticJobListener();
//    }
//
//    /**
//     * 将作业运行的痕迹进行持久化到DB
//     */
//    @Bean
//    public JobEventConfiguration jobEventConfiguration(){
//        return new JobEventRdbConfiguration(dataSource);
//    }
//
//}
