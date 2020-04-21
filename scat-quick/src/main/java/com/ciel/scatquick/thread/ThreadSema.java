package com.ciel.scatquick.thread;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.concurrent.Semaphore;

public class ThreadSema {

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1); //控制线程并发数量 ,1个线程相当于同步代码

        Num num = new Num();

        for(int i=0; i<5000; i ++){
            new Thread(new Hels(num,semaphore)).start();
        }

        while (true){
            System.out.println(num.getNum());
            Thread.sleep(1000);
        }

    }
}

class Hels implements Runnable{

    private Semaphore semaphore;
    private Num num;

    public Hels(Num num, Semaphore semaphore){
        this.semaphore=semaphore;
        this.num=num;
    }

    @SneakyThrows
    @Override
    public void run() {

        semaphore.acquire();//许可
        System.out.println(Thread.currentThread().getName()+":A");
        num.setNum(num.getNum()+1);
        semaphore.release(); //释放 acquire 和 release 中间是同步代码

    }
}

@Data
class Num{
    private int num = 0;
}