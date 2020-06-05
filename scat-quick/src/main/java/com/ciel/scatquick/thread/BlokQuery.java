package com.ciel.scatquick.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class BlokQuery {

    public static void main(String[] args) throws IOException, InterruptedException {
        //阻塞队列
        BlockingQueue blockingQueue = new SynchronousQueue<Runnable>();

        BlockingQueue blockingQueue1 = new ArrayBlockingQueue<>(1 << 7); //非阻塞队列

        BlockingQueue blockingQueue3 = new LinkedBlockingQueue<>(1<<4);

        BlockingQueue blockingQueue2 = new DelayQueue(); //双端队列

        LinkedList<String> linkedList = new LinkedList<>();

        blockingQueue1.put("a"); //如果队列满了，一直阻塞，直到队列不满了或者线程被中断-->阻塞

        blockingQueue1.offer("b"); //如果队列没满，立即返回true； 如果队列满了，立即返回false-->不阻塞

        blockingQueue1.offer("c",2,TimeUnit.SECONDS);
        //在队尾插入一个元素,，如果队列已满，则进入等待，直到出现以下三种情况：-->阻塞
        //被唤醒  //等待时间超时 //当前线程被中断

        blockingQueue1.poll(); //如果没有元素，直接返回null；如果有元素，出队

        blockingQueue1.take(); // take()：如果队列空了，一直阻塞，直到队列不为空或者线程被中断-->阻塞

        blockingQueue1.poll(2,TimeUnit.SECONDS);

        h1();
    }

    public static void h1() throws IOException {

        if(System.getProperty("os.name").toLowerCase().contains("win")){
            Process exec = Runtime.getRuntime().exec("C:\\Users\\Administrator\\Desktop\\start_redis6510.bat");
        }

        
    }
}
