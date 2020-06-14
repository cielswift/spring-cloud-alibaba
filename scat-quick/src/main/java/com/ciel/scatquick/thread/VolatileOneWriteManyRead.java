package com.ciel.scatquick.thread;

import lombok.Data;

public class VolatileOneWriteManyRead {

    public static void main(String[] args) {
        //volatile 修改对所有线程可见 ,立即写入主内存
        Fox fox = new VolatileOneWriteManyRead.Fox();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {

                while(true){
                    System.out.println(String.format("线程 %s 读取到距离 %s",Thread.currentThread().getName(),fox.getSpace()));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(String.format("写线程修改距离: %s",i));
                fox.setSpace(i);  //volatile 修改不能依赖于当前值 , volatile适合一写多读
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Data
    public static class Fox {
        protected volatile Integer space = 0;
    }
}

