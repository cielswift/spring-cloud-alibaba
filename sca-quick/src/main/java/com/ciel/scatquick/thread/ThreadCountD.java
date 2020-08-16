package com.ciel.scatquick.thread;

import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ThreadCountD {

    public static void main(String[] args) {
        //减法计数器
        CountDownLatch countDownLatch = new CountDownLatch(2); //计数器次数

        new Thread(new ThreadA(countDownLatch)).start();
        new Thread(new ThreadB(countDownLatch)).start();
        new Thread(new ThreadC(countDownLatch)).start();

    }

    public static class ThreadA implements Runnable {
        private CountDownLatch countDownLatch;

        public ThreadA(CountDownLatch count) {
            this.countDownLatch = count;
        }

        @SneakyThrows
        @Override
        public void run() {
            System.out.println("1");

            //countDownLatch.await(); //调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行

            //和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行
            //表示最多等1秒，不管计数器是否为0，await方法都会返回，若等待时间内，
            //计数器变为0了，立即返回true，否则超时后返回false
            boolean await = countDownLatch.await(1, TimeUnit.SECONDS);
            System.out.println(await);

            System.out.println("6"); //一定是最后执行
        }
    }

    public static class ThreadB implements Runnable {
        private CountDownLatch countDownLatch;

        public ThreadB(CountDownLatch count) {
            this.countDownLatch = count;
        }

        @SneakyThrows
        @Override
        public void run() {
            try{
                System.out.println("2");
            }finally {
                /**
                 * 计数器-1
                 */
                countDownLatch.countDown();
            }
            System.out.println("3");  //D 和 C 不一定谁先执行
        }
    }

    public static class ThreadC implements Runnable {
        private CountDownLatch countDownLatch;

        public ThreadC(CountDownLatch count) {
            this.countDownLatch = count;
        }

        @SneakyThrows
        @Override
        public void run() {

            try{
                System.out.println("4");
            }finally {
                /**
                 * 计数器-1
                 */
                countDownLatch.countDown(); //
            }
            System.out.println("5");  //D 和 C 不一定谁先执行
        }
    }
}



