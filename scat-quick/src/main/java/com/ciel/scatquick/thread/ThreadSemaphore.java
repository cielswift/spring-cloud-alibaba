package com.ciel.scatquick.thread;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ThreadSemaphore {

    public static void main(String[] args) {

        /**
         * 三个许可证
         * 控制线程并发数量 ,1个线程相当于同步代码
         */
        Semaphore semaphore = new Semaphore(3, true);

        for (int i = 0; i < 5000; i++) {
            new Thread(() -> {

                boolean isSuccess = false;
                try {
                    semaphore.acquire();//获取一个许可
                    isSuccess = true; //许可获取成功

                    //semaphore.acquire(2); //指定许可数量
                    //指定许可数量尝试获取许可,true表示获取成功，false表示获取失败
                    //boolean acquire = semaphore.tryAcquire(2, 1, TimeUnit.SECONDS);

                    int permits = semaphore.availablePermits();
                    System.out.println(String.format("当前可用的许可数量: %s",permits));

                    System.out.println(Thread.currentThread().getName() + ":运行中");
                    Thread.sleep(3000);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    if(isSuccess){ //成功在释放 ,如果凭空释放 会导致许可增多;

                        System.out.println("===================================================================");
                        semaphore.release(); //释放许可
                        //semaphore.release(2); //释放多个
                    }


                }

            }, i + "==线程").start();
        }


    }

}

