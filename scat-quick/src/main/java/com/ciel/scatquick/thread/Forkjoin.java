package com.ciel.scatquick.thread;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Forkjoin {
    public static void main(String[] args) throws Exception {
        // 创建2000个随机数组成的数组:
        long[] array = new long[20000];
        long expectedSum = 0;
        for (int i = 0; i < array.length; i++) {
            array[i] = new Random().nextInt(1000);
            expectedSum += array[i];
        }
        System.out.println("合计: " + expectedSum);

        new Thread(() -> {
            //
            long start = System.currentTimeMillis();
            long su1 = 0;
            for (long l : array) {
                su1+=l;
            }
            System.out.println(String.format("普通计算的值为: %s ;普通计算消耗时间: %s",su1,System.currentTimeMillis()-start));
            //
        }).start();


        new Thread(() -> {
            long start = System.currentTimeMillis();
            long l = Arrays.stream(array).parallel().reduce((x, y) -> x + y).orElse(0);
            System.out.println(String.format("并行流计算的值为: %s ;并行流计算消耗时间: %s",l,System.currentTimeMillis()-start));

        }).start();


        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        long startTime = System.currentTimeMillis();
        Long result = ForkJoinPool.commonPool().invoke(task);

//        ForkJoinPool.commonPool().execute(task);//同步
//        ForkJoinTask<Long> submit = ForkJoinPool.commonPool().submit(task);//异步
//        submit.get();

        System.out.println("Fork/join sum: " + result + " in " + (System.currentTimeMillis() - startTime) + " ms.");
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class SumTask extends RecursiveTask<Long> { //ForkJoinTask 任务
        static final int THRESHOLD = 500;
        long[] array;
        int start;
        int end;

        SumTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        //计算和 返回值 类型是 泛型
        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                // 如果任务足够小,直接计算:
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += this.array[i];
                }
                return sum;
            }

            // 任务太大,一分为二:
            int middle = (end + start) / 2;

            System.out.println(String.format("split %d~%d ==> %d~%d, %d~%d", start, end, start, middle, middle, end));

            SumTask subtask1 = new SumTask(this.array, start, middle);
            SumTask subtask2 = new SumTask(this.array, middle, end);

            invokeAll(subtask1, subtask2);  // // invokeAll会并行运行两个子任务:

            //ForkJoinTask<Long> fork = subtask1.fork();
            //ForkJoinTask<Long> fork1 = subtask2.fork();

            Long subresult1 = subtask1.join();
            Long subresult2 = subtask2.join();

            Long result = subresult1 + subresult2;
            System.out.println("result = " + subresult1 + " + " + subresult2 + " ==> " + result);
            return result;
        }
    }
}


