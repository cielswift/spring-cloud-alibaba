package com.ciel.scatquick.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.lucene.util.NamedThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ThreadPoolCompletableFuture {

    public volatile static int age = 1;

    //获取计算机cpu核心数 ,适合cpu密集型
    public static final int cros = Runtime.getRuntime().availableProcessors();

    //线程工厂 ,线程名称格式化
    public static final ThreadFactory threadFactory =
            new ThreadFactoryBuilder().setNameFormat("xiapeixin-pool-%d").build();

    //线程工厂 ,线程名称格式化
    public static final ThreadFactory factory = new NamedThreadFactory("ciel-pool-%d");

    /**
     * 不要所有业务共用一个线程池，因为，一旦有任务执行一些很慢的 I/O 操作，
     * 就会导致线程池中所有线程都阻塞在 I/O 操作上，从而造成线程饥饿，进而影响整个系统的性能
     */
    //线程池
    public static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            cros+1, //核心线程
            cros+1,  //最大线程
            2, TimeUnit.SECONDS, //超出核心线程的超时时间 会释放
            new LinkedBlockingDeque<>(1024), //阻塞队列
            threadFactory //线程工程
            //  ,new ThreadPoolExecutor.AbortPolicy() //拒绝策略 抛出异常
            //   ,new ThreadPoolExecutor.CallerRunsPolicy() //main 线程处理
            //   ,new ThreadPoolExecutor.DiscardPolicy() //队列满了不出异常 丢弃任务 没有异常
            ,new ThreadPoolExecutor.DiscardOldestPolicy()  //队列满了和第一个线程竞争 失败丢弃任务 没有异常
    );

    public static void threadPoll() throws ExecutionException, InterruptedException {

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
    }

    public static void future() throws ExecutionException, InterruptedException {

        // 非阻塞的Future
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> "young girl"); //异步计算返回结果
        System.out.println(supplyAsync.get());

        //异步计算 不能返回结果
        CompletableFuture.runAsync(() -> System.out.println(Thread.currentThread().getName() + ": america girl"), threadPool);

        // 如果执行成功 传入一个回调函数, 可以返回一个新的结果
        CompletableFuture.supplyAsync(() -> "fuck young girl").thenApply(r -> r.toUpperCase());

        // 如果执行成功 传入一个回调函数, 不能收到结果 也不能返回结果
        CompletableFuture.supplyAsync(() -> "fuck young girl").thenRun(() -> System.out.println("wy"));

        // 如果执行成功 传入一个回调函数;不想从回调函数中返回任何结果
        CompletableFuture<Void> success = supplyAsync.thenAccept((result) -> {
            System.out.println("the result is : " + result);
        });
        //每个提供回调方法的函数都有两个异步（Async）变体，异步就是另外起一个线程 ,可以用自己的线程池
        CompletableFuture.supplyAsync(() -> "fuck young girl").thenApplyAsync(r -> r.toUpperCase(),threadPool);

        // 如果执行异常, 起到 catch的作用
        CompletableFuture<String> exce = supplyAsync.exceptionally((e) -> {
            e.printStackTrace();
            return "null";
        });

        //handle 就可以起到 finally 的作用，对上述程序做一个小小的更改， handle 接受两个参数，一个是正常返回值，一个是异常
        CompletableFuture<String> handle = supplyAsync.handle((res, ex) -> {
            return "aa";
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //thenCompose（）用来连接两个CompletableFuture，返回值是新的CompletableFuture
        CompletableFuture<String> thenCompose = CompletableFuture.supplyAsync(() -> "girls").thenCompose(r -> {
            return CompletableFuture.supplyAsync(() -> {
                return r + "-> of america";
            });
        });
        System.out.println(thenCompose.get());

        //如果要聚合两个独立 Future 的结果，那么 thenCombine 就会派上用场了
        CompletableFuture<String> thenCombine = CompletableFuture.supplyAsync(() -> "girls")
                .thenCombine(CompletableFuture.supplyAsync(() -> " -> of america"),(a,b) -> a + b);

        System.out.println(thenCombine.get());

        // allOf 聚合多个
        CompletableFuture<Void> allOf = CompletableFuture.allOf(CompletableFuture.supplyAsync(() -> "girls"),
                CompletableFuture.supplyAsync(() -> "-> of america"),
                CompletableFuture.supplyAsync(() -> "-> make love"));
        System.out.println(allOf.get());

        //anyOf()可以实现“任意个CompletableFuture只要一个成功”，allOf()可以实现“所有CompletableFuture都必须成功”
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(CompletableFuture.supplyAsync(() -> "girls"),
                CompletableFuture.supplyAsync(() -> "-> of america"));
        System.out.println(anyOf.get());

    }

    public static void shel(){
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
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        future();

        shel();
    }
}
