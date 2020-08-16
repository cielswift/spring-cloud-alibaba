//package com.ciel.scatquick.eljob;
//
//import com.dangdang.ddframe.job.api.ShardingContext;
//import com.dangdang.ddframe.job.api.simple.SimpleJob;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Component
//@ElasticScheduler(cron = "0/50 * * * * ?",shardingTotalCount = 4,name = "测试注解321",
//        shardingItemParameters = "0=0,1=0,2=1,3=1",jobParameters = "参数123")
//@Slf4j
//public class MySimpleJob implements SimpleJob {
//
//    @Override
//    public void execute(ShardingContext shardingContext) {
//        log.info(String.format("Thread ID: %s, 作业分片总数: %s, " +
//                        "当前分片项: %s.当前参数: %s," +
//                        "作业名称: %s.作业自定义参数: %s"
//                ,
//                Thread.currentThread().getId(),
//                shardingContext.getShardingTotalCount(),
//                shardingContext.getShardingItem(),
//                shardingContext.getShardingParameter(),
//                shardingContext.getJobName(),
//                shardingContext.getJobParameter()
//        ));
//
//    }
//}
