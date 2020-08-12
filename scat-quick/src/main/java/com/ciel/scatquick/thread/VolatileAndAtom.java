package com.ciel.scatquick.thread;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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

        //关于原子类操作，都位于java.util.concurrent.atomic包中
        LongAdder longAdder = new LongAdder(); //线程安全的原子类

        AtomicLong atomicLong = new AtomicLong(); //线程安全的原子类

      //  public final int get() //获取当前的值
      //  public final int getAndSet(int newValue)//获取当前的值，并设置新的值
      //  public final int getAndIncrement()//获取当前的值，并自增
       // public final int getAndDecrement() //获取当前的值，并自减
      //  public final int getAndAdd(int delta) //获取当前的值，并加上预期的值
      //  boolean compareAndSet(int expect, int update) //如果输入的数值等于预期值，则以原子方式将该值设置为输入值（update）
      //  public final void lazySet(int newValue)//最终设置为newValue,使用 lazySet 设置之后可能导致其他线程在之后的一小段时间内还是可以读到旧的值。


        atomicLong.compareAndSet(0,1); //如果当前是0才会修改


        longAdder.increment(); //自增

        //AtomicReference：原子更新引用类型；

        //    AtomicStampedReference：原子更新带有版本号的引用类型；

        //   AtomicMarkableReference：原子更新带有标记位的引用类型。可以原子更新一个布尔类型的标记为和引用类型；

    }

    @Data
    public static class Fox {
        protected volatile Integer space = 0;

        @SneakyThrows
        public void run(){
            while(true){
                Thread.sleep(200);
                if(space ==2){
                    System.out.println(Thread.currentThread().getName()+":run run run 222==");
                }else{
                    System.out.println(Thread.currentThread().getName()+":run run run 000==");
                }

            }
        }

    }
}

