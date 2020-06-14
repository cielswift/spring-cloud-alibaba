package com.ciel.scatquick.thread;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceThread {

    public static void main(String[] args) {
        //自旋锁
        AtomicReference cas = new AtomicReference();

        lock(cas);

        unlock(cas);

    }

    public static void lock(java.util.concurrent.atomic.AtomicReference cas) { //不断重试来获取锁
        while (!cas.compareAndSet(null, Thread.currentThread())) {

        }
    }

    public static void unlock(java.util.concurrent.atomic.AtomicReference cas) { //释放锁
        cas.compareAndSet(Thread.currentThread(), null);
    }
}
