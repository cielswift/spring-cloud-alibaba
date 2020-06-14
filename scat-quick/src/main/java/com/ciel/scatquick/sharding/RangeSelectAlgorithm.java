package com.ciel.scatquick.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.stream.Collectors;

/***
 * 范围分片算法类名称，用于BETWEEN，可选。该类需实现RangeShardingAlgorithm接口并提供无参数的构造器
 */
public class RangeSelectAlgorithm implements RangeShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames,
                                         RangeShardingValue<Long> shardingValue) {

        return availableTargetNames.stream().filter(x -> {

            System.out.println("范围分片"+availableTargetNames);
            System.out.println("范围分片"+shardingValue);

            Range<Long> valueRange = shardingValue.getValueRange();
            return x.endsWith("2");
        }).collect(Collectors.toSet());
    }

    //    @Override
//    public Collection<String> doSharding(final Collection<String> databaseNames, final RangeShardingValue<Integer> shardingValueRange) {
//        Set<String> result = new LinkedHashSet<>();
//        if (Range.closed(1, 5).encloses(shardingValueRange.getValueRange())) {
//            for (String each : databaseNames) {
//                if (each.endsWith("0")) {
//                    result.add(each);
//                }
//            }
//        } else if (Range.closed(6, 10).encloses(shardingValueRange.getValueRange())) {
//            for (String each : databaseNames) {
//                if (each.endsWith("1")) {
//                    result.add(each);
//                }
//            }
//        } else if (Range.closed(1, 10).encloses(shardingValueRange.getValueRange())) {
//            result.addAll(databaseNames);
//        } else {
//            throw new UnsupportedOperationException();
//        }
//        return result;
//    }
}
