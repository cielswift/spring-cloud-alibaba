package com.ciel.springcloudalibabacommons.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class MybatisPlugin {

    /**
     * 注入数据中心和机器序列
     * @return
     */

    @Value("${clusters.datacenterId}")
    private Integer datacenterId;

    @Value("${clusters.machineId}")
    private Integer machineId;

    @Bean
    public SnowFlake snowFlake(){
        return new SnowFlake(datacenterId, machineId);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {  //自动分页
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDbType(DbType.MYSQL);
        return page;
    }

    @Bean //乐观锁插件 //仅支持 updateById(id) 与 update(entity, wrapper) 方法
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    @Bean //分布式id生成器
    public IdentifierGenerator identifierGenerator(){

        return new IdentifierGenerator() {
            @Override
            public Number nextId(Object entity) {

//                //可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成;
//                String bizKey = entity.getClass().getName();
//                //根据bizKey调用分布式ID生成
//                return bizKey.hashCode()+entity.hashCode() +
//                        System.currentTimeMillis() +
//                        new Random().nextInt(95276);

               return snowFlake().nextId();
            }
        };
    }

}
