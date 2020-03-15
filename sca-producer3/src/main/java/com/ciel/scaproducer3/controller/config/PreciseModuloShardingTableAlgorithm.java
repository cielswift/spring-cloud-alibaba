//package com.ciel.scaproducer3.controller.config;
//
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
//
//import java.util.Collection;
//import java.util.HashSet;
//
//public final class PreciseModuloShardingTableAlgorithm implements PreciseShardingAlgorithm<Long> {
//
//    @Override
//    public String doSharding(final Collection<String> tableNames, final PreciseShardingValue<Long> shardingValue) {
//        for (String each : tableNames) {
//            if (each.endsWith(shardingValue.getValue() % 2 + "")) {
//                return each;
//            }
//        }
//        throw new UnsupportedOperationException();
//    }
//}
