package com.ciel.scaapi.retu.jdkproxy;

import lombok.SneakyThrows;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.*;

public class Employee implements Boss {

    int a = 0;

    @Override
    public void doSomethinig() {
        System.out.println("员工工作中 ...");
    }
    @Override
    public void finishTasks(String name) {
        System.out.println("员工正在完成项目 " + name + " ...");
    }

    public static void main(String[] args) {


        LongAdder longAdder = new LongAdder();

        AtomicLong atomicLong = new AtomicLong();

        Lock lock = new ReentrantLock();

        ReadWriteLock lock2 = new ReentrantReadWriteLock();

        Employee emp = new Employee();

        for (int i =0 ; i<100000 ;i ++){

            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {

                    Thread.sleep(10);
                    longAdder.add(1);
                    System.out.println("longAdder:"+longAdder);

                    long l = atomicLong.incrementAndGet();

                    System.out.println("AtomicLong:"+l);

//                    lock.lock();
//
//                    emp.a+=1;
//
//                    System.out.println("a:"+ emp.a);
//                    lock.unlock();
                }
            }).start();
        }

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(20, 30, 10,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(30));

        
    }


}