package com.ciel.scatquick.aoptxspi.nati;

import com.ciel.scatquick.proxy.Empor;
import com.ciel.scatquick.proxy.Programmer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.*;

import java.lang.reflect.Method;

public class AopFas {
    public static void main(String[] args) throws Exception {
        //1.创建代理所需参数配置（如：采用什么方式的代理、通知列表等）

        //两个子类ProxyCreatorSupport
        //      ProxyFactory
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setExposeProxy(true); //在threadLocal暴露代理对象

        //设置被代理的目标对象
        Programmer target = new Programmer("xia");
        advisedSupport.setTarget(target);
        //强制使用cglib代理
        advisedSupport.setProxyTargetClass(true);
        advisedSupport.setInterfaces(Empor.class);

//        配置中添加的Advice对象最终都会被转换为DefaultPointcutAdvisor对象，此时DefaultPointcutAdvisor未指定pointcut，
//        大家可以去看一下DefaultPointcutAdvisor中pointcut有个默认值，默认会匹配任意类的任意方法。
//
//        当配置被冻结的时候，即frozen为true的时，此时配置中的Advisor列表是不允许修改的。
//
//        上面的getInterceptorsAndDynamicInterceptionAdvice方法，通过代理调用目标方法的时候，最后需要通过方法和目标类的类型，
//        从当前配置中会获取匹配的方法拦截器列表，获取方法拦截器列表是由AdvisorChainFactory负责的。
//        getInterceptorsAndDynamicInterceptionAdvice会在调用代理的方法时会执行，
//
//        目标方法和其关联的方法拦截器列表会被缓存在methodCache中，当顾问列表有变化的时候，methodCache缓存会被清除

        advisedSupport.addAdvice(new MethodInterceptor(){
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("aaaaaaa:"+invocation.getMethod().getName());
                return invocation.proceed();
            }
        });

        //如：添加一个前置通知
        advisedSupport.addAdvice(new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                System.out.println("bbbbbbbb");
                //如果不是路人的时候，抛出非法访问异常
                if ("路人".equals(method.getName())) {
                    throw new RuntimeException(String.format("[%s]非法访问!", method.getName()));
                }
            }
        });

        //2.根据配置信息获取AopProxy对象，AopProxy用来负责创建最终的代理对象
        /**
         * 默认AopProxyFactory实现，创建CGLIB代理或JDK动态代理。
         * 对于给定的AdvisedSupport实例，以下条件为真，则创建一个CGLIB代理:
         * optimize = true
         * proxyTargetClass = true
         * 未指定代理接口
         * 通常，指定proxyTargetClass来强制执行CGLIB代理，或者指定一个或多个接口来使用JDK动态代理。
         */
        // 创建AopProxy使用了简单工厂模式
        AopProxyFactory aopProxyFactory = new DefaultAopProxyFactory();

        //通过AopProxy工厂获取AopProxy对象
        // AopProxy接口有2个实现类（JDK动态代理、cglib代理）
//        JdkDynamicAopProxy：以jdk动态代理的方式创建代理
//        ObjenesisCglibAopProxy：以cglib的方式创建动态代理
//        jdk动态代理方式创建代理最终会调用ReflectiveMethodInvocation#proceed方法。
//        cglib方式创建的代理最终会调用CglibAopProxy.CglibMethodInvocation#proceed方法。

        AopProxy aopProxy = aopProxyFactory.createAopProxy(advisedSupport);
        /**
         * createAopProxy -> ObjenesisCglibAopProxy -> getProxy
         *
         *  // 被代理的目标对象是否是动态的（是否是单例的）
         *     boolean isStatic = this.advised.getTargetSource().isStatic();
         *      //所谓静态的，你可以理解为是否是单例的
         *           isStatic为true，表示目标对象是单例的，同一个代理对象中所有方法共享一个目标对象
         *          isStatic为false的时候，通常每次调用代理的方法，target对象是不一样的，
         *          所以方法调用万之后需要进行释放，可能有些资源清理，连接的关闭等操作
         */

        //获取方法的执行链 AdvisedSupport#getInterceptorsAndDynamicInterceptionAdvice
        //AdvisorChainFactory 拦截器链工厂接口 默认实现是DefaultAdvisorChainFactory

        //3.通过AopProxy创建代理对象
        Programmer proxy = (Programmer)aopProxy.getProxy();

        //获取需要被代理的所有接口
        Class<?>[] classes = AopProxyUtils.completeProxiedInterfaces(advisedSupport);


        proxy.work("aa");

        System.out.println(proxy);
    }
}
