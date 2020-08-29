package com.ciel.scatquick.sharding;

import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.Collection;

public class HistAlgorithm implements HintShardingAlgorithm {


    @Override
    public Collection<String> doSharding(Collection collection, HintShardingValue hintShardingValue) {
        return null;
    }
}
