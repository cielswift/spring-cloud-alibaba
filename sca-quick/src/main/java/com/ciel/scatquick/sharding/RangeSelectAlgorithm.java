package com.ciel.scatquick.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.stream.Collectors;

public class RangeSelectAlgorithm implements RangeShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(Collection<String> tagNames, RangeShardingValue<Long> value) {

        return tagNames.stream().filter(x -> {

            System.out.println("范围分片:"+x);
            System.out.println("范围分片值:"+value);

            Range<Long> valueRange = value.getValueRange();
            return x.endsWith("2");

        }).collect(Collectors.toSet());
    }

}
