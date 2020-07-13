package com.ciel.scatquick.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.lucene.util.NamedThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ThreadPoolAndAtomClass {

    public volatile static int age = 1;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        //线程工厂
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("task-pool-%d").build();
        //线程工厂
        ThreadFactory factory = new NamedThreadFactory("task-pool");

        //cpu 核心数 适合cpu密集型
        int cros = Runtime.getRuntime().availableProcessors();

        //线程池
        ThreadPoolExecutor threadPool =
                new ThreadPoolExecutor(cros+1, //核心线程
                        cros+1,  //最大线程
                        2, TimeUnit.SECONDS, //超出核心线程的超时时间 会释放
                        new LinkedBlockingDeque<>(1024), //阻塞队列
                        threadFactory //线程工程
                      //  ,new ThreadPoolExecutor.AbortPolicy() //拒绝策略 抛出异常
                     //   ,new ThreadPoolExecutor.CallerRunsPolicy() //main 线程处理
                     //   ,new ThreadPoolExecutor.DiscardPolicy() //队列满了不出异常 丢弃任务 没有异常
                        ,new ThreadPoolExecutor.DiscardOldestPolicy()  //队列满了和第一个线程竞争 失败丢弃任务 没有异常
                 );

        threadPool.execute(() -> { //execute会直接抛出任务执行时的异常;
                System.out.println("execute");
        });

        Future<?> submit = threadPool.submit(() -> { //submit会吃掉异常，可通过Future的get方法将任务执行时的异常重新抛出
                System.out.println("submit");
        });

        Future<String> submit1 = threadPool.submit(() -> { //可以执行Callable 任务 ,获取返回值
            System.out.println("submit");
            return "xiapeixin";
        });

        String s = submit1.get(); //返回执行结果 ,可能阻塞
//        get(long timeout, TimeUnit unit)：获取结果，但只等待指定的时间；
//        cancel(boolean mayInterruptIfRunning)：取消当前任务；
//        isDone()：判断任务是否已完成

        threadPool.shutdown();  // shutdown()方法关闭线程池的时候，它会等待正在执行的任务先完成，然后再关闭。
        //shutdownNow()会立刻停止正在执行的任务，
        //awaitTermination(long timeout, TimeUnit unit)则会等待指定的时间让线程池关闭。


//----------------------------------------------------------------------------------------
        // 非阻塞的Future
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "asyn task");
        cf.get();

        // 如果执行成功:
        CompletableFuture<Void> success = cf.thenAccept((result) -> {
            System.out.println("price: " + result);
        });

        // 如果执行异常:
        CompletableFuture<String> exce = cf.exceptionally((e) -> {
            e.printStackTrace();
            return "null";
        });


        //串行执行第二个任务,根据第一个任务的结果
        CompletableFuture<String> cf2 = cf.thenApplyAsync((x) -> "asyn task2");

        // cfFetch成功后打印结果:
        CompletableFuture<Void> success2 = cf2.thenAccept((result) -> {
            System.out.println("price: " + result);
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //  并行执行 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> "aa");
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> "bb");

        // 用anyOf合并为一个新的CompletableFuture:
        CompletableFuture<Object> objectCompletableFuture =
                CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);

        // 两个CompletableFuture执行异步查询:
        CompletableFuture<String> cfFetchFromSina = objectCompletableFuture.thenApplyAsync((code) -> code+"cc");
        CompletableFuture<String> cfFetchFrom163 = objectCompletableFuture.thenApplyAsync((code) -> code+"dd");

        //anyOf()可以实现“任意个CompletableFuture只要一个成功”，allOf()可以实现“所有CompletableFuture都必须成功”
        CompletableFuture<Object> any =
                CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);

        CompletableFuture<Void> all
                = CompletableFuture.allOf(cfFetchFromSina, cfFetchFrom163);


        // 最终结果:
        any.thenAccept((result) -> {
            System.out.println("price: " + result);
        });

        all.thenAccept(result -> {
            System.out.println("price: " + result);
        });
//----------------------------------------------------------------------------------------

        //放入ScheduledThreadPool的任务可以定期反复执行。
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(cros);
        // 1秒后执行一次性任务:
        ses.schedule(() -> System.out.println("bb"),5, TimeUnit.SECONDS);
        //1秒后执行,每一秒执行一次
        ses.scheduleAtFixedRate(() -> System.out.println("aa"), 1, 1, TimeUnit.SECONDS);
            //1秒后执行,每一秒执行一次
        ses. scheduleWithFixedDelay(() -> System.out.println("cc"), 1, 1, TimeUnit.SECONDS);

       // 注意FixedRate和FixedDelay的区别。
        //FixedRate是指任务总是以固定时间间隔触发，不管任务执行多长时间：
        //而FixedDelay是指，上一次任务执行完毕后，等待固定的时间间隔，再执行下一次任务

//------------------------------------------------------------------------------------------
        LongAdder longAdder = new LongAdder(); //线程安全的原子类
        AtomicLong atomicLong = new AtomicLong(); //线程安全的原子类

        atomicLong.compareAndSet(0,1); //如果当前是0才会修改

        longAdder.increment(); //自增

        //AtomicReference：原子更新引用类型；

    //    AtomicStampedReference：原子更新带有版本号的引用类型；

     //   AtomicMarkableReference：原子更新带有标记位的引用类型。可以原子更新一个布尔类型的标记为和引用类型；

    }
}
