package com.ciel.scatquick.thread;

import lombok.SneakyThrows;

import java.util.concurrent.CyclicBarrier;

public class ThreadCyclic  {

    public static void main(String[] args) {

        //5个线程为一组,当一组执行完后就会执行Sth任务;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5,new Sth());

        for(int i=0 ;i<10;i++){
            new Thread(new Empty(cyclicBarrier), "名"+i).start();
        }

    }
}

class Empty implements Runnable{

    private CyclicBarrier cyclicBarrier;

    public Empty(CyclicBarrier cyclicBarrier){
        this.cyclicBarrier=cyclicBarrier;
    }

    @SneakyThrows
    @Override
    public void run() {
        Thread.sleep(1000);
        System.out.println("第"+Thread.currentThread().getName()+"正在去会议室开会");
        cyclicBarrier.await(); //阻塞当前线程; 已到达屏障 ;或已执行完毕
    }
}

class Sth implements Runnable{

    @Override
    public void run() {
        System.out.println("第"+Thread.currentThread().getName()+"正在 组织会议");
    }
}