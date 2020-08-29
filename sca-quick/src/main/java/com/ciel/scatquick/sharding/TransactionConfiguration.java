package com.ciel.scatquick.sharding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * shardingsphereJDBC SpringBoot  XA分布式事务
 *
 * Apache ShardingSphere 默认的 XA 事务管理器为 Atomikos。
 *
 * 数据恢复
 * 在项目的 logs 目录中会生成xa_tx.log, 这是 XA 崩溃恢复时所需的日志，请勿删除。
 *
 * 修改配置
 * 可以通过在项目的 classpath 中添加 jta.properties 来定制化 Atomikos 配置项。
 *
 */

@Configuration
public class TransactionConfiguration {

    @Bean
    public PlatformTransactionManager txManager(@Autowired final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(@Autowired final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}