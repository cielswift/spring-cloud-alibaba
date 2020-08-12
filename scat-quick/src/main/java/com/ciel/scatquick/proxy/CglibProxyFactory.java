package com.ciel.scatquick.proxy;

import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.*;
import org.springframework.aop.framework.AopContext;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * cglib 动态代理
 */
public class CglibProxyFactory<T> implements MethodInterceptor  {
    private T target;

    public CglibProxyFactory(T target) {
        this.target = target;
    }

    public T getProxyInstance() {
        // 创建 Enhancer 对象
        Enhancer enhancer = new Enhancer();

        //表示生成代理类的名字的策略
        enhancer.setNamingPolicy(new NamingPolicy(){
            @Override
            public String getClassName(String prefix, String source, Object key, Predicate names) {
                return prefix.concat("CglibCielSwift");
            }
        });


        // 设置目标对象的Class
        enhancer.setSuperclass(target.getClass());
        //设置代理对象实现的接口
        enhancer.setInterfaces(target.getClass().getInterfaces());

        /*设置回调，需实现org.springframework.cglib.proxy.Callback接口，
        此处我们使用的是org.springframework.cglib.proxy.MethodInterceptor，也是一个接口，实现了Callback接口，
        当调用代理对象的任何方法的时候，都会被MethodInterceptor接口的invoke方法处理*/
        enhancer.setCallback(this);

        //enhancer.setCallback(NoOp.INSTANCE); //被调用的方法会直接放行，像没有任何代理一样
////////////////////////////////////////////////////////////////////////////////////////////////////////
        //创建2个Callback
        Callback[] callbacks = {
                new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        long starTime = System.nanoTime();
                        Object result = methodProxy.invokeSuper(o, objects);
                        long endTime = System.nanoTime();
                        System.out.println(method + "，耗时(纳秒):" + (endTime - starTime));
                        return result;
                    }
                },
                //下面这个用来拦截所有get开头的方法，返回固定值的
                new FixedValue() {
                    @Override
                    public Object loadObject() throws Exception {
                        return "路人甲Java";
                    }
                }
        };

        enhancer.setCallbacks(callbacks); //多个回调

        /**
         * 设置过滤器CallbackFilter
         * CallbackFilter用来判断调用方法的时候使用callbacks数组中的哪个Callback来处理当前方法
         * 返回的是callbacks数组的下标
         */
        enhancer.setCallbackFilter(new CallbackFilter() {
            @Override
            public int accept(Method method) {
                return method.getName().startsWith("work")? 0 : 1;
            }
        });
///////////////////////////////////////////////////////////////////////////////////////////////////////

        CallbackHelper callbackHelper = new CallbackHelper(target.getClass(), null) {
            @Override
            protected Object getCallback(Method method) {
                return method.getName().startsWith("work") ? callbacks[0] : callbacks[1];
            }
        };

        enhancer.setCallbacks(callbackHelper.getCallbacks());
        enhancer.setCallbackFilter(callbackHelper);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * 当第1次调用work方法的时候，会被cglib拦截，进入lazyLoader的loadObject内部，
         * 将这个方法的返回值作为work方法的调用者，loadObject中返回了一个对象，
         * cglib内部会将loadObject方法的返回值和work方法关联起来，然后缓存起来，而第2次调用work方法的时候
         * ，通过方法名去缓存中找，会直接拿到第1次返回的对象，所以第2次不会进入到loadObject方法中了
         */
        //创建一个LazyLoader对象
        LazyLoader lazyLoader = new LazyLoader() {
            @Override
            public Object loadObject() throws Exception {
                System.out.println("调用LazyLoader.loadObject()方法");
                return target.getClass().getConstructor().newInstance();
            }
        };
        enhancer.setCallback(lazyLoader);
        enhancer.setCallbackFilter(null);
/////////////////////////////////////////////////////////////////////////////////////////////////////

       // Dispatcher和LazyLoader作用很相似，区别是用Dispatcher的话每次对增强bean进行方法调用都会触发回调

        Dispatcher dispatcher = new Dispatcher() {
            @Override
            public Object loadObject() throws Exception {
                System.out.println("调用Dispatcher.loadObject()方法");
                return target.getClass().getConstructor().newInstance();
            }
        };
        enhancer.setCallback(dispatcher);
        enhancer.setCallbackFilter(null);
/////////////////////////////////////////////////////////////////////////////////////////////////////


        return (T)enhancer.create(); //被代理类必须有无参构造函数 否则报错
    }


    /**
     * intercept方法，这个方法会拦截代理对象所有的方法调用
     *
     * @param proxy       com.ciel.scatquick.proxy.Programmer$$EnhancerByCGLIB$$d0aaa01e 代理对象
     * @param method      work 方法
     * @param args        代码 参数
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        System.out.println("开始事务...");

        // 两种方式执行方法，第二行注释掉的，和当前行代码效果相同，下面会分析
       // 可以调用被代理类，也就是子类中的具体的方法，从方法名称的意思可以看出是调用父类，
                //实际对某个类创建代理，cglib底层通过修改字节码的方式为被代理类创建了一个子类

        // Object invoke = methodProxy.invokeSuper(proxy, args);
        try {
            if ("work".equals(method.getName())) {
                Object invoke = method.invoke(target, args);

                 System.out.println("提交事务");

                if(invoke.getClass().isAssignableFrom(String.class)){
                    System.out.println(true);
                }

                return invoke;
            }else{
                return method.invoke(target, args);
            }

        } catch (Exception e) {
            System.out.println(e.getCause().getMessage() +"::回滚事务");
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

        //Objenesis 创建对象即使没有空的构造函数; 即使你有空的构造方法，也是不会执行的
        Objenesis objenesis = new ObjenesisStd();

        System ff = objenesis.newInstance(System.class);

        ObjectInstantiator<System> of = objenesis.getInstantiatorOf(System.class);
        System ff2 = of.newInstance();

        System.out.println(ff);
        System.out.println(ff2);

        Programmer programmer = new Programmer("夏培鑫");
        CglibProxyFactory<Programmer> cglibProxyFactory = new CglibProxyFactory<>(programmer);

        Programmer proxyInstance =  cglibProxyFactory.getProxyInstance();


        String work = proxyInstance.work("代码");
        String work2 = proxyInstance.work("代码");

        System.out.println(work);
        System.out.println(work2);
    }
}