package com.ciel.scatquick.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.lucene.util.NamedThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;

public class ThreadPoolAndAtomClass {

    public volatile static int age = 1;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //线程池
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("task-pool-%d").build();

        ThreadFactory factory = new NamedThreadFactory("task-pool");

        //cpu 核心数 适合cpu密集型
        int cros = Runtime.getRuntime().availableProcessors();

        ThreadPoolExecutor threadPool =
                new ThreadPoolExecutor(cros/2, //核心线程
                        cros,  //最大线程
                        5, TimeUnit.SECONDS, //超出核心线程的超时时间 会释放
                        new LinkedBlockingDeque<>(1024), //阻塞队列
                        threadFactory //线程工程
                      //  ,new ThreadPoolExecutor.AbortPolicy() //拒绝策略 抛出异常
                     //   ,new ThreadPoolExecutor.CallerRunsPolicy() //main 线程处理
                     //   ,new ThreadPoolExecutor.DiscardPolicy() //队列满了不出异常 丢弃任务 没有异常
                        ,new ThreadPoolExecutor.DiscardOldestPolicy()  //队列满了和第一个线程竞争 失败丢弃任务 没有异常
                 );

        // shutdown()方法关闭线程池的时候，它会等待正在执行的任务先完成，然后再关闭。
        //shutdownNow()会立刻停止正在执行的任务，
        //awaitTermination()则会等待指定的时间让线程池关闭。

        threadPool.execute(() -> System.out.println("aaa")); //会直接抛出任务执行时的异常
        Future<String> future = threadPool.submit(() -> "bbb");//submit会吃掉异常

        future.get(); //返回执行结果 bbb ,可能阻塞
//        get()：获取结果（可能会等待）
//        get(long timeout, TimeUnit unit)：获取结果，但只等待指定的时间；
//        cancel(boolean mayInterruptIfRunning)：取消当前任务；
//        isDone()：判断任务是否已完成

//----------------------------------------------------------------------------------------
        // 非阻塞的Future
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "asyn task");
        cf.get();

        // 如果执行成功:
        cf.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 如果执行异常:
        cf.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });

        //串行执行第二个任务,根据第一个任务的结果
        CompletableFuture<String> cf2 = cf.thenApplyAsync((x) -> "asyn task2");

        // cfFetch成功后打印结果:
        cf2.thenAccept((result) -> {
            System.out.println("price: " + result);
        });

        //并行执行
        // 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> "aa");
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> "bb");

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);

        // 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfFetchFromSina = objectCompletableFuture.thenApplyAsync((code) -> code+"cc");
        CompletableFuture<String> cfFetchFrom163 = objectCompletableFuture.thenApplyAsync((code) -> code+"dd");

        //anyOf()可以实现“任意个CompletableFuture只要一个成功”，allOf()可以实现“所有CompletableFuture都必须成功”

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> objectCompletableFuture1 = CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);

        // 最终结果:
        objectCompletableFuture1.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
//----------------------------------------------------------------------------------------

        //放入ScheduledThreadPool的任务可以定期反复执行。
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);
        // 1秒后执行一次性任务:
        ses.schedule(() -> System.out.println("bb"),5, TimeUnit.SECONDS);
        //1秒后执行,每一秒执行一次
        ses.scheduleAtFixedRate(() -> System.out.println("aa"), 1, 1, TimeUnit.SECONDS);
            //1秒后执行,每一秒执行一次
        ses. scheduleWithFixedDelay(() -> System.out.println("cc"), 1, 1, TimeUnit.SECONDS);

       // 注意FixedRate和FixedDelay的区别。
        //FixedRate是指任务总是以固定时间间隔触发，不管任务执行多长时间：
        //而FixedDelay是指，上一次任务执行完毕后，等待固定的时间间隔，再执行下一次任务

        LongAdder longAdder = new LongAdder();

        AtomicLong atomicLong = new AtomicLong();
        atomicLong.compareAndSet(0,1); //如果当前是0才会修改

        AtomicStampedReference stampedReference =
                new AtomicStampedReference<Integer>(10,1); //初始10 版本号1
        int stamp = stampedReference.getStamp(); //获得版本号
        stampedReference.compareAndSet(10,11,stamp,stamp+=1);

        System.out.println(stampedReference.getStamp());
        System.out.println(stampedReference.getReference());

        //自旋锁
        AtomicReference<Thread> atomicReference = new AtomicReference<>();

        System.out.println();
    }
}
