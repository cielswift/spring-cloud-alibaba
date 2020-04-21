package com.ciel.scatquick.thread;

import lombok.SneakyThrows;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ThreadExcut {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();  //线程数据交换

        new Thread(new Body(exchanger),"body").start();
        new Thread(new Girl(exchanger),"girl").start();
      //  new Thread(new Three(exchanger),"three").start();
    }
}

class Body implements Runnable{
    private Exchanger<String> exchanger;

    public Body(Exchanger<String> exchanger){
        this.exchanger=exchanger;
    }

    @SneakyThrows
    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName()+":定情信物: 同心锁");
        String exchange = exchanger.exchange("同心锁",1, TimeUnit.SECONDS); //会等待,设置超时

        System.out.println(Thread.currentThread().getName()+":收到"+exchange);
    }
}

class Girl implements Runnable{
    private Exchanger<String> exchanger;

    public Girl(Exchanger<String> exchanger){
        this.exchanger=exchanger;
    }

    @SneakyThrows
    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName()+":定情信物: 药匙");

        //Thread.sleep(2000);
        String exchange = exchanger.exchange("药匙",1, TimeUnit.SECONDS);

        System.out.println(Thread.currentThread().getName()+":收到"+exchange);
    }
}

class Three implements Runnable{
    private Exchanger<String> exchanger;

    public Three(Exchanger<String> exchanger){
        this.exchanger=exchanger;
    }

    @SneakyThrows
    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName()+":定情信物: 辟孕套");
        String exchange = exchanger.exchange("润滑油"); //会等待

        System.out.println(Thread.currentThread().getName()+":收到"+exchange);
    }
}