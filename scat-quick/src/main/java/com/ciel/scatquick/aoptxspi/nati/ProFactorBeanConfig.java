package com.ciel.scatquick.aoptxspi.nati;

import com.ciel.scatquick.aoptxspi.LogAspect;
import com.ciel.scatquick.proxy.Programmer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

//@Configuration
public class ProFactorBeanConfig {

    //注册目标对象
    @Bean
    public Programmer programmer() {
        return new Programmer("xiapeixin");
    }

    //注册一个前置通知
    @Bean
    public MethodBeforeAdvice beforeAdviceaa() {
        return new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, @Nullable Object target) throws Throwable {
                System.out.println("bbbbbbbbb");
            }
        };
    }

    //注册一个后置通知
    @Bean
    public MethodInterceptor costTimeInterceptoraa() {
        return new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {

                System.out.println("aaaaaaaaaaaa");

                return invocation.proceed();
            }
        };
    }

    //注册ProxyFactoryBean
    @Bean
    public ProxyFactoryBean proxyFactoryBean(Programmer programmer,
                                             MethodBeforeAdvice methodBeforeAdvice,
                                             MethodInterceptor methodInterceptor) {
        /**
         * getObject
         * //初始化advisor(拦截器)链
         *     initializeAdvisorChain(); 是根据interceptorNames配置，找到spring容器中符合条件的拦截器，将其放入创建aop代理的配置中
         *     //创建单例代理对象
         *         return getSingletonInstance();
         */
        //1.创建ProxyFactoryBean
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();


        //2.设置目标对象的bean名称
        //proxyFactoryBean.setTargetName("programmerzz"); //通过名称设置
        proxyFactoryBean.setTarget(programmer);
        //3.设置拦截器的bean名称列表，此处2个（advice1和advice2)

        proxyFactoryBean.setProxyTargetClass(true);
        proxyFactoryBean.setExposeProxy(true);

        /**
         * 按顺序执行
         */
        proxyFactoryBean.addAdvice(methodInterceptor);
        proxyFactoryBean.addAdvice(methodBeforeAdvice);

        //批量方式添加切面 ;按顺序执行  支持通配符 如*
        /**
         * org.springframework.aop.Advisor
         * org.aopalliance.intercept.Interceptor
         * 这个地方使用的时候需要注意，批量的方式注册的时候，如果增强器的类型不是上面2种类型的，
         * 比如下面3种类型通知，我们需要将其包装为Advisor才可以，
         * 而MethodInterceptor是Interceptor类型的，可以不用包装为Advisor类型的
         * MethodBeforeAdvice（方法前置通知）
         * AfterReturningAdvice（方法后置通知）
         * ThrowsAdvice（异常通知）
         */
        //proxyFactoryBean.setInterceptorNames("beforeAdviceaa", "costTimeInterceptoraa");
        return proxyFactoryBean;
    }

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ProFactorBeanConfig.class);

//获取代理对象，代理对象bean的名称为注册ProxyFactoryBean的名称，即：service1Proxy
        Programmer programmer = context.getBean("proxyFactoryBean",Programmer.class);
//调用代理的方法
       programmer.work("aa");

        //context.register(MainConfig1.class); 注册一个新的配置类
        //context.refresh(); 刷新容器

       ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("----------------------------------------------------------------------");
        //对应目标对象
        //创建AspectJProxyFactory对象
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
        //设置被代理的目标对象
        proxyFactory.setTarget(new Programmer("xiapeixin"));
        //设置标注了@Aspect注解的类
        proxyFactory.addAspect(LogAspect.class);
        //生成代理对象
        Programmer proxy = proxyFactory.getProxy();
        //使用代理对象
        proxy.work("bb");

    }
}
