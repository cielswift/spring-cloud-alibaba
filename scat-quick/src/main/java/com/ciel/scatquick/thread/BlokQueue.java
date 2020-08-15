package com.ciel.scatquick.thread;

import org.apache.shardingsphere.core.yaml.swapper.impl.ShadowRuleConfigurationYamlSwapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class BlokQueue {

    /**
     * 操作类型	抛出异常| 返回特殊值 | 一直阻塞	 | 	超时退出
     * 插入	add(e)   |	offer(e) | put(e)    | offer(e,timeuout,unit)
     * 移除	remove() |	poll()  | take()     |  poll(timeout,unit)
     * 检查	element()|	peek()  |            |
     *
     * remove,poll方法都移除并返回队列的头部
     * element,peek方法返回队列头部的元素，但不移除
     */


    /**
     * 基于数组的阻塞队列实现，其内部维护一个定长的数组，用于存储队列元素。线程阻塞的实现是通过ReentrantLock来完成的，
     * 数据的插入与取出共用同一个锁，因此ArrayBlockingQueue并不能实现生产、消费同时进行。
     * 而且在创建ArrayBlockingQueue时，我们还可以控制对象的内部锁是否采用公平锁，默认采用非公平锁。
     * <p>
     * new ArrayBlockingQueue<>(1, true); 使用公平锁
     */
    public static BlockingQueue<String> bl1 = new ArrayBlockingQueue<>(1);

    /**
     * 基于单向链表的阻塞队列实现，在初始化LinkedBlockingQueue的时候可以指定大小，也可以不指定，
     * 默认类似一个无限大小的容量（Integer.MAX_VALUE），不指队列容量大小也是会有风险的，
     * 一旦数据生产速度大于消费速度，系统内存将有可能被消耗殆尽，因此要谨慎操作。
     * 另外LinkedBlockingQueue中用于阻塞生产者、消费者的锁是两个（锁分离），因此生产与消费是可以同时进行的
     *
     * //容量为Integer.MAX_VALUE,并将传入的集合丢入队列中
     * public LinkedBlockingQueue(Collection<? extends E> c);
     */
    public static BlockingQueue<String> bl2 = new LinkedBlockingQueue<>(1 << 4);

    /**
     * 一个支持优先级排序的无界阻塞队列，进入队列的元素会按照优先级进行排序
     *
     * //指定队列的初始化容量和放入元素的比较器
     * public PriorityBlockingQueue(int initialCapacity,Comparator<? super E> comparator);
     *
     * 目前推送是按照放入的先后顺序进行发送的，比如有些公告比较紧急，优先级比较高，需要快点发送，怎么搞？
     * 此时PriorityBlockingQueue就派上用场了
     */
    public static BlockingQueue<String> bl3 = new PriorityBlockingQueue(1 << 4);

    /**
     * 同步阻塞队列，SynchronousQueue没有容量，与其他BlockingQueue不同，
     * SynchronousQueue是一个不存储元素的BlockingQueue，每一个put操作必须要等待一个take操作，
     * 否则不能继续添加元素，反之亦然
     */
    public static BlockingQueue<String> bl4 = new SynchronousQueue();

    /**
     * DelayQueue是一个支持延时获取元素的无界阻塞队列，里面的元素全部都是“可延期”的元素，
     * 列头的元素是最先“到期”的元素，如果队列里面没有元素到期，是不能从列头获取元素的，哪怕有元素也不行，
     * 也就是说只有在延迟期到时才能够从队列中取元素
     *
     * 还是推送的业务，有时候我们希望早上9点或者其他指定的时间进行推送，如何实现呢？此时DelayQueue就派上用场了
     *
     * Delayed接口中的getDelay方法：重点在于getDelay方法，这个方法返回剩余的延迟时间
     */
    public static BlockingQueue<Delayed> bl5 = new DelayQueue();

    /**
     * LinkedTransferQueue是基于链表的FIFO无界阻塞队列，它出现在JDK7中，
     * Doug Lea 大神说LinkedTransferQueue是一个聪明的队列，
     * 它是ConcurrentLinkedQueue、SynchronousQueue(公平模式下)、无界的LinkedBlockingQueues等的超集
     * ，LinkedTransferQueue包含了ConcurrentLinkedQueue、SynchronousQueue、LinkedBlockingQueues三种队列的功能
     *
     * LinkedTransferQueue多了tryTransfer和transfer方法。
     *
     * // 如果存在一个消费者已经等待接收它，则立即传送指定的元素，否则返回false，并且不进入队列。
     *     boolean tryTransfer(E e);
     *     // 如果存在一个消费者已经等待接收它，则立即传送指定的元素，否则等待直到元素被消费者接收。
     *     void transfer(E e) throws InterruptedException;
     *     // 在上述方法的基础上设置超时时间
     *     boolean tryTransfer(E e, long timeout, TimeUnit unit)
     *         throws InterruptedException;
     *     // 如果至少有一位消费者在等待，则返回true
     *     boolean hasWaitingConsumer();
     *     // 获取所有等待获取元素的消费线程数量
     *     int getWaitingConsumerCount();
     */
    public static BlockingQueue<String> bl6 = new LinkedTransferQueue();

    public static void main(String[] args) throws InterruptedException {

//        new Thread(() ->{
//            try {
//
//                String take = bl1.take();
//                System.out.println(take);
//
//                String take2 = bl1.take();
//                System.out.println(take2);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//
//        boolean add = bl1.add("a");
//        boolean b = bl1.offer("b");
//        bl1.put("c");
//
//        System.out.println(b);


    }


}
