package com.ciel.scatquick.thread;

import java.io.IOException;
import java.util.concurrent.*;

public class BlokQuery {

    public static void main(String[] args) throws IOException, InterruptedException {
        //阻塞队列
        BlockingQueue<Runnable> blockingQueue = new SynchronousQueue<Runnable>(); //同步队列 只有1个元素

        BlockingQueue<String> blockingQueue1 = new ArrayBlockingQueue<>(1); //非阻塞队列

        BlockingQueue<Runnable> blockingQueue3 = new LinkedBlockingQueue<>(1<<4);

        BlockingQueue blockingQueue2 = new DelayQueue(); //双端队列

//        blockingQueue.put("aa");
//        blockingQueue.take();

        for(int i=0 ; i<100; i++){

            final String temp = String.valueOf(i);

            new Thread(() -> {
                try {
                    blockingQueue1.put("aaa"+temp);
                    System.out.println("存入了");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {

                try {
                    Object take = blockingQueue1.take();
                    System.out.println("消费了"+take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        blockingQueue1.element(); //产看队首元素 没有会异常
        blockingQueue1.peek();  //产看队首元素 没有返回null


       // blockingQueue1.add(""); //添加 满了异常
     //   blockingQueue1.remove(""); //移除 空了异常


//        blockingQueue1.put("a"); //  如果队列满了，一直阻塞，直到队列不满了或者线程被中断-->阻塞
//
//        blockingQueue1.offer("b"); //如果队列没满，立即返回true； 如果队列满了，立即返回false-->不阻塞
//
//        blockingQueue1.offer("c",2,TimeUnit.SECONDS);
//        //在队尾插入一个元素,，如果队列已满，则进入等待，直到出现以下三种情况：-->阻塞
//        //被唤醒  //等待时间超时 //当前线程被中断
//
//        blockingQueue1.poll(); //如果没有元素，直接返回null；如果有元素，出队
//
//        blockingQueue1.take(); // take()：如果队列空了，一直阻塞，直到队列不为空或者线程被中断-->阻塞
//
//        blockingQueue1.poll(2,TimeUnit.SECONDS);

    }

}
