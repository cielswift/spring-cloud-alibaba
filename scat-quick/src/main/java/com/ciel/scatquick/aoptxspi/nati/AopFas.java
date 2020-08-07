package com.ciel.scatquick.aoptxspi.nati;

import com.ciel.scatquick.proxy.Programmer;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.*;

import java.io.Serializable;
import java.lang.reflect.Method;

public class AopFas {
    public static void main(String[] args) {

        //1.创建代理所需参数配置（如：采用什么方式的代理、通知列表等）
        AdvisedSupport advisedSupport = new AdvisedSupport();





        advisedSupport.setProxyTargetClass(true);

        //如：添加一个前置通知
        advisedSupport.addAdvice(new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                //如果不是路人的时候，抛出非法访问异常
                if (!"路人".equals(method.getName())) {
                    throw new RuntimeException(String.format("[%s]非法访问!", method.getName()));
                }
            }
        });

        //设置被代理的目标对象
        Programmer target = new Programmer("xia");
        advisedSupport.setTarget(target);

        //2.根据配置信息获取AopProxy对象，AopProxy用来负责创建最终的代理对象
        // AopProxy接口有2个实现类（JDK动态代理、cglib代理）
        // 具体最终会使用哪种方式，需要根据AdvisedSupport中指定的参数来判断
        // 创建AopProxy使用了简单工厂模式
        AopProxyFactory aopProxyFactory = new DefaultAopProxyFactory();
        //通过AopProxy工厂获取AopProxy对象
        AopProxy aopProxy = aopProxyFactory.createAopProxy(advisedSupport);

        //3.通过AopProxy创建代理对象
        Object proxy = aopProxy.getProxy();

        System.out.println(proxy);
    }
}
