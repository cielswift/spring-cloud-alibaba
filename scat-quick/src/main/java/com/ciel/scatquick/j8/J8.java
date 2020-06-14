package com.ciel.scatquick.j8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Deprecated
public class J8 {

    public static void main(String[] args) throws IOException {

        long ccs = Files.lines(Paths.get(J8.class.getResource("./J8.class").toString()),
                Charset.forName("utf-8")).count(); //获取行数

        Logger logger = LoggerFactory.getLogger(J8.class);

        List<String> ciel = new ArrayList<>();
        ciel.add("ciel");
        ciel.add("xia");
        ciel.add("swift");
        ciel.add("join");

        List<String> filter = ciel.stream().filter(t -> t.length() == 3).collect(Collectors.toList()); //过滤

        System.out.println(filter);

        Map<String, String> map = new HashMap<>();
        map.put("name", "xiapeixin");
        map.put("age", "22");
        map.entrySet().stream().forEach(J8::show);

        Arrays.stream(new String[]{"a", "b", "c"}).forEach(t -> System.out.println(t));//数组的流

        long count = ciel.stream().count(); //数量
        long count2 = ciel.stream().limit(2).count(); //取前几个
        ciel.stream().skip(2); //跳过前几个

        ciel.stream().map(t -> t.hashCode()).collect(Collectors.toList());
        //返回修改的结果,并且修改后的可以和原集合的泛型不一致; 这里就 string 变成了 Integer


        ciel.stream().sorted((t, i) -> t.length() - i.length()).forEach(System.out::println); //sorted 排序
        ciel.stream().distinct().forEach(System.out::println); //去重
        boolean b = ciel.stream().allMatch(t -> t.length() > 5); //每个元素的长度都大于5 匹配通过
        boolean b1 = ciel.stream().anyMatch(t -> t.length() > 5); //只要有一个通过就返回true;
        boolean b2 = ciel.stream().noneMatch(t -> t.length() > 5); //全部不通过才返回true;

        String s = ciel.stream().findFirst().get(); //找到第一个
        String s1 = ciel.stream().findAny().get();


        String s2 = ciel.stream().max((t, i) -> t.length() - i.length()).get();//获取最大值,也就是排序后的最后一个值
        String s3 = ciel.stream().min((t, i) -> t.length() - i.length()).get(); //最小值

        String reduce = ciel.stream().reduce("//", (x, y) -> x + y); //把所有结果相加,所有元素+前面的默认值
        //其实是对每一个元素都做了运算

        Stream.of(1, 8, 6).mapToInt(t -> t).summaryStatistics().getAverage(); //平均值
        //           .getMax() .getcount() .getsum()


        ciel.stream().toArray(); //收集到数组中
        ciel.stream().collect(Collectors.toList()); //收集到集合中
        ciel.stream().collect(Collectors.toSet()); //set

        //List<String> xia = List.of("xia", "pei"); //创建只读的集合;
        //Map<String, String> x1 = Map.of("x", "xia", "p", "pei");
        //List<String> copyOf = List.copyOf(ciel); //复制只读集合


        Double collect = ciel.stream().collect(Collectors.averagingInt(t -> t.length())); //平均值
        Integer collect1 = ciel.stream().collect(Collectors.summingInt(t -> t.length())); //相加
        Long collect2 = ciel.stream().collect(Collectors.counting()); //数量

        System.err.println(collect + "-" + collect1 + "-" + collect2);

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

        System.out.println(collect6);

        System.out.println(collect);
        /** ------------------------------------------------------------------------*/

        Stream.concat(ciel.stream(), ciel.stream()).forEach(t -> System.out.println(t)); //组合流

        ciel.stream().parallel(); //获取并发流,注意,线程不安全;

        ciel.parallelStream().forEach(t -> System.err.println(Thread.currentThread().getName() + ":" + t));
        //并行流 ,多线程执行;


        List<Long> ls = new ArrayList<>();
        for (long i = 0; i <= 100; i++) {
            ls.add(i);
        }
        long l = System.currentTimeMillis();
        Long integer = ls.parallelStream().reduce((t, x) -> t + x).get();
        System.err.println(integer);
        System.err.println(System.currentTimeMillis() - l);

        //5000000050000000

        /** ------------------------------------------------------------------------*/
        Optional<String> optionalO = Optional.empty();
        Optional<String> optionalS = Optional.of("白丝妹妹");
        boolean present = optionalO.isPresent();
        System.out.println(present + "--:--" + optionalS.isPresent()); //不为空 返回true

        String s4 = optionalO.orElse("黑丝熟女"); //有值取,没有取else
        System.out.println(s4);

      //  optionalS.ifPresentOrElse(t -> System.out.println(t), () -> System.err.println("没有值"));
        //有值做什么 ,没有值做什么
        Optional<Integer> integer1 = optionalS.map(t -> t.hashCode()); //转换


       // ciel.stream().takeWhile(t -> t.length() == 4).forEach(System.out::println);
        //获取满足条件的元素, 如果遇到不满足条件的元素,就会立即停止,不管后面的满不满足;

        //ciel.stream().dropWhile(t -> t.length() == 4).forEach(System.out::println);
        //删除满足条件的元素,如果遇到不满足条件的元素,就会立即停止,不管后面的满不满足;

        //long count1 = Stream.ofNullable(null).count();//空stream
       // System.err.println("uu" + count1);


        //数组扁平化

        List<List> lsn = new ArrayList<>();
        //lsn.add(List.of("a", "b"));
       // lsn.add(List.of("c", "d"));

        Object collect7 = lsn.stream().flatMap(t -> t.stream()).collect(Collectors.toList());
        System.out.println(collect7);

        Stream<UUID> generate = Stream.generate(UUID::randomUUID); //生成stream流
        generate.limit(50).forEach(t -> logger.info(t.toString()));

    }


    public static void show(Map.Entry me) {
        System.out.println(me.getKey().toString().concat(me.getValue().toString()));
    }
}