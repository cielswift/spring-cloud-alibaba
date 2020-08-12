package com.ciel.scatquick.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.*;

public class TryReentrantLock {

    public static void reentranLock() throws InterruptedException {

        ReentrantLock lock = new ReentrantLock(true); //可重入锁 公平锁
        Condition condition = lock.newCondition(); //等待和唤醒的对象

        List<Integer> list = new ArrayList<>();
        list.add(0);

        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                try {
                    //lock.tryLock(); //尝试立即获取锁
                    //  boolean tryLock = lock.tryLock(2, TimeUnit.SECONDS);//等待一定时间获取锁

                    //isHeldByCurrentThread：实例方法，判断当前线程是否持有ReentrantLock的锁
                    lock.lock();

                    /**
                     * 如果其他线程调用condition的signal或者signalAll方法并且当前线程获取Lock从await方法返回
                     * ，如果在等待状态中被中断会抛出被中断异常
                     *  如调用interrupt() 中断线程, 回出现InterruptedException异常,
                     *  且线程中断标志被清除,标志的变换过程：false->true->false
                     *
                     *  会释放锁
                     */
                    //condition.await();

                    //当前线程进入等待状态直到被通知，中断或者超时；
                    //long nanos = condition.awaitNanos();

                    /**
                     * 支持自定义时间单位，false：表示方法超时之后自动返回的，true：表示等待还未超时时，
                     * await方法就返回了（超时之前，被其他线程唤醒了）
                     */
                    //condition.await(1,TimeUnit.SECONDS);

                    /**
                     * 前线程进入等待状态直到被通知，中断或者到了某个时间
                     */
                   // boolean awaitUntil = condition.awaitUntil(new Date());

                    /*当前线程进入等待状态，不会响应线程中断操作，只能通过唤醒的方式让线程继续*/
                    //condition.awaitUninterruptibly();

                    /**
                     * 不会释放锁
                     */
                   // LockSupport.park();
                   // LockSupport.unpark(Thread.currentThread()); //唤醒线程 (参数)



                    Integer integer = list.get(0);
                    integer += 1;

                    list.set(0, integer);

                    //condition.signalAll();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }).start();
        }

        while (true) {
            Thread.sleep(100);
            System.out.println(list.get(0));
        }
    }

    public static void readWriteLock() {

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
                hashMap.put(temp, temp + "四月是你的谎言");
                writeLock.unlock();

                readLock.lock();
                System.out.println(hashMap.get(temp));
                readLock.unlock();

            }, i + "=线程=").start();
        }
    }

    public static volatile int cut = 984;

    public static void stampedLock() {

        //读的过程中也允许获取写锁后写入 //乐观锁
        StampedLock stampedLock = new StampedLock();

        long version = stampedLock.writeLock(); //写锁 返回版本 0 表示失败
        cut = 1 << 5; //操作资源
        stampedLock.unlockWrite(version); //释放写锁 根据版本

        long stamp = stampedLock.tryOptimisticRead(); // 获得一个乐观读锁,并返回版本号

        if (!stampedLock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生 ;通过validate()去验证版本号

            //说明有其他写锁
            long lockRead = stampedLock.readLock();// 获取一个悲观读锁
            cut = 1 << 6; //操作资源
            stampedLock.unlockRead(lockRead); // 释放悲观读锁
        }

        long tti = stampedLock.readLock();// 获取一个悲观读锁
        long ws = stampedLock.tryConvertToWriteLock(tti);  //读锁转换为写锁

        stampedLock.unlockRead(ws);
        long sp = stampedLock.writeLock();
        stampedLock.unlock(sp);
    }

    public static void main(String[] args) throws InterruptedException {

        reentranLock();

        ///////////////////////////////////////////////////////////////////////////////////////////////////

        //readWriteLock();
        //////////////////////////////////////////////////////////////////////////////

        //stampedLock();
    }
}
