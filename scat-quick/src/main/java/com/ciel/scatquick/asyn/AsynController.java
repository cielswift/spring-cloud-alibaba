package com.ciel.scatquick.asyn;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.SysUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ResourceUtils;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 异步返回的 控制器
 */

@RestController
@RequestMapping("/asyn")

@Slf4j
@AllArgsConstructor
public class AsynController {

    protected AsynService service;

    @GetMapping("/a")
    public Result a() throws ExecutionException, InterruptedException {

        System.out.println(Thread.currentThread().getName());

        Future<String> result = service.returnMessage();
        //使用了Future的get方法获取了异步方法的返回值，但是这种获取返回值的方式会阻塞当前线程，也就是说调用了get方法之后，
        // 会等待异步线程执行完毕后才进行下一行代码的执行
        return Result.ok().data(result.get());
    }


    /**
     * 代码中可以看出，在返回的结果中添加了两个回调，分别是异步处理成功的回调SuccessCallback接口的实现类对象
     *  和异步处理失败发生异常的回调FailureCallback接口的实现类对象。ListenableFuture接口是对Future接口的扩展，支持回调，
     *  有效的避免了线程阻塞问题，也就是说，它会监听Future接口的执行情况，一旦完成，就会调用onSuccess方法进行成功后的处理，一旦发生异常，
     * 就会调用onFailure方法进行异常处理。相比较而言，更加推荐使用ListenableFuture来进行有返回值的异步处理。
     */
    @GetMapping("/b")
    public void b(Integer status) throws AlertException {
        System.out.println(Thread.currentThread().getName());

        HttpServletResponse response = SysUtils.getResponse(); //先获取响应 因为下面是两一个线程所以找不到了;

        ListenableFuture<String> result = service.returnMsg(status);

        result.addCallback(new SuccessCallback<String>() {

            @Override
            public void onSuccess(String result) {

                System.out.println("异步成功:"+result);
                try {
                    Faster.respJson(Result.ok().data(result),response);
                } catch (IOException e) {
                    throw new RuntimeException("请查看日志:"+e.getMessage());
                }
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable ex) {

                System.out.println("异步失败:"+ex);

                try {
                    Faster.respJson(Result.error("错误").data(ex.getMessage()),response);
                } catch (IOException e) {
                    throw new RuntimeException("请查看日志:"+e.getMessage());
                }
            }
        });
    }

    @GetMapping("/c")
    public void c() throws IOException {

        HttpServletResponse response = SysUtils.getResponse();
        service.asynDown(response);

    }
}
