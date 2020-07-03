package com.ciel.scatquick.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class FutureTaskCl {

    public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;
    static {
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(64, 64, 2, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(1024),
                new ThreadFactoryBuilder().setNameFormat("CIEL-JDK-POOL-%d").build(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<String> task = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + ":" + "开始烧开水...");
            // 模拟烧开水耗时
            Thread.sleep(200);
            System.out.println(Thread.currentThread().getName() + ":" + "开水已经烧好了...");
            return "开水";
        });

        THREAD_POOL_EXECUTOR.submit(task);

        // do other thing
        System.out.println(Thread.currentThread().getName() + ":" + " 此时开启了一个线程执行future的逻辑（烧开水），此时我们可以干点别的事情（比如准备火锅食材）...");
        // 模拟准备火锅食材耗时
        Thread.sleep(3000);
        System.out.println(Thread.currentThread().getName() + ":" + "火锅食材准备好了");
        String shicai = "火锅食材";

        // 开水已经稍好，我们取得烧好的开水
        String boilWater = task.get();

        System.out.println(boilWater);
    }
}
