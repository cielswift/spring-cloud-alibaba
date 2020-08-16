//package com.ciel.scatquick.eljob;
//
//import com.dangdang.ddframe.job.executor.ShardingContexts;
//import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Slf4j
//public class MyElasticJobListener implements ElasticJobListener {
//
//    /**
//     *  长日期格式
//     */
//    private long beginTime = 0;
//
//    @Override
//    public void beforeJobExecuted(ShardingContexts shardingContexts) {
//        beginTime = System.currentTimeMillis();
//        log.info("任务开始执行 任务名称:%s 当前时间:%s",shardingContexts.getJobName(), beginTime);
//    }
//
//    @Override
//    public void afterJobExecuted(ShardingContexts shardingContexts) {
//        log.info("任务结束执行 任务名称:%s 消耗时间:%s",shardingContexts.getJobName(), System.currentTimeMillis() - beginTime);
//    }
//
//}