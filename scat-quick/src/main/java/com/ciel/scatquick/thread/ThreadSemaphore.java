package com.ciel.scatquick.thread;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.concurrent.Semaphore;

public class ThreadSemaphore {

    public static void main(String[] args) {




        Semaphore semaphore = new Semaphore(3); //控制线程并发数量 ,1个线程相当于同步代码

        Num num = new Num();

//        for (int i = 0; i < 5000; i++) {
//            new Thread(new Helstrd(num, semaphore)).start();
//        }

        for (int i = 0; i < 5000; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();

                    System.out.println(Thread.currentThread().getName() + ":运行中");
                    Thread.sleep(3000);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }

            }, i + "==线程==").start();
        }


//        while (true) {
//            System.out.println(num.getNum());
//            Thread.sleep(1000);
//        }
    }


    public static class Helstrd implements Runnable {

        private Semaphore semaphore;
        private Num num;

        public Helstrd(Num num, Semaphore semaphore) {
            this.semaphore = semaphore;
            this.num = num;
        }

        @SneakyThrows
        @Override
        public void run() {

            semaphore.acquire();//许可
            System.out.println(Thread.currentThread().getName() + ":A");
            num.setNum(num.getNum() + 1);
            semaphore.release(); //释放 acquire 和 release 中间是同步代码
        }

    }

    @Data
    public static class Num {
        private int num = 0;
    }
}

