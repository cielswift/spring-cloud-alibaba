package com.ciel.scatquick.guava;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Guava {

    public static void main(String[] args) {

        // 普通Collection的创建
        List<String> list = Lists.newArrayList();
        Set<String> set = Sets.newHashSet();
        Map<String, String> map = Maps.newHashMap();

        // 不变Collection的创建
        ImmutableList<String> iList = ImmutableList.of("a", "b", "c");
        ImmutableSet<String> iSet = ImmutableSet.of("e1", "e2");
        ImmutableMap<String, String> iMap = ImmutableMap.of("k1", "v1", "k2", "v2");

        //当我们需要一个map中包含key为String类型，value为List类型的时候
        Multiset<String> multiset = HashMultiset.create();

        multiset.add("夏培鑫");
        multiset.add("夏培鑫");
        multiset.add("夏培鑫C");

        int count = multiset.count("夏培鑫");
        System.out.println(count);

        //当我们需要一个map中包含key为String类型，value为List类型的时候
        Multimap<String,Integer> mumap = ArrayListMultimap.create();
        mumap.put("aa", 1);
        mumap.put("aa", 2);
        System.out.println(mumap.get("aa"));  //[1, 2]

////////////////////////////////////////////////////////////
        Multimap<String,String> multimap = HashMultimap.create();

        multimap.put("a","a1");
        multimap.put("a","a2");

        Collection<String> strings = multimap.get("a");

        //////////////////////////////////////////////////////////////
        BiMap<String,String> biMap = HashBiMap.create(); //BiMap 可以用来实现键值对的双向映射需求， 键与值都不能重复

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

        //将集合转换为特定规则的字符串
        String join = Joiner.on("-").join(iList);
        System.out.println(join);

        //将String转换为特定的集合
        List<String> list1 = Splitter.on("-").splitToList(join);

        //uava还可以使用 omitEmptyStrings().trimResults() 去除空串与空格
        String str = "1-2-3-4-  5-  6   ";
        List<String> listzz = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(str);
        System.out.println(listzz);

        //将String转换为map
        String strz = "xiaoming=11,xiaohong=23";
        Map<String,String> mapz = Splitter.on(",").withKeyValueSeparator("=").split(strz);

        //guava还支持多个字符切割，或者特定的正则分隔
        String input = "aa.dd,,ff,,.";
        List<String> result = Splitter.onPattern("[.|,]").omitEmptyStrings().splitToList(input);
        System.out.println(result);

        // 判断匹配结果
        boolean ramgge = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z')).matches('K'); //true
        System.out.println(ramgge);


    }
}
