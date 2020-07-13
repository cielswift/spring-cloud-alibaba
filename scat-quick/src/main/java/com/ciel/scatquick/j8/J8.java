package com.ciel.scatquick.j8;

import com.ciel.scaentity.entity.ScaGirls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Deprecated
public class J8 {

    public static void main(String[] args) throws IOException {

        long ccs = Files.lines(Paths.get("C:/ciel/zipkin.txt")).count(); //获取行数

        Logger logger = LoggerFactory.getLogger(J8.class); //日志

        List<String> ciel = new ArrayList<>();
        ciel.add("ciel");ciel.add("xia");ciel.add("swift");ciel.add("join");

        Map<Integer, String> collect8 = ciel.stream()
                .filter(t -> t.length() == 3) //过滤
                .map(String::hashCode)  //转换 并且可以和原集合的泛型不一致; 这里就 string 变成了 Integer
                .collect(Collectors.toMap(Object::hashCode, String::valueOf)); //收集到map 中
        // .collect(Collectors.toList()); //收集到集合中;
        //.toArray(); //收集到数组中
        //.collect(Collectors.toSet()); //收集到集合中

        Map<String, String> map = new HashMap<>();
        map.put("name", "xiapeixin");map.put("age", "22");
        map.entrySet().stream().filter(t -> true).forEach(J8::show); //map 循环

        Arrays.stream(new String[]{"a", "b", "c"}).forEach(System.out::println);//数组的流

        long count = ciel.stream()
                .limit(3)//取前几个
                .skip(2) //跳过前几个
                .count(); //数量

        ciel.stream().sorted((t, i) -> t.length() - i.length())
                .forEach(System.out::println); //sorted 排序

        ciel.stream().distinct().forEach(System.out::println); //去重

        List<ScaGirls> girls = new ArrayList<>();
        ScaGirls g1 = new ScaGirls();g1.setName("lwx");g1.setId(1L);
        girls.add(g1);
        ScaGirls g2 = new ScaGirls();g1.setName("aaa");g1.setId(1L);
        girls.add(g2);

        boolean b = ciel.stream().allMatch(t -> t.length() > 5); //每个元素的长度都大于5 匹配通过
        boolean b1 = ciel.stream().anyMatch(t -> t.length() > 5); //只要有一个通过就返回true;
        boolean b2 = ciel.stream().noneMatch(t -> t.length() > 5); //全部不通过才返回true;

        String s = ciel.stream().findFirst().orElseGet(() -> "null"); //找到第一个
        String s1 = ciel.stream().findAny().orElseGet(() -> "null");  //返回任意一个 不一定是第一个

        String s2 = ciel.stream().max((t, i) -> t.length() - i.length()).orElseGet(() -> "null");//获取最大值,也就是排序后的最后一个值
        String s3 = ciel.stream().min((t, i) -> t.length() - i.length()).orElseGet(() -> "null"); //最小值

        //把所有结果相加;
        String aNull = ciel.stream().reduce((x, y) -> x + y).orElse("null");
        //所有元素+前面的默认值,其实是对每一个元素都做了运算
        String reduce = ciel.stream().reduce("==", (x, y) -> x + y);

        //.getMax() .getcount() .getsum() getMin
        Stream.of(1, 8, 6).mapToInt(t -> t).summaryStatistics().getAverage();

        //java 11语法
        //List<String> xia = List.of("xia", "pei"); //创建只读的集合;
        //Map<String, String> x1 = Map.of("x", "xia", "p", "pei");
        //List<String> copyOf = List.copyOf(ciel); //复制只读集合

        Double collect = ciel.stream().collect(Collectors.averagingInt(t -> t.length())); //平均值
        Integer collect1 = ciel.stream().collect(Collectors.summingInt(t -> t.length())); //相加
        Long collect2 = ciel.stream().collect(Collectors.counting()); //数量


        Map<Integer, List<String>> mapss = ciel.stream().collect(Collectors.groupingBy(t -> t.length()));
        //分组 ,会以字符长度进行分组, 字符长度是key, value是被分在一组的对象

        Map<Integer, Map<Integer, List<String>>> collect3 = ciel.stream().collect(Collectors.groupingBy(t -> t.length(), Collectors.groupingBy(x -> x.hashCode())));
        //多级分组,先根据字符长度,然后根据hash值

        Map<Boolean, List<String>> collect4 = ciel.stream().collect(Collectors.partitioningBy(t -> t.length() == 4));
        //分区,和分组一样;只有两个true 和false

        String collect5 = ciel.stream().collect(Collectors.joining("-"));
        //按下划线拼接

        String collect6 = ciel.stream().collect(Collectors.joining("-", "^", "$"));
        //添加前缀和后缀

        /** ------------------------------------------------------------------------*/

        Stream.concat(ciel.stream(), ciel.stream()).forEach(System.out::println); //组合流

        ciel.stream().parallel(); //获取并发流,注意,线程不安全;

        ciel.parallelStream().forEach(t -> System.err.println(Thread.currentThread().getName() + ":" + t));
        //并行流 ,多线程执行;


        /** ------------------------------------------------------------------------*/
        Optional<String> nu = Optional.empty();
        Optional<String> sister = Optional.of("白丝妹妹");
        boolean present = nu.isPresent(); //是否存在值 //不为空 返回true

        String s4 = nu.orElse("黑丝熟女"); //有值取,没有取else
        System.out.println(s4);

        //java 9 语法 ,  //有值做什么 ,没有值做什么
        //nu.ifPresentOrElse(t -> System.out.println(t), () -> System.err.println("没有值"));

        Optional<Integer> integer1 = sister.map(String::hashCode); //转换

        //java 9 语法 ,
       // ciel.stream().takeWhile(t -> t.length() == 4).forEach(System.out::println);
        //获取满足条件的元素, 如果遇到不满足条件的元素,就会立即停止,不管后面的满不满足;

        //ciel.stream().dropWhile(t -> t.length() == 4).forEach(System.out::println);
        //删除满足条件的元素,如果遇到不满足条件的元素,就会立即停止,不管后面的满不满足;

        //long count1 = Stream.ofNullable(null).count();//空stream
       // System.err.println("uu" + count1);


        //数组扁平化
        List<List<String>> lsn = new ArrayList<>();
        List<String> ls1 = new ArrayList<>();ls1.add("a");ls1.add("b");
        List<String> ls2 = new ArrayList<>();ls1.add("c");ls1.add("d");
        lsn.add(ls1); lsn.add(ls2);

        Object collect7 = lsn.stream().flatMap(t -> t.stream()).collect(Collectors.toList());
        System.out.println(collect7);

        Stream<UUID> generate = Stream.generate(UUID::randomUUID); //生成stream流
        generate.limit(50).forEach(t -> logger.info(t.toString()));

    }

    public static void show(Map.Entry me) {
        System.out.println(me.getKey().toString().concat(me.getValue().toString()));
    }
}