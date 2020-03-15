//package com.ciel.scaproducer3.controller.config;
//
//import org.apache.shardingsphere.spi.database.DatabaseType;
//import org.apache.shardingsphere.transaction.core.ResourceDataSource;
//import org.apache.shardingsphere.transaction.core.TransactionType;
//import org.apache.shardingsphere.transaction.spi.ShardingTransactionManager;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.Collection;
//
///**
// * BASE事务
// * BASE目前没有打包到Sharding-Proxy中，使用时需要将实现了ShardingTransactionManagerSPI的jar拷贝至conf/lib目录，
// * 然后切换事务类型为BASE。
// *
// *
// * SCTL (Sharding-Proxy control language)
// * SCTL为Sharding-Proxy特有的控制语句，可以在运行时修改和查询Sharding-Proxy的状态，目前支持的语法为：
// *
// * 语句	说明
// * sctl:set transaction_type=XX	修改当前TCP连接的事务类型, 支持LOCAL，XA，BASE。例：sctl:set transaction_type=XA
// * sctl:show transaction_type	查询当前TCP连接的事务类型
// * sctl:show cached_connections	查询当前TCP连接中缓存的物理数据库连接个数
// * sctl:explain SQL语句	查看逻辑SQL的执行计划，例：sctl:explain select * from t_order;
// * sctl:hint set MASTER_ONLY=true	针对当前TCP连接，是否将数据库操作强制路由到主库
// * sctl:hint set DatabaseShardingValue=yy	针对当前TCP连接，设置hint仅对数据库分片有效，并添加分片值，yy：数据库分片值
// * sctl:hint addDatabaseShardingValue xx=yy	针对当前TCP连接，为表xx添加分片值yy，xx：逻辑表名称，yy：数据库分片值
// * sctl:hint addTableShardingValue xx=yy	针对当前TCP连接，为表xx添加分片值yy，xx：逻辑表名称，yy：表分片值
// * sctl:hint clear	针对当前TCP连接，清除hint所有设置
// * sctl:hint show status	针对当前TCP连接，查询hint状态，master_only:true/false，sharding_type:databases_only/databases_tables
// * sctl:hint show table status	针对当前TCP连接，查询逻辑表的hint分片值
// *
// * Sharding-Proxy 默认不支持hint，如需支持，请在conf/server.yaml中，将props的属性proxy.hint.enabled设置为true。
// * 在Sharding-Proxy中，HintShardingAlgorithm的泛型只能是String类型。
// *
// * harding-Proxy默认使用3307端口，可以通过启动脚本追加参数作为启动端口号。如: bin/start.sh 3308
// * Sharding-Proxy使用conf/server.yaml配置注册中心、认证信息以及公用属性。
// * Sharding-Proxy支持多逻辑数据源，每个以config-前缀命名的yaml配置文件，即为一个逻辑数据源。
// */
//public class BaseTransactionSharding implements ShardingTransactionManager {
//
//    @Override
//    public void init(DatabaseType databaseType, Collection<ResourceDataSource> collection) {
//
//    }
//
//    @Override
//    public TransactionType getTransactionType() {
//
//
//        return null;
//    }
//
//    @Override
//    public boolean isInTransaction() {
//
//
//        return false;
//    }
//
//    @Override
//    public Connection getConnection(String s) throws SQLException {
//        return null;
//    }
//
//    @Override
//    public void begin() {
//
//
//    }
//
//    @Override
//    public void commit() {
//
//
//    }
//
//    @Override
//    public void rollback() {
//
//
//    }
//
//    @Override
//    public void close() throws Exception {
//
//    }
//}
