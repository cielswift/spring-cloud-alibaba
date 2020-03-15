//package com.ciel.scaproducer3.controller.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shardingsphere.api.sharding.ShardingValue;
//import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
///**
// * Sharding 使用自定义分片算法
// * <p>
// * 精确分片算法
// * 对应PreciseShardingAlgorithm，用于处理使用单一键作为分片键的=与IN进行分片的场景。需要配合StandardShardingStrategy使用。
// * <p>
// * 范围分片算法
// * 对应RangeShardingAlgorithm，用于处理使用单一键作为分片键的BETWEEN AND进行分片的场景。需要配合StandardShardingStrategy使用。
// * <p>
// * 复合分片算法
// * 对应ComplexKeysShardingAlgorithm，用于处理使用多键作为分片键进行分片的场景，多分片键逻辑较复杂
// * ，需要应用开发者自行处理其中的复杂度。需要配合ComplexShardingStrategy使用。
// * <p>
// * Hint分片算法
// * 对应HintShardingAlgorithm，用于处理使用Hint行分片的场景。需要配合HintShardingStrategy使用。
// */
//@Slf4j
//public class PreciseModuloShardingDatabaseAlgorithm implements PreciseShardingAlgorithm<Long> {
//
//    /**
//     * * @param availableTargetNames可用的数据源或表的名称
//     *       @param shardingValue分片值
//     *     @返回数据源或表名称的分片结果
//     */
//
//    @Override
//    public String doSharding(Collection<String> databaseNames,
//                             PreciseShardingValue<Long> shardingValue) {
//
//        for (String each : databaseNames) {
//            if (each.endsWith(String.valueOf(shardingValue.getValue() % 2))) {
//                return each;
//            }
//        }
//        return null;
//    }
//
//
//
//}
