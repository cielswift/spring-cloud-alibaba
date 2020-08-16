package com.ciel.scatquick.asyn;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.util.Faster;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.concurrent.ListenableFuture;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Future;

@Service
public class AsynService {

    /**
     * 异步回调消息方法
     */
    @Async
    public Future<String> returnMessage() {
        System.out.println(Thread.currentThread().getName());
        String message = "夏培鑫 异步返回值 阻塞";
        return new AsyncResult<>(message);
    }

    /**
     * 异步回调消息方法
     */
    @Async
    public ListenableFuture<String> returnMsg(Integer status) throws AlertException {
        System.out.println(Thread.currentThread().getName());

        if(status != null && status.equals(2)){
            throw new AlertException("2 异常");
        }

        String message = "夏培鑫 异步返回值 非阻塞";
        return new AsyncResult<>(message);
    }

    @Async
    public void asynDown(HttpServletResponse response) throws IOException {

        File file = ResourceUtils.getFile("classpath:bootstrap.yml");
        Faster.download(file,response);
    }

}