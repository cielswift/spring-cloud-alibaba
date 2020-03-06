package com.ciel.scaconsumer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class InterceptorMain extends HandlerInterceptorAdapter {

    protected RedisTemplate<String, String> redisTemplate;

    public InterceptorMain(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        log.info(request.getRemoteHost().concat(":访问:").concat(request.getRequestURI()));
        System.out.println(request.getRemoteHost().concat(":访问:").concat(request.getRequestURI()));

        redisTemplate.opsForValue().increment(request.getRemoteHost(),1);

        return true;
        //当请求到达时 第一个执行此方法; 在执行controller处理之前执行
        //返回true 放行或进入下一个拦截器 ,返回false,不放行;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        //第二个执行 :在执行controller的处理后，在ModelAndView处理前执行
        //在请求完成后,但是(DispatcherServlet)还没有向客户端响应前调用;

        //modelAndView 包含了要返回的数据和跳转页面或者控制器
        //modelAndView.getViewName();
        //modelAndView.getModel().get("a");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        //afterCompletion ：在DispatchServlet执行完ModelAndView之后执行
        //preHandle 返回true时才会调用(并且一定会调用,不管下一个拦截器是否放行,或者postHandle是否执行成功); 可以对资源进行清理;
        //ex  是否为null来记录异常;
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response,
                                               Object handler) throws Exception {
        //这个方法会在Controller方法异步执行时开始执行，而postHandle方法则是需要等到Controller的异步执行完才能执行

        System.out.println("拦截了异步执行的方法");

    }
}