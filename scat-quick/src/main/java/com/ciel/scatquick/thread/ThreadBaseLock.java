package com.ciel.scatquick.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadBaseLock {
    public static void main(String[] args) {

        Goodss gs = new Goodss();

        new Thread(new Produces(gs)).start();
        new Thread(new Produces(gs)).start();
        new Thread(new Consumes(gs)).start();
        new Thread(new Consumes(gs)).start();

    }

    public static class Goodss {
        private String name;
        private int number;
        private boolean flag;

        public int addNumber() {
            return number++;
        }

        private Lock loc = new ReentrantLock(); // 创建了一个对象锁;

        private Condition conP = loc.newCondition(); // 根据锁,来获取一个具有,等待,唤醒功能的这样一个对象;
        private Condition conC = loc.newCondition(); // 或者这样说;返回一个绑定到lock实例的对象;
        //一个锁上可以有多个Condition对象;

        public void produce(String name) throws Exception {

            loc.lock(); // (显式,手动的)获取锁;synchronized是隐式的锁;这个显式;
            try {
                while (flag) {
                    conP.await(); // 让生产线程等待;让以这个对象为锁的线程等待,并且它只能被以这个对象为锁的线程唤醒; (等待唤醒必须是同一个锁);
                }                        //和生产线程代码呆在一起;
                this.name = name;
                this.addNumber();
                System.out.println(Thread.currentThread().getName() + "生产者===" + this.name + "===" + this.number);
                this.flag = true;
                conC.signal();       //唤醒消费者线程(唤醒对方)

            } finally  //如果程序出现异常,就会跳到对应的catch块,那也就不会释放锁了,所以写在finally里;(因为lock需要手动释放锁);
            {
                loc.unlock(); // (显式,手动的)释放锁;synchronized是隐式的锁;这个显式;
            }
        }

        public void consume() throws Exception {
            loc.lock(); // (显式)获取锁
            try {
                while (!flag) {
                    conC.await(); //让消费线程等待;让以这个对象为锁的线程等待,并且它只能被以这个对象为锁的线程唤醒; (等待唤醒必须是同一个锁);
                }
                System.out.println(Thread.currentThread().getName() + "消费者---" + this.name + "---" + this.number);
                this.flag = false;
                conP.signal(); // 唤醒生产者线程(唤醒对方)
            } finally {
                loc.unlock(); // (显式)释放锁
            }

        }
    }

    public static class Produces implements Runnable {
        private Goodss g;

        public Produces(Goodss g) {
            this.g = g;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    g.produce("茶叶蛋");
                }
            } catch (Exception e) {
            }
        }
    }

    public static class Consumes implements Runnable {
        private Goodss g;

        public Consumes(Goodss g) {
            this.g = g;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    g.consume();
                }
            } catch (Exception e) {
            }
        }
    }

}