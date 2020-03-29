package com.ciel.scaproducer3.controller;

import com.google.common.collect.*;

import java.util.Collection;

public class Guat  {
    public static void main(String[] args) {

        Multiset<String> multiset = HashMultiset.create();

        multiset.add("夏培鑫");
        multiset.add("夏培鑫");
        multiset.add("夏培鑫C");

        int count = multiset.count("夏培鑫");
        System.out.println(count);
////////////////////////////////////////////////////////////
        Multimap<String,String> multimap = HashMultimap.create();

        multimap.put("a","a1");
        multimap.put("a","a2");

        Collection<String> strings = multimap.get("a");
   //////////////////////////////////////////////////////////////
        BiMap<String,String> biMap = HashBiMap.create(); //BiMap 可以用来实现键值对的双向映射需求，

        //这里需要注意，BiMap#put方法不能加入重复元素， 若加入，将会抛错。如果若特定值一定要替换，可以使用 BiMap#forcePut代替

        biMap.put("a","a1");

        String a = biMap.get("a");

        String a1 = biMap.inverse().get("a1");

        System.out.println(a);
        System.out.println(a1);

        ///////////////////////////////////
        //交集
        Sets.SetView<Object> intersection = Sets.intersection(Sets.newHashSet(), Sets.newHashSet());
        //并集
        Sets.SetView<Object> union = Sets.union(Sets.newHashSet(), Sets.newHashSet());
        //差集
        Sets.SetView<Object> difference = Sets.difference(Sets.newHashSet(), Sets.newHashSet());
    }
}
