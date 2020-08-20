package com.ciel.scatquick.thread;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicCasThread {
    
    public static void main(String[] args) {

        //自旋锁
        AtomicReference<Thread> cas = new AtomicReference<>();
        lock(cas);
        unlock(cas);

        AtomicStampedReference<Integer> stampedReference =
                new AtomicStampedReference<>(10,1); //初始10 版本号1

        int stamp = stampedReference.getStamp(); //获得版本号

        //旧值 新值 版本号 下一个版本号
        stampedReference.compareAndSet(10,11,stamp, ++stamp);

        System.out.println(stampedReference.getStamp()); //版本号
        System.out.println(stampedReference.getReference()); //值

    }

    /**
     * 比较并设置
     * 如是null 则设置为当前线程
     *  不断重试来获取锁
     */
    public static void lock(AtomicReference<Thread> cas) {
        while (!cas.compareAndSet(null, Thread.currentThread())) {
            System.out.println("not get  try aging");
        }
    }

    /**
     * 比较并设置
     * 如果是当前线程 则 设置为null
     */
    public static void unlock(AtomicReference<Thread> cas) {
        cas.compareAndSet(Thread.currentThread(), null);
    }
}
