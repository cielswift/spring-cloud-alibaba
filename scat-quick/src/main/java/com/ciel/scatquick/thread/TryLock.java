package com.ciel.scatquick.thread;

import org.springframework.util.ResourceUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class TryLock {

    public static void main(String[] args) throws InterruptedException {

        Lock lock = new ReentrantLock(); //锁
        Condition condition = lock.newCondition(); //等待和唤醒的对象

        List<Integer> list = new ArrayList<>();
        list.add(0);

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {

                try {
                    lock.lock();

                    Integer integer = list.get(0);
                    integer += 1;
                    list.set(0, integer);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    lock.unlock();
                }

            }).start();
        }

//        while (true) {
//            System.out.println(list.get(0));
//            Thread.sleep(1000);
//        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////

        //多线程同时读，但只有一个线程能写的问题。
        //如果有线程正在读，写线程需要等待读线程释放锁后才能获取写锁，即读的过程中不允许写，这是一种悲观的读锁。
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock readLock = readWriteLock.readLock(); //读锁 ,多个线程可以同时读
        Lock writeLock = readWriteLock.writeLock(); //写锁,只能由一个线程写


        HashMap<String, String> hashMap = new HashMap<>();

        for (int i = 0; i < 1000; i++) {

            final String temp = String.valueOf(i);

            new Thread(() -> {

                writeLock.lock();
                hashMap.put(temp,temp+"四月是你的谎言");
                writeLock.unlock();

                readLock.lock();
                System.out.println(hashMap.get(temp));
                readLock.unlock();

            },i + "=线程=").start();
        }

        //////////////////////////////////////////////////////////////////////////////

        //读的过程中也允许获取写锁后写入 //乐观锁
        StampedLock stampedLock = new StampedLock();
        long version = stampedLock.writeLock(); //写锁 版本
        stampedLock.unlockWrite(version); //释放写锁 根据版本


        long stamp = stampedLock.tryOptimisticRead(); // 获得一个乐观读锁,并返回版本号
        if (!stampedLock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生 ;通过validate()去验证版本号

            long lockRead = stampedLock.readLock();// 获取一个悲观读锁
            stampedLock.unlockRead(lockRead); // 释放悲观读锁
        }



    }


}
