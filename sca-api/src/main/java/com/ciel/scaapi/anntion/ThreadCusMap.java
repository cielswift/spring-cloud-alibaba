package com.ciel.scaapi.anntion;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class ThreadCusMap {

    public static void main(String[] args) throws InterruptedException {

        Map<String, String> map = new HashMap<>();

        Map<String, String> cus = new ConcurrentHashMap<>();

        new Thread(new Jobs(cus)).start();
        new Thread(new Jobs(cus)).start();


        while (true){
            System.out.println(cus.size());
            Thread.sleep(1000);
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