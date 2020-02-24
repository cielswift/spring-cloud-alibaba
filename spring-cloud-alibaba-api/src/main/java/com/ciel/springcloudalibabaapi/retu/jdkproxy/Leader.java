package com.ciel.springcloudalibabaapi.retu.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Leader implements InvocationHandler {

    private Object target;

    // 返回一个代理对象，并绑定被代理对象
    public Object getProxyInstance(Object object) {
        this.target = object;

        // Proxy的newProxyInstance方法需要三个参数
        return Proxy.newProxyInstance(
            // 1.一个类加载器，通常可以从已经被加载的对象中获取类加载器
            object.getClass().getClassLoader(),
            // 2.希望代理实现的接口列表
            object.getClass().getInterfaces(),
            // 3.一个InvocationHandler接口的实现
            this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 这里我们简单输出一下invoke的第二第三个参数
        System.out.println("method : " + method);
        System.out.println("args : " + args);
        if (args != null) {
        System.out.print("arg : ");
        for (Object arg : args) {
            System.out.print(arg + " ");
            }
            System.out.println();
        }
        // 员工完成工作之前
        System.out.println(">>>>>员工完成工作之前");
        // 员工正在完成工作
        Object invoke = method.invoke(target, args);
        // 员工完成工作之后
        System.out.println("员工完成工作之后>>>>>");
        return invoke;
    }

    public static void main(String[] args) {
        Employee employee = new Employee();
        Leader leader = new Leader();

        Boss boss = (Boss) leader.getProxyInstance(employee);

        boss.doSomethinig();
        boss.finishTasks("Java动态代理");
    }
}