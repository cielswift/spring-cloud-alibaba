package com.ciel.scatquick.anntion;

import io.micrometer.core.instrument.util.NamedThreadFactory;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 测试java spi机制
 */
public class SpiClass implements SpiInterface {

    @Override
    public String vue(String in) {
        System.out.println(in+"<<<");
        return in+">>";
    }

    public static void main(String[] args) throws InterruptedException {

        ThreadFactory factory = new NamedThreadFactory("task-pool");

        ThreadPoolExecutor threadPool =
                new ThreadPoolExecutor(200, 200, 5, TimeUnit.SECONDS,
                        new LinkedBlockingDeque<>(2048),factory);

      //  threadPool.execute(() -> System.out.println("aaa")); //会直接抛出任务执行时的异常
     //   threadPool.submit(() -> System.out.println("bbb")); //submit会吃掉异常

        LongAdder longAdder = new LongAdder();

        AtomicLong atomicLong = new AtomicLong();

        Lock lock = new ReentrantLock();

        ReadWriteLock lock2 = new ReentrantReadWriteLock();

        for (int i = 0; i < 2000; i++) {

            threadPool.execute(() -> {
                lock.lock();
                try {
                    Thread.sleep(10);
                    longAdder.add(1);
                    System.out.println(Thread.currentThread().getName()+"<<longAdder:" + longAdder);

                    long l = atomicLong.incrementAndGet();
                    System.out.println(Thread.currentThread().getName()+"<<AtomicLong:" + l);

                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    lock.unlock();
                }

            });

        }

        while (true){
            System.out.println("longAdder:"+ longAdder);
            System.out.println("AtomicLong:"+ atomicLong);
            Thread.sleep(1000);
        }
    }
}
