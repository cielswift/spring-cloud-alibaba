package com.ciel.scatquick.sharding;

import org.apache.shardingsphere.spi.masterslave.MasterSlaveLoadBalanceAlgorithm;

import java.util.List;
import java.util.Properties;

/**
 * #从库负载均衡算法类名称。该类需实现MasterSlaveLoadBalanceAlgorithm接口且提供无参数构造器
 *
 * Apache ShardingSphere 内置的从库负载均衡算法实现类包括：
 *
 * 轮询算法
 * 类名称：org.apache.shardingsphere.masterslave.algorithm.RoundRobinMasterSlaveLoadBalanceAlgorithm
 *
 * 可配置属性：无
 *
 * 随机访问算法
 * 类名称：org.apache.shardingsphere.masterslave.algorithm.RandomMasterSlaveLoadBalanceAlgorithm
 *
 * 可配置属性：无
 *
 */
public class MasterSlaveLoad implements MasterSlaveLoadBalanceAlgorithm {

    /**
     *   * @param名称主从逻辑数据源名称
     *       * @param masterDataSourceName主数据源的名称
     *       * @param slaveDataSourceNames从属数据源的名称
     *
     *
     */
    @Override
    public String getDataSource(String name, String masterDataSourceName, List<String> slaveDataSourceNames) {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
