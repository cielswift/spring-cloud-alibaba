package com.ciel.scatquick.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scaentity.entity.ScaUser;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.lucene.util.NamedThreadFactory;
import org.apache.shardingsphere.core.yaml.swapper.impl.ShadowRuleConfigurationYamlSwapper;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ThreadPoolCompletableFuture {

    public volatile static int age = 1;

    /**
     * CPU密集型任务应该尽可能小的线程，如配置cpu数量+1个线程的线程池
     * 由于IO密集型任务并不是一直在执行任务，不能让cpu闲着，则应配置尽可能多的线程，如：cup数量*2
     */
    public static final int cros = Runtime.getRuntime().availableProcessors();

    //线程工厂 ,线程名称格式化
    public static final ThreadFactory threadFactory =
            new ThreadFactoryBuilder().setNameFormat("xiaPeiXin-pool-%d").build();

    //线程工厂 ,线程名称格式化
    public static final ThreadFactory factory = new NamedThreadFactory("ciel-pool-%d");

    /**
     * 线程池
     * 不要所有业务共用一个线程池，因为，一旦有任务执行一些很慢的 I/O 操作，
     * 就会导致线程池中所有线程都阻塞在 I/O 操作上，从而造成线程饥饿，进而影响整个系统的性能
     */
    public static final ThreadPoolExecutor CPU_THREAD_POOL = new ThreadPoolExecutor(
            /**
             * 等到工作的线程数大于核心线程数时就不会在创建了
             */
            cros + 1, //核心线程
            /**
             * //线程池允许创建的最大线程数。如果队列满了，并且以创建的线程数小于最大线程数，
             * 则线程池会再创建新的线程执行任务。如果我们使用了无界队列，那么所有的任务会加入队列，这个参数就没有什么效果了
             */
            cros + 1,
            /**
             * 线程池的工作线程空闲后，保持存活的时间。如果没有任务处理了，有些线程会空闲，
             * 空闲的时间超过了这个值，会被回收掉。如果任务很多，并且每个任务的执行时间比较短，
             * 避免线程重复创建和回收，可以调大这个时间，提高线程的利用率
             */
            2, TimeUnit.SECONDS,
            /**
             * ArrayBlockingQueue是一个基于数组结构的有界阻塞队列，此队列按照先进先出原则对元素进行排序
             * LinkedBlockingQueue：是一个基于链表结构的阻塞队列，此队列按照先进先出排序元素，吞吐量通常要高于ArrayBlockingQueue
             * SynchronousQueue ：一个不存储元素的阻塞队列，每个插入操作必须等到另外一个线程调用移除操作，否则插入操作一直处理阻塞状态，吞吐量通常要高于LinkedBlockingQueue
             * PriorityBlockingQueue：优先级队列，进入队列的元素按照优先级会进行排序
             */
            new LinkedBlockingDeque<>(1 << 12), //阻塞队列 asa 4096
            threadFactory //线程工程

            /**
             * 线程池对拒绝任务(无限程可用)的处理策略
             * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
             * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
             * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程） 没有异常
             * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务,如果执行器已关闭,则丢弃.
             *
             * 在使用CompletableFuture的时候线程池拒绝策略最好使用AbortPolicy，如果线程池满了直接抛出异常中断主线程，达到快速失败的效果
             */
            , new ThreadPoolExecutor.DiscardOldestPolicy()) {

        /**
         * beforeExecute：任务执行之前调用的方法，有2个参数，第1个参数是执行任务的线程，第2个参数是任务
         */
        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println("任务执行之前调用");
        }

        /**
         * afterExecute：任务执行完成之后调用的方法，2个参数，第1个参数表示任务，第2个参数表示任务执行时的异常信息，
         * 如果无异常，第二个参数为null
         */
        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            System.out.println("任务执行完成后调用");
        }

        /**
         * terminated：线程池最终关闭之后调用的方法。所有的工作线程都退出了，最终线程池会退出，退出时调用该方法
         */
        @Override
        protected void terminated() {
            System.out.println("线程池最终关闭之后调用的方");
        }
    };

    /**
     * 线程池操作
     */
    public static void threadPoll() throws ExecutionException, InterruptedException {


        CPU_THREAD_POOL.prestartAllCoreThreads();//池会提前把核心线程都创造好

        Thread.currentThread().interrupt();
        /*
        调用interrupt()方法之后，再调用sleep方法将会抛出 InterruptedException异常
         并且线程的中断标志会被清除（置为false）
         */
        //Thread.sleep(5000);
        if (Thread.currentThread().isInterrupted()) {
            System.err.println("WARNING : CURRENT THREAD IS INTERRUPTED!");
        }
        /*
            设置为守护线程;当所有的用户线程执行完后，不管守护线程是否结束，都会自动退出
            需要在start()方法之前进行;
            tart()方法之后执行的，执行会报异常
         */
        Thread.currentThread().setDaemon(true);
        Thread.currentThread().setPriority(9); //设置线程优先级
//////////////////////////////////////////////////////////////////////////////////////////////////////

        FutureTask<String> task = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + ":" + "开始烧开水...");
            Thread.sleep(2000);  // 模拟烧开水耗时
            System.out.println(Thread.currentThread().getName() + ":" + "开水已经烧好了...");
            return "开水";
        });

        CPU_THREAD_POOL.submit(task); //FutureTask是
        System.out.println(Thread.currentThread().getName() + "此时我们可以干点别的事情（比如准备火锅食材）...");

        // 开水已经稍好，我们取得烧好的开水
        System.out.println("得到了:" + task.get());
        /////////////////////////////////////////////////////////////////////////////////////////////////////

        CPU_THREAD_POOL.execute(() -> { //execute会直接抛出任务执行时的异常;
            System.out.println("execute");
        });

        Future<?> submit = CPU_THREAD_POOL.submit(() -> { //submit会吃掉异常，可通过Future的get方法将任务执行时的异常重新抛出
            System.out.println("submit");
        });

        Future<String> submit1 = CPU_THREAD_POOL.submit(() -> { //可以执行Callable 任务 ,获取返回值
            System.out.println("submit");
            return "xiapeixin";
        });

        String s = submit1.get(); //返回执行结果 ,阻塞
//        get(long timeout, TimeUnit unit)：获取结果，但只等待指定的时间；
//        cancel(boolean mayInterruptIfRunning)：取消当前任务； //是否给任务发送中断信号
//        isCancelled：用来判断任务是否被取消
//        isDone()：判断任务是否已完成

        CPU_THREAD_POOL.shutdown();  // shutdown()方法关闭线程池的时候，它会等待正在执行的任务先完成，然后再关闭。
        //shutdownNow()会立刻停止正在执行的任务;
        //CPU_THREAD_POOL.isShutdown(); 是否关闭 是否终止
        //CPU_THREAD_POOL.isTerminated(); 是否关闭 是否终止

        //awaitTermination(long timeout, TimeUnit unit)则会等待指定的时间让线程池关闭。
    }

    /**
     * future操作
     */
    public static void future() throws ExecutionException, InterruptedException {

        // 非阻塞的Future
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> "young girl"); //异步计算返回结果
        System.out.println(supplyAsync.get());

        //异步计算 不能返回结果
        CompletableFuture.runAsync(() -> System.out.println(Thread.currentThread().getName() + ": america girl"), CPU_THREAD_POOL);

        // 如果执行成功 传入一个回调函数, 可以返回一个新的结果
        CompletableFuture.supplyAsync(() -> "fuck young girl").thenApply(String::toUpperCase);

        // 如果执行成功 传入一个回调函数, 不能收到结果 也不能返回结果
        CompletableFuture.supplyAsync(() -> "fuck young girl").thenRun(() -> System.out.println("wy"));

        // 如果执行成功 传入一个回调函数;不想从回调函数中返回任何结果
        CompletableFuture<Void> success = supplyAsync.thenAccept((result) -> {
            System.out.println("the result is : " + result);
        });
        //每个提供回调方法的函数都有两个异步（Async）变体，异步就是另外起一个线程 ,可以用自己的线程池
        CompletableFuture.supplyAsync(() -> "fuck young girl").thenApplyAsync(String::toUpperCase, CPU_THREAD_POOL);

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
                .thenCombine(CompletableFuture.supplyAsync(() -> " -> of america"), (a, b) -> a + b);

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

    public static void shel() {
        //----------------------------------------------------------------------------------------
        //放入ScheduledThreadPool的任务可以定期反复执行。
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(cros);
        // 1秒后执行一次性任务:
        ScheduledFuture<?> bb = ses.schedule(() -> System.out.println("bb"), 5, TimeUnit.SECONDS);
        //1秒后执行,每一秒执行一次
        ScheduledFuture<?> aa = ses.scheduleAtFixedRate(() -> System.out.println("aa"), 1, 1, TimeUnit.SECONDS);
        //1秒后执行,每一秒执行一次
        ses.scheduleWithFixedDelay(() -> System.out.println("cc"), 1, 1, TimeUnit.SECONDS);

        // 注意FixedRate和FixedDelay的区别。
        //FixedRate是指任务总是以固定时间间隔触发，不管任务执行多长时间：
        //而FixedDelay是指，上一次任务执行完毕后，等待固定的时间间隔，再执行下一次任务

        aa.cancel(true); //是否给任务发送中断信号
        aa.isDone();


    }

    public static void que() throws InterruptedException, ExecutionException {

        /**
         * CompletionService相当于一个执行任务的服务，通过submit丢任务给这个服务，服务内部去执行任务，
         * 可以通过服务提供的一些方法获取服务中已经完成的任务
         * submit 用于向服务中提交有返回结果的任务，并返回Future对象
         * take 从服务中返回并移除一个已经完成的任务，如果获取不到，会一致阻塞到有返回值为止。此方法会响应线程中断
         * poll 从服务中返回并移除一个已经完成的任务，如果内部没有已经完成的任务，则返回空，此方法会立即响应
         * poll(..) 尝试在指定的时间内从服务中返回并移除一个已经完成的任务，超时还是没有获取到已完成的任务则返回空。此方法会响应线程中断
         *
         * ExecutorCompletionService内部有个阻塞队列，任意一个任务完成之后，会将任务的执行结果（Future类型）放入阻塞队列中，
         * 然后其他线程可以调用它take、poll方法从这个阻塞队列中获取一个已经完成的任务，
         * 获取任务返回结果的顺序和任务执行完成的先后顺序一致，所以最先完成的任务会先返回
         *
         * 执行一批任务，然后消费执行结果
         */
        ExecutorCompletionService<String> completionService =
                new ExecutorCompletionService(CPU_THREAD_POOL);

        for (int i=0; i<100; i++){
            completionService.submit(() -> {
                System.out.println(String.format("当前线程: %s",Thread.currentThread().getName()));
                return String.valueOf(System.currentTimeMillis());
            });
        }

        while (true){

            Future<String> take = completionService.take();

            System.out.println(take.get());
        }

        //必须等待所有的任务执行完成后统一返回
        //CPU_THREAD_POOL.invokeAll()
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        //future();

        //shel();

        que();

    }
}
