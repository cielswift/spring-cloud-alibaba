package com.ciel.scatquick.thread;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
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

        AtomicLong atomicLong = new AtomicLong(); //线程安全的原子类
        /**
         * 是AtomicLong的增强版
         */
        LongAdder longAdder = new LongAdder(); //线程安全的原子类
        /**
         * LongAccumulator是LongAdder的功能增强版。
         * LongAdder的API只有对数值的加减，而LongAccumulator提供了自定义的函数操作，其构造函数如下
         *
         * accumulatorFunction：需要执行的二元函数（接收2个long作为形参，并返回1个long）
         *   identity：初始值
         */
        LongAccumulator accumulator = new LongAccumulator((x,y) -> {
            return x+y;
        }, 0L);

        accumulator.accumulate(5); //更新为5
        accumulator.reset();
        System.out.println(accumulator);


      //  public final int get() //获取当前的值
      //  public final int getAndSet(int newValue)//获取当前的值，并设置新的值
      //  public final int getAndIncrement()//获取当前的值，并自增
       // public final int getAndDecrement() //获取当前的值，并自减
      //  public final int getAndAdd(int delta) //获取当前的值，并加上预期的值
      //  boolean compareAndSet(int expect, int update) //如果输入的数值等于预期值，则以原子方式将该值设置为输入值（update）
      //  public final void lazySet(int newValue)//最终设置为newValue,使用 lazySet 设置之后可能导致其他线程在之后的一小段时间内还是可以读到旧的值。

        atomicLong.compareAndSet(0,1); //如果当前是0才会修改

        longAdder.increment(); //自增
////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        AtomicIntegerArray：整形数组原子操作类
//        AtomicLongArray：长整形数组原子操作类
//        AtomicReferenceArray ：引用类型数组原子操作类

//        public final int get(int i) //获取 index=i 位置元素的值
//        public final int getAndSet(int i, int newValue)//返回 index=i 位置的当前的值，并将其设置为新值：newValue
//        public final int getAndIncrement(int i)//获取 index=i 位置元素的值，并让该位置的元素自增
//        public final int getAndDecrement(int i) //获取 index=i 位置元素的值，并让该位置的元素自减
//        public final int getAndAdd(int delta) //获取 index=i 位置元素的值，并加上预期的值
//        boolean compareAndSet(int expect, int update) //如果输入的数值等于预期值，则以原子方式将 index=i 位置的元素值设置为输入值（update）
//        public final void lazySet(int i, int newValue)//最终 将index=i 位置的元素设置为newValue,使用 lazySet 设置之后可能导致其他线程在之后的一小段时间内还是可以读到旧的值。

//        AtomicReference：引用类型原子类
//        AtomicStampedRerence：原子更新引用类型里的字段原子类
//        AtomicMarkableReference ：原子更新带有标记位的引用类型 可以原子更新一个布尔类型的标记为和引用类型；
//        AtomicStampedReference：原子更新带有版本号的引用类型；

//        //比较设置，参数依次为：期望值、写入新值、期望时间戳、新时间戳
//         boolean compareAndSet(V expectedReference, V newReference, int expectedStamp, int newStamp);
//        //获得当前对象引用
//         V getReference();
//        //获得当前时间戳
//         int getStamp();
//            //设置当前对象引用和时间戳
//         void set(V newReference, int newStamp);
///////////////////////////////////////////////////////////////////////////////////////////

//        AtomicIntegerFieldUpdater：原子更新整形字段的值
//        AtomicLongFieldUpdater：原子更新长整形字段的值
//        AtomicReferenceFieldUpdater ：原子更新应用类型字段的值

//        第一步，因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须使用静态方法
//        newUpdater()创建一个更新器，并且需要设置想要更新的类和属性。
//
//        第二步，更新的对象属性必须使用 public volatile 修饰符

//        AtomicReferenceFieldUpdater<ScaGirls, String> name =
//                AtomicReferenceFieldUpdater.newUpdater(ScaGirls.class, String.class, "name");


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

