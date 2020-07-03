package com.ciel.scatquick.thread;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CyclicBarrier;

public class ThreadCyclic {

    public static void main(String[] args) {

        //加法计数器
        //5个线程为一组,当一组执行完后就会执行第二个构造参数任务;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            System.out.println("第" + Thread.currentThread().getName() + "正在组织会议");
        });

        for (int i = 0; i < 10; i++) {
            final String temp = String.valueOf(i);

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("第" + Thread.currentThread().getName() + "正在去会议室开会");
                    cyclicBarrier.await(); //阻塞当前线程; 已到达屏障 ;或已执行完毕
                } catch (Exception e) {

                }
            }, temp).start();
        }

    }
}