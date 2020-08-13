package com.ciel.scaapi.util;

/**
 * 上下文
 */
public final class AppContext {

    public static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    public static String getToken(){
        return CURRENT.get();
    }
    
    public static void setToken(String token){
         CURRENT.set(token);
    }


    //创建一个操作Thread中存放请求任务追踪id口袋的对象,子线程可以继承父线程中内容
    public static final InheritableThreadLocal<String> traceIdKD = new InheritableThreadLocal<>();

    public static String innGet(){
        return traceIdKD.get();
    }

    public static void innSet(String token){
        traceIdKD.set(token);
    }

}
