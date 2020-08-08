package com.ciel.scatquick.aoptxspi.nati;

import com.ciel.scatquick.proxy.Programmer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.*;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class Join {

    public static void main(String[] args) throws Exception {
        //定义目标对象
        Programmer target = new Programmer("xiapeixin");

        //切入点
        Pointcut pointcut = new Pointcut() {

            /**
             * 用来判断目标类型是否匹配
             */
            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.isAssignableFrom(Programmer.class);
            }

            @Override
            public MethodMatcher getMethodMatcher() {
                return new MethodMatcher() {

                    /**
                     * 执行静态检查给定方法是否匹配
                     * @param method 目标方法
                     * @param targetClass 目标对象类型
                     */

                    @Override
                    public boolean matches(Method method, Class<?> targetClass) {
                        //判断方法名称是否是work
                        return "work".equals(method.getName());
                    }
                    /**
                     * 是否是动态匹配，即是否每次执行目标方法的时候都去验证一下
                     */
                    @Override
                    public boolean isRuntime() {
                        return false; //这里返回true 才会进行下面的方法
                    }
                    /**
                     * 动态匹配验证的方法，比第一个matches方法多了一个参数args，这个参数是调用目标方法传入的参数
                     */
                    @Override
                    public boolean matches(Method method, Class<?> targetClass, Object... args) {
                        // isRuntime 必须返回true ,才会进入这里
                        return false;
                    }
                };
            }
        };

        //前置通知
        MethodBeforeAdvice beforeAdvice = (method, arg, target1) -> {
            System.out.println("你好:" + arg[0]);
        };

        //后置通知
        //不过需要注意一点：目标方法正常执行后，才会回调这个接口，当目标方法有异常，那么这通知会被跳过
        AfterReturningAdvice afterReturningAdvice =  (returnValue,  method, arg,  targ) -> {
            System.out.println(returnValue);
        };


        /** 通知
         * 拦截目标方法的执行，可以在这个方法内部实现需要增强的逻辑，以及主动调用目标方法
         * 所有的通知均需要转换为MethodInterceptor类型的，最终多个MethodInterceptor组成一个方法拦截器连
         */
        MethodInterceptor methodInterceptor = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {

                //如果是jdk动态代理 那么是ReflectiveMethodInvocation
                //如果cglib 那么是 CglibMethodInvocation

//                public interface ProxyMethodInvocation extends MethodInvocation {
//                     * 获取被调用的代理对象
//                    Object getProxy();
//
//                     * 克隆一个方法调用器MethodInvocation
//                    MethodInvocation invocableClone();
//
//                     * 克隆一个方法调用器MethodInvocation，并为方法调用器指定参数
//                    MethodInvocation invocableClone(Object... arguments);
//
//                     * 设置要用于此链中任何通知的后续调用的参数。
//                    void setArguments(Object... arguments);
//
//                     * 添加一些扩展用户属性，这些属性不在AOP框架内使用。它们只是作为调用对象的一部分保留，用于特殊的拦截器。
//                    void setUserAttribute(String key, @Nullable Object value);
//
//                     * 根据key获取对应的用户属性
//                    Object getUserAttribute(String key);
//                }

                //返回正在被调用得方法~~~  返回的是当前Method对象。
                Method method = invocation.getMethod();

                 //将参数作为数组对象获取，可以更改此数组中的元素值以更改参数。
                Object[] arguments = invocation.getArguments();

                //返回保存当前连接点静态部分【的对象】，这里一般指被代理的目标对象
                Object aThis = invocation.getThis();

                //返回此静态连接点  一般就为当前的Method(至少目前的唯一实现是MethodInvocation,所以连接点得静态部分肯定就是本方法)
                AccessibleObject staticPart = invocation.getStaticPart();
                return invocation.proceed(); //转到拦截器链中的下一个拦截器
            }
        };

        //切入点和通知组装 //顾问
        DefaultPointcutAdvisor bef = new DefaultPointcutAdvisor(pointcut, beforeAdvice);
        bef.setOrder(1);

        DefaultPointcutAdvisor af = new DefaultPointcutAdvisor(pointcut, afterReturningAdvice);
        bef.setOrder(2);

        DefaultPointcutAdvisor me = new DefaultPointcutAdvisor(pointcut, methodInterceptor);
        me.setOrder(3);

        DefaultPointcutAdvisor th = new DefaultPointcutAdvisor(pointcut, new ExceptionAdvice());
        bef.setOrder(4);

        //通过spring提供的代理创建工厂来创建代理
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setExposeProxy(true); //在threadLocal暴露代理对象
        //强制使用cglib代理
        proxyFactory.setProxyTargetClass(true);
        //为工厂指定目标对象
        proxyFactory.setTarget(target);
       // proxyFactory.setInterfaces();

//        一个目标方法中可以添加很多Advice，这些Advice最终都会被转换为MethodInterceptor类型的方法拦截器，
//        最终会有多个MethodInterceptor，这些MethodInterceptor会组成一个方法调用链

        //调用addAdvisor方法，为目标添加增强的功能，即添加Advisor，可以为目标添加很多个Advisor
        //添加包装器 (包装器 = 通知 + 切入点)
        proxyFactory.addAdvisor(bef);
        proxyFactory.addAdvisor(af);
        proxyFactory.addAdvisor(me);
        proxyFactory.addAdvisor(th);


        //proxyFactory.addAdvice(methodInterceptor); 可以直接添加 通知
        //通过工厂提供的方法来生成代理对象

        Programmer userServiceProxy = (Programmer) proxyFactory.getProxy();

        //调用代理的work方法
        userServiceProxy.work("路人");
    }
}
