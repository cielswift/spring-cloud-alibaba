package com.ciel.scatquick.thread;

import com.ciel.scaapi.util.Faster;
import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Brokint {

    public static void limit() {
        /**
         * RateLimiter.create(5)创建QPS为5的限流对象，后面又调用rateLimiter.setRate(10);将速率设为10，
         * 输出中分2段，第一段每次输出相隔200毫秒，第二段每次输出相隔100毫秒，可以非常精准的控制系统的QPS
         */
        RateLimiter rateLimiter = RateLimiter.create(5);//设置QPS为5

        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire();
            System.out.println(System.currentTimeMillis());
        }

        System.out.println("----------");

        //可以随时调整速率，我们将qps调整为10
        rateLimiter.setRate(10);

        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire();
            System.out.println(System.currentTimeMillis());
        }

    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // limit();

        ListeningExecutorService executorService =
                MoreExecutors.listeningDecorator(ThreadPoolCompletableFuture.CPU_THREAD_POOL);

        ListenableFuture<?> listenableFuture = executorService.submit(() -> {
            System.out.println("任务+++++++++++++执行");
        });

        listenableFuture.addListener(() -> {
            System.out.println("任务++++++++++回调");
        }, ThreadPoolCompletableFuture.CPU_THREAD_POOL);


        ListenableFuture<String> future = executorService.submit(() -> {
            System.out.println("带返回值");
            return "AAA";
        });

        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String result) {
                System.out.println("成功");
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("失败");
            }
        }, ThreadPoolCompletableFuture.CPU_THREAD_POOL);


        ////获取一批任务的执行结果
        List<String> strings = Futures.allAsList(Faster.toList(future)).get();
        System.out.println(strings);


        //一批任务执行回调
        ListenableFuture<List<String>> listListenableFuture = Futures.allAsList(Faster.toList(future));

        //一批任务添加回调
        Futures.addCallback(listListenableFuture,new FutureCallback<List<String>>(){

            @Override
            public void onSuccess(@Nullable List<String> result) {
                System.out.println("成功");
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("失败");
            }

        },ThreadPoolCompletableFuture.CPU_THREAD_POOL);

    }
}
