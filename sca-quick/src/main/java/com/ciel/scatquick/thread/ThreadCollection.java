package com.ciel.scatquick.thread;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class ThreadCollection {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {

        //线程安全的集合
        Map<String, String> map = new HashMap<>();

        map.put("k","vs");

        //值不存在，会使用函数值代替为新值
        String k = map.computeIfAbsent("ks", x -> x);

        //值存在示例，会使用函数值代替旧值
        String kg = map.computeIfPresent("k", (x, y) -> {
           return x + y;
        });

        //当key的值存在时不替换值,且返回旧值; 当key的值不存在时替换key的值,返回null
        String s = map.putIfAbsent("dk", "ss");

        //当key的值存在时获取值，否则获取指定的默认值
        String orDefault = map.getOrDefault("dk", "sdc");

        /**
         * 如果key的值不存在会用新的值（第二参数）代替
         * 如果key的值存在会用函数值代替旧值；新的值（第二参数）不起作用
         * 当函数值为空会移除key；
         */
        String merge = map.merge("k", "sdv", (x, y) -> x + y);

        System.out.println(k);
        System.out.println(kg);
        System.out.println(s);

        System.exit(0);
        Map<String, String> cus = new ConcurrentHashMap<>(); //适合少量并发 线程安全

        Map<String, String> skip = new ConcurrentSkipListMap<>(); //适合大量并发 线程安全

        new Thread(new Jobs(cus)).start();
        new Thread(new Jobs(cus)).start();

        new Thread(new Jobs(map)).start();
        new Thread(new Jobs(map)).start();

        new Thread(new Jobs(skip)).start();
        new Thread(new Jobs(skip)).start();

        //适合 读多写少的
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<>();

        while (true) {
            System.out.println("===========================");
            System.out.println("ConcurrentHashMap:" + cus.size());

            System.out.println("HashMap:" + map.size());

            System.out.println("ConcurrentSkipListMap:" + skip.size());
            System.out.println("===========================");
            Thread.sleep(1000);
        }
    }

    public static class Jobs implements Runnable {
        private Map<String, String> map;
        public Jobs(Map<String, String> map) {
            this.map = map;
        }
        @Override
        public void run() {
            for (int i = 0; i < 50000; i++) {
                map.put(Thread.currentThread().getName() + i, "20");
            }
        }
    }
}

