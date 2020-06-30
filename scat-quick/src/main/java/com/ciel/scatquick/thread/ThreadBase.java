package com.ciel.scatquick.thread;

public class ThreadBase {
    public static void main(String[] args) {
        Goods g = new Goods();

        new Thread(new Produce(g)).start();
        new Thread(new Produce(g)).start();
        new Thread(new Consume(g)).start();
        new Thread(new Consume(g)).start();
        
    }

    public static class Goods {
        private String name;
        private int number;
        private boolean flag; //信号灯

        synchronized public int addNumber() {
            return number++;
        }

        synchronized public void produce(String name) //同步方法使用的锁是this,这个Goods类的当前对象;
        {
            while (flag)//让被唤醒的线程再一次判断标记,如果是true,有商品,等待挂起; (如果写if,那么有可能会唤醒生产线程,但是生产会继续往下执行,不在判断了)
            {
                try {
                    this.wait();
                } catch (Exception e) {
                }
                ;  //当线程被等待的时候,也会释放锁,其他线程就可以进来;
            }                                            //sleep不会释放锁
            this.name = name;
            this.addNumber();
            System.out.println(Thread.currentThread().getName() + "生产者===" + this.name + "===" + this.number);
            this.flag = true;
            this.notifyAll(); //唤醒全部被等待的线程,(如果只唤醒第一个被等待的话,有可能唤醒了另一个生产者,出现生产两次(覆盖了),却只消费一次的情况);
            //也会唤醒了本方的线程,但是它会进入while判断,而再次等待

            //如果只this.notify(),那么有可能只唤醒本方的线程,然后本方线程也等待了,这样就陷入了全部线程都在等待,没人任何一个线程执行notify()唤醒了;
            //因为对方线程一开始就满足条件,进入等待了;
        }

        synchronized public void consume() {
            while (!flag)//让被唤醒的线程再一次判断标记,如果是false,没有商品,等待挂起;(如果写if,那么有可能会唤醒消费线程,但是消费会继续往下执行,不在判断了)
            {
                try {
                    this.wait();
                } catch (Exception e) {
                }
                ;   //当线程被等待的时候,也会释放锁,其他线程就可以进来;
            }
            System.out.println(Thread.currentThread().getName() + "消费者---" + this.name + "---" + this.number);
            this.flag = false;
            this.notifyAll();  //唤醒全部被等待的线程,(如果只唤醒第一个被等待的话,有可能唤醒了另一个消费者,会出现消费两次的情况);
            //也会唤醒了本方的线程,但是它会进入while判断,而再次等待
        }
    }

    public static class Produce implements Runnable {
        private Goods g;

        public Produce(Goods g) {
            this.g = g;
        }

        @Override
        public void run() {
            while (true) {
                g.produce("茶叶蛋");
            }
        }
    }

    public static class Consume implements Runnable {
        private Goods g;

        public Consume(Goods g) {
            this.g = g;
        }

        @Override
        public void run() {
            while (true) {
                g.consume();
            }
        }
    }

}