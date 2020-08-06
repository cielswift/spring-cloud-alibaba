package com.ciel.scatquick.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorDemo {

    public static void main(String[] args) {

        //1 重试策略：初试时间为1s 重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        //2 通过工厂创建连接
        CuratorFramework cf = CuratorFrameworkFactory.builder()
                .connectString(ZookClient.ADDRESS)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        //3 开启连接
        cf.start();
        InterProcessMutex lock = new InterProcessMutex(cf,"/cur-lock");

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "尝试获取锁。。。");
                try {
                //可重入
                    lock.acquire();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "获得锁");

                try {
                    System.out.println(Thread.currentThread().getName() + "执行中。。。。。。。。。。");
                    Thread.sleep(1000 * 60 * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "执行完，释放锁");
                try {
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "释放成功");
            },"线程"+i).start();
        }
    }


}
