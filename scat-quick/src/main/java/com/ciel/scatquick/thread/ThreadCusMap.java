package com.ciel.scatquick.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThreadCusMap {

    public static void main(String[] args) throws InterruptedException {

        Map<String, String> map = new HashMap<>();

        Map<String, String> cus = new ConcurrentHashMap<>(); //适合少量并发 线程安全

        Map<String, String> skip = new ConcurrentSkipListMap<>(); //适合大量并发 线程安全

        new Thread(new Jobs(cus)).start();
        new Thread(new Jobs(cus)).start();

        new Thread(new Jobs(map)).start();
        new Thread(new Jobs(map)).start();

        new Thread(new Jobs(skip)).start();
        new Thread(new Jobs(skip)).start();

          //适合 读多写少
        CopyOnWriteArrayList<String> copy = new CopyOnWriteArrayList<>();


        while (true){

            System.out.println("===========================");
            System.out.println("ConcurrentHashMap:"+ cus.size());

            System.out.println("HashMap:"+ map.size());

            System.out.println("ConcurrentSkipListMap:"+ skip.size());
            Thread.sleep(1000);
            System.out.println("===========================");
        }

    }
}

class Jobs implements Runnable{

    private Map<String, String> map;

    public Jobs(Map<String, String> map){
        this.map=map;
    }

    @Override
    public void run() {

        for(int i = 0; i<5000;i++){

            map.put(Thread.currentThread().getName()+i,"20");
        }
    }
}