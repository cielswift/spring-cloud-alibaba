package com.ciel.scatquick.log;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 日志提交到redis
 */
@Slf4j
public class RedisLogAppend {

    public static void main(String[] args) {

        log.info("fuck yuo mother");

        String join = String.join("=", "b","c");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 0,
                TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024));

        executor.submit(RedisLogAppend::run);



    }


    public static void run(){
        while(true){
            System.out.println("fuck you mother");
        }
    }
}
