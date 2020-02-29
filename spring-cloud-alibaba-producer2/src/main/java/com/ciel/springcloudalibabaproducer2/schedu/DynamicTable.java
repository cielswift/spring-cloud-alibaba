package com.ciel.springcloudalibabaproducer2.schedu;

import com.ciel.springcloudalibabacommons.mapper.TableAT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 动态建表
 */
@Component
public class DynamicTable {

    private static final Logger logger = LoggerFactory.getLogger(DynamicTable.class);

    @Autowired
    protected TableAT tableAT;

    @Scheduled(cron = "1 1 23 28 * ?")
    //@Scheduled(cron = "1 * * * * ?")
    public void createDynamicTable() throws SQLException {

        String tName = "sca_order_".concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM")));

        boolean ise = tableAT.isExitsTable(tName);

        if(ise){
            logger.error(tName.concat(":表已经提前存在;"));
        }else{
            tableAT.createTableOrder(tName);
            logger.error(tName.concat(":表创建成功"));
        }

    }
}