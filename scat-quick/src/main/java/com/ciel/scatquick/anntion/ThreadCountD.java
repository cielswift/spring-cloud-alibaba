package com.ciel.scatquick.anntion;

import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;

public class ThreadCountD {

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(1); //计数器次数

        new Thread(new ThreadA(countDownLatch)).start();
        new Thread(new ThreadB(countDownLatch)).start();

    }
}

class ThreadA implements Runnable{

    private CountDownLatch countDownLatch;

    public ThreadA(CountDownLatch count){
        this.countDownLatch = count;
    }


    @Override
    public void run() {

        System.out.println("A");

        try {
            countDownLatch.await(); //当前线程让出cpu,计数器减到0时 才继续执行
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("C");
    }
}

class ThreadB implements Runnable{

    private CountDownLatch countDownLatch;

    public ThreadB(CountDownLatch count){
        this.countDownLatch = count;
    }

    @SneakyThrows
    @Override
    public void run() {

        System.out.println("B");
        countDownLatch.countDown(); //计数器-1
    }
}

