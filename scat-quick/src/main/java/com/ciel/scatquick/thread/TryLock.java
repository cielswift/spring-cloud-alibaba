package com.ciel.scatquick.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class TryLock {

    public static void main(String[] args) throws InterruptedException {

    }
    public static void mains(String[] args) throws InterruptedException {

        Lock lock = new ReentrantLock();

        //多线程同时读，但只有一个线程能写的问题。
        //如果有线程正在读，写线程需要等待读线程释放锁后才能获取写锁，即读的过程中不允许写，这是一种悲观的读锁。
        ReadWriteLock lock1 = new ReentrantReadWriteLock();
        Lock lock2 = lock1.readLock(); //读锁 ,多个线程可以同时读
        Lock lock3 = lock1.writeLock(); //写锁,只能由一个线程写

        //读的过程中也允许获取写锁后写入 //乐观锁
        StampedLock stampedLock = new StampedLock();
        long writeLock = stampedLock.writeLock(); //写锁
        stampedLock.unlockWrite(writeLock); //释放写锁


        long stamp = stampedLock.tryOptimisticRead(); // 获得一个乐观读锁,并返回版本号
        if(!stampedLock.validate(stamp)){ // 检查乐观读锁后是否有其他写锁发生 ;通过validate()去验证版本号

            long lockRead = stampedLock.readLock();// 获取一个悲观读锁
            stampedLock.unlockRead(lockRead); // 释放悲观读锁

        }


        Condition condition = lock.newCondition();


        List<Integer> list = new ArrayList<Integer>();
        list.add(0);

        for (int i=0; i<1000; i++){
            new Thread(() -> {

                lock.lock();
                Integer integer = list.get(0);
                integer+=1;
                list.set(0,integer);

                  lock.unlock();

            }).start();
        }


        while(true){
            System.out.println(list.get(0));
            Thread.sleep(1000);
        }
    }
}
