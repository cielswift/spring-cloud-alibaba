package com.ciel.scatquick.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * cglib 动态代理
 */
public class CglibProxyFactory implements MethodInterceptor {
    private Object target;

    public CglibProxyFactory(Object target) {
        this.target = target;
    }

    public Object getProxyInstance() {
        // 创建 Enhancer 对象
        Enhancer enhancer = new Enhancer();
        // 设置目标对象的Class
        enhancer.setSuperclass(target.getClass());
        // 设置回调操作，相当于InvocationHandler
        enhancer.setCallback(this);

        return enhancer.create(); //被代理类必须有无参构造函数 否则报错
    }

    /**
     *
     * @param proxy  com.ciel.scatquick.proxy.Programmer$$EnhancerByCGLIB$$d0aaa01e 代理对象
     * @param method work 方法
     * @param args 代码 参数
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        System.out.println("开始事务...");

        // 两种方式执行方法，第二行注释掉的，和当前行代码效果相同，下面会分析；
        // Object invoke = methodProxy.invokeSuper(proxy, args);

        if("work".equals(method.getName())){
            Object invoke = method.invoke(target, args);
            System.out.println("提交");
            return invoke;
        }

        System.out.println("回滚");

        return null;
    }

    public static void main(String[] args) {

        Programmer programmer = new Programmer("夏培鑫");

        CglibProxyFactory cglibProxyFactory = new CglibProxyFactory(programmer);

        Programmer proxyInstance = (Programmer)cglibProxyFactory.getProxyInstance();

        proxyInstance.work("代码");

    }
}