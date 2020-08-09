package com.ciel.scatquick.thread;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class ThreadCyclic {

    public static void main(String[] args) {

        //加法计数器 (循环屏障)
        //5个线程为一组,当一组执行完后就会执行第二个构造参数任务;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, () -> {
            System.out.println("第" + Thread.currentThread().getName() + "正在组织会议");
        });


        for (int i = 0; i < 10; i++) {
            final String temp = String.valueOf(i);

            new Thread(() -> {
                try {
                    /**
                     * 其中一个线程发送中断信号后（t.interrupt()）后，
                     * 员工5的(先前调用的)await()方法会触发InterruptedException异常，
                     * 此时其他线程，自己立即也不等了，内部从await()方法中触发BrokenBarrierException异常，然后继续执行，
                     * 后面的新线程也直接抛出了BrokenBarrierException异常;
                     */
                    Thread.sleep(2000);
                    System.out.println("第" + Thread.currentThread().getName() + "正在去会议室开会");
                    cyclicBarrier.await(); //阻塞当前线程; 已到达屏障 ;或已执行完毕

                    if ("2".equals(temp)) {
                        cyclicBarrier.reset(); //重置规则
                       // Thread.currentThread().interrupt();
                    }

                    /**
                     * await方法等待1秒之后，触发了TimeoutException异常，
                     * 然后继续向下运行，其他线程他们的await抛出了BrokenBarrierException异常）； 继续执行
                     */
                   // int await = cyclicBarrier.await(1, TimeUnit.SECONDS);

                    System.out.println("第" + Thread.currentThread().getName() + ":全部到齐,按顺序做到自己的位置上");
                } catch (Exception e) {

                }
            }, temp.concat("线程")).start();
        }

    }
}