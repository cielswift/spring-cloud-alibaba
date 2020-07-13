package com.ciel.scatquick.thread;

import lombok.Data;

public class VolatileOneWriteManyRead {

    public static void main(String[] args) throws InterruptedException {
        //volatile 修改不能依赖于当前值 , volatile适合一写多读
        //volatile 修改对所有线程可见 ,立即写入主内存

        Fox fox = new Fox();
        new Thread(fox::run).start();

        Thread.sleep(1000);

        new Thread(() -> {
            fox.setSpace(2);
        }).start();


    }

    @Data
    public static class Fox {
        protected volatile Integer space = 0;

        public void run(){
            while(space==0){
                System.out.println("run run run==");
            }
        }

    }
}

