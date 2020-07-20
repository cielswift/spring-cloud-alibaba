package com.ciel.scatquick.thread;

import lombok.Data;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class VolatileAndAtom {

    public static void main(String[] args) throws InterruptedException {
        //volatile 修改不能依赖于当前值 , volatile适合一写多读, 并且写不依赖当前值
        //volatile 修改对所有线程可见 ,立即写入主内存
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

        Fox fox = new Fox();
        new Thread(fox::run).start();

        Thread.sleep(1000);

        new Thread(() -> {
            fox.setSpace(2);
        }).start();


        //------------------------------------------------------------------------------------------
        //如果不能使用volatile 可以使用jdk 原子类
        LongAdder longAdder = new LongAdder(); //线程安全的原子类
        AtomicLong atomicLong = new AtomicLong(); //线程安全的原子类

        atomicLong.compareAndSet(0,1); //如果当前是0才会修改

        longAdder.increment(); //自增

        //AtomicReference：原子更新引用类型；

        //    AtomicStampedReference：原子更新带有版本号的引用类型；

        //   AtomicMarkableReference：原子更新带有标记位的引用类型。可以原子更新一个布尔类型的标记为和引用类型；

    }

    @Data
    public static class Fox {
        protected volatile Integer space = 0;

        public void run(){
            while(space==0){
                System.out.println("run run run==");
            }
        }

    }
}

