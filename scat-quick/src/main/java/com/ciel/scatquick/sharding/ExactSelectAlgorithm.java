package com.ciel.scatquick.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/**
 * #精确分片算法类名称，用于=和IN。该类需实现PreciseShardingAlgorithm接口并提供无参数的构造器
 * <p>
 * /
 * // * Sharding 使用自定义分片算法
 * // * <p>
 * // * 精确分片算法
 * // * 对应PreciseShardingAlgorithm，用于处理使用单一键作为分片键的=与IN进行分片的场景。需要配合StandardShardingStrategy使用。
 * // * <p>
 * // * 范围分片算法
 * // * 对应RangeShardingAlgorithm，用于处理使用单一键作为分片键的BETWEEN AND进行分片的场景。需要配合StandardShardingStrategy使用。
 * // * <p>
 * // * 复合分片算法
 * // * 对应ComplexKeysShardingAlgorithm，用于处理使用多键作为分片键进行分片的场景，多分片键逻辑较复杂
 * // * ，需要应用开发者自行处理其中的复杂度。需要配合ComplexShardingStrategy使用。
 * // * <p>
 * / * Hint分片算法
 * / 对应HintShardingAlgorithm，用于处理使用Hint行分片的场景。需要配合HintShardingStrategy使用。
 */
public class ExactSelectAlgorithm implements PreciseShardingAlgorithm<Long> {

    /**
     * availableTargetNames可用的数据源或表的名称
     *   shardingValue分片值
     *  返回数据源或表名称的分片结果
     */

    @Override
    public String doSharding(Collection<String> availableTargetNames,
                             PreciseShardingValue<Long> shardingValue) {

        for (String each : availableTargetNames) {

            System.out.println("精确分片" + availableTargetNames);  //[ds0, ds1]
            System.out.println("精确分片" + shardingValue); //columnName ID ; logicTableName sca_girls;
            // value 590957418782732


            if (each.endsWith(String.valueOf(shardingValue.getValue() % 2))) {
                return each;
            }
        }

        return availableTargetNames.stream().findFirst().get();
    }
}
