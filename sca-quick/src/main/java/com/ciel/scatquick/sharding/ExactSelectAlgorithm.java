package com.ciel.scatquick.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * #精确分片算法类名称，用于=和IN。该类需实现PreciseShardingAlgorithm接口并提供无参数的构造器
 *
 * Sharding 使用自定义分片算法
 * 精确分片算法
 * 对应PreciseShardingAlgorithm，用于处理使用单一键作为分片键的=与IN进行分片的场景。需要配合StandardShardingStrategy使用。
 *
 * 范围分片算法
 * 对应RangeShardingAlgorithm，用于处理使用单一键作为分片键的BETWEEN AND,>、<、>=、<=进行分片的场景。
 *      需要配合StandardShardingStrategy使用,如果不配置RangeShardingAlgorithm，SQL中的BETWEEN AND将按照全库路由处理
 *
 * 复合分片算法
 * 对应ComplexKeysShardingAlgorithm，用于处理使用多键作为分片键进行分片的场景，多分片键逻辑较复杂
 * 需要应用开发者自行处理其中的复杂度。需要配合ComplexShardingStrategy使用。
 *
 * Hint分片算法
 * 对应HintShardingAlgorithm，用于处理使用Hint行分片的场景。需要配合HintShardingStrategy使用。
 */
public class ExactSelectAlgorithm implements PreciseShardingAlgorithm<Long> {

    /**
     * availableTargetNames可用的数据源或表的名称
     *   shardingValue分片值
     *  返回数据源或表名称的分片结果
     */

    @Override
    public String doSharding(Collection<String> targetNames, PreciseShardingValue<Long> value) {

        for (String name : targetNames) {
            System.out.println("可用分片:" + name);
            System.out.println("当前ID值" + value);

            if (name.endsWith(String.valueOf(value.getValue() % targetNames.size()))) {
                return name;
            }
        }
        return targetNames.stream().findFirst().get();
    }
}
