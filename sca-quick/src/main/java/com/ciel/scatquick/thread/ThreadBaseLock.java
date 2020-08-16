package com.ciel.scatquick.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadBaseLock {
    public static void main(String[] args) {
        Foods fs = new Foods("德国烤香肠");

        new Thread(() -> { while (true) { fs.produce(); } }, "PRODUCER1").start();
        new Thread(() -> { while (true) { fs.produce(); } }, "PRODUCER2").start();

        new Thread(() -> {while (true) { fs.ship(); } }, "SHIP1").start();
        new Thread(() -> { while (true) { fs.ship(); } }, "SHIP2").start();

        new Thread(() -> { while (true) { fs.consumer(); } }, "CONSUMER1").start();
        new Thread(() -> { while (true) { fs.consumer(); } }, "CONSUMER2").start();
    }

    public static class Foods {
        private String name;
        private int number;
        private int flag;

        public Foods(String name) {
            this.name = name;
        }
        /**
         * 可重入锁
         * 可中断锁
         */
        private Lock loc = new ReentrantLock(true); // true公平锁,性能低

        //根据锁,来获取一个具有,等待,唤醒功能的这样一个对象
        //或者这样说;返回一个绑定到lock实例的对象
        //一个锁上可以有多个Condition对象

        private Condition pro = loc.newCondition(); //生产
        private Condition ship = loc.newCondition(); //运输
        private Condition con = loc.newCondition(); //消费

        public void produce() {

            // (显式,手动的)获取锁;synchronized是隐式的锁;这个显式;
            loc.lock();
            try {
                while (flag != 0) {
                    pro.await();   //生产者等待 ,必须是0才能生产
                }

                number++;
                System.out.println(Thread.currentThread().getName() + this.name + "===" + this.number);

                this.flag = 1; //生产完成 运输状态

                ship.signal();     //唤醒运输

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //如果程序出现异常,就会跳到对应的catch块,那也就不会释放锁了,所以写在finally里;(因为lock需要手动释放锁);
                // (显式,手动的)释放锁;synchronized是隐式的锁;这个显式;
                loc.unlock();
            }
        }

        public void ship() {
            loc.lock();
            try {
                while (flag != 1) {
                    ship.await();
                }
                System.out.println(Thread.currentThread().getName() + this.name + "---" + this.number);

                this.flag = 2; //运输完成 消费状态

                con.signal();  //唤醒消费
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                loc.unlock();
            }

        }

        public void consumer() {
            loc.lock();
            try {
                while (flag != 2) {
                    con.await();
                }
                System.out.println(Thread.currentThread().getName() + this.name + "---" + this.number);

                this.flag = 0; //消费完成 生产状态

                pro.signal();  //唤醒生产

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                loc.unlock();
            }

        }
    }

}