package com.ciel.scatquick.thread;

import lombok.SneakyThrows;

public class ThreadBase {
    public static void main(String[] args) {
        Goods g = new Goods("茶叶蛋");

        new Thread(() -> {
            while (true) {
                g.produce();
            }
        },"PRODUCE1").start();

        new Thread(() -> {
            while (true) {
                g.produce();
            }
        },"PRODUCE2").start();


        new Thread(() -> {
            while (true) {
                g.consume();
            }
        },"CONSUME1").start();


        new Thread(() -> {
            while (true) {
                g.consume();
            }
        },"CONSUME2").start();

    }

    public static class Goods {
        private String name;
        private int number;
        private boolean flag; //信号灯

        public Goods(String name){
            this.name=name;
        }

        synchronized public int addNumber() {
            return number++;
        }

        //同步方法使用的锁是this,这个Goods类的当前对象;
        @SneakyThrows
        synchronized public void produce() {
            //让被唤醒的线程再一次判断标记,如果是true,有商品,等待挂起;
            //(如果写if,那么有可能会唤醒生产线程,但是生产会继续往下执行,不在判断了)

            while (flag) {  //当线程被等待的时候,也会释放锁,其他线程就可以进来;
                this.wait(); //sleep不会释放锁
            }

            this.name = name;
            this.addNumber();
            System.out.println(Thread.currentThread().getName() + this.name + ">>>" + this.number);
            this.flag = true;

            //唤醒全部被等待的线程,(如果只唤醒第一个被等待的话,有可能唤醒了另一个生产者,出现生产两次(覆盖了),却只消费一次的情况);
            //也会唤醒了本方的线程,但是它会进入while判断,而再次等待
            this.notifyAll();

            //如果只this.notify(),那么有可能只唤醒本方的线程,然后本方线程也等待了,
            //这样就陷入了全部线程都在等待,没人任何一个线程执行notify()唤醒了;
        }

        @SneakyThrows
        synchronized public void consume() {
            while (!flag) {
                this.wait();
            }
            System.out.println(Thread.currentThread().getName() + this.name + ">>>" + this.number);
            this.flag = false;
            this.notifyAll();

        }
    }

}