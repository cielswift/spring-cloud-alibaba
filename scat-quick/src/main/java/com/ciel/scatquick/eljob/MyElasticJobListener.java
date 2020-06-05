package com.ciel.scatquick.eljob;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class MyElasticJobListener implements ElasticJobListener {

    /**
     *  长日期格式
     */
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private long beginTime = 0;
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        beginTime = System.currentTimeMillis();
 
        log.info("===>{} JOB BEGIN TIME: {} <===",shardingContexts.getJobName(), beginTime);
    }
 
    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        long endTime = System.currentTimeMillis();
        log.info("===>{} JOB END TIME: {},TOTAL CAST: {} <===",shardingContexts.getJobName(),
                endTime, endTime - beginTime);
    }
 
    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @param format
     * @return
     */
    public static String convert2String(long time, String format) {

        if (time > 0L) {
            if (StringUtils.isBlank(format)){
                format = TIME_FORMAT;
            }

            SimpleDateFormat sf = new SimpleDateFormat(format);
            Date date = new Date(time);
 
            return sf.format(date);
        }
        return "";
    }
}