package com.ciel.scatquick.thread;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class Brokint {

    public static class BucketLimit {
        static AtomicInteger threadNum = new AtomicInteger(1);
        //容量
        private int capcity;
        //流速
        private int flowRate;
        //流速时间单位
        private TimeUnit flowRateUnit;
        private BlockingQueue<Node> queue;
        //漏桶流出的任务时间间隔（纳秒）
        private long flowRateNanosTime;

        public BucketLimit(int capcity, int flowRate, TimeUnit flowRateUnit) {
            this.capcity = capcity;
            this.flowRate = flowRate;
            this.flowRateUnit = flowRateUnit;
            this.bucketThreadWork();
        }

        //漏桶线程
        public void bucketThreadWork() {
            this.queue = new ArrayBlockingQueue<Node>(capcity);

            //漏桶流出的任务时间间隔（纳秒）
            this.flowRateNanosTime = flowRateUnit.toNanos(1) / flowRate;

            Thread thread = new Thread(this::bucketWork);
            thread.setName("漏桶线程-" + threadNum.getAndIncrement());
            thread.start();
        }

        //漏桶线程开始工作
        public void bucketWork() {
            while (true) {
                Node node = this.queue.poll();
                if (Objects.nonNull(node)) {
                    //唤醒任务线程
                    LockSupport.unpark(node.thread);
                }
                //休眠flowRateNanosTime
                LockSupport.parkNanos(this.flowRateNanosTime);
            }
        }

        //返回一个漏桶
        public static BucketLimit build(int capcity, int flowRate, TimeUnit flowRateUnit) {
            if (capcity < 0 || flowRate < 0) {
                throw new IllegalArgumentException("capcity、flowRate必须大于0！");
            }
            return new BucketLimit(capcity, flowRate, flowRateUnit);
        }

        //当前线程加入漏桶，返回false，表示漏桶已满；true：表示被漏桶限流成功，可以继续处理任务
        public boolean acquire() {
            Thread thread = Thread.currentThread();
            Node node = new Node(thread);
            if (this.queue.offer(node)) {
                LockSupport.park();
                return true;
            }
            return false;
        }

        //漏桶中存放的元素
        class Node {
            private Thread thread;

            public Node(Thread thread) {
                this.thread = thread;
            }
        }
    }

    public static void main(String[] args) {
        BucketLimit bucketLimit = BucketLimit.build(10, 60, TimeUnit.MINUTES);

        for (int i = 0; i < 15; i++) {
            new Thread(() -> {
                boolean acquire = bucketLimit.acquire();
                System.out.println(System.currentTimeMillis() + " " + acquire);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
