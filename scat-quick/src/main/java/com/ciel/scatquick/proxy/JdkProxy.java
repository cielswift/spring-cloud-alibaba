package com.ciel.scatquick.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxy implements InvocationHandler {

    private Object target;

    public JdkProxy(Object target){
        this.target=target;
    }

    // 返回一个代理对象，并绑定被代理对象
    public Object getProxyInstance() {

        // Proxy的newProxyInstance方法需要三个参数
        return Proxy.newProxyInstance(
                // 1.一个类加载器，通常可以从已经被加载的对象中获取类加载器
                target.getClass().getClassLoader(),
                // 2.希望代理实现的接口列表
                target.getClass().getInterfaces(),
                // 3.一个InvocationHandler接口的实现
                this);
    }

    /**
     *
     * @param proxy 代理对象
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 这里我们简单输出一下invoke的第二第三个参数

        // 员工正在完成工作
        Object invoke = method.invoke(target, args);
        // 员工完成工作之后

        return invoke;
    }

    public static void main(String[] args) {

        Programmer programmer = new Programmer("夏培鑫");
        JdkProxy jdkProxy = new JdkProxy(programmer);

        Empor boss = (Empor) jdkProxy.getProxyInstance();

        boss.work("代码");
    }
}