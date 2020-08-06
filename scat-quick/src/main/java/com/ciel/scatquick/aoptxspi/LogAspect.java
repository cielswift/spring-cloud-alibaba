package com.ciel.scatquick.aoptxspi;

import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.SysUtils;
import com.ciel.scatquick.controller.HiController;
import com.ciel.scatquick.security.realm.ScaCusUser;
import com.ciel.scatquick.sqlite.Sqlite;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Aspect //当前是一个切面类
@Component

//@Order(1)
@Slf4j
public class LogAspect implements Ordered {

    /**
     * @EnableAspectJAutoProxy的主要作用是开启对AOP的支持;
     * 主要起作用的是@Import(AspectJAutoProxyRegistrar.class)。
     * AspectJAutoProxyRegistrar注册器的主要作用是将AnnotationAwareAspectJAutoProxyCreator后置处理器注册到容器中
     *
     *
     *   实现该接口后Spring 在解析配置类的时候会通过后置处理器ConfigurationClassPostProcessor调用到
     *   ImportBeanDefinitionRegistrar#registerBeanDefinitions()方法
     *  并将AnnotationAwareAspectJAutoProxyCreator后置处理器的Bean定义注册到容器中。
     *
     *  BeanFactoryAspectJAdvisorsBuilder.buildAspectJAdvisors
     *  切面类的解析其实就是去配置类中找加了Before.class, Around.class, After.class, AfterReturning.class,
     *  AfterThrowing.class, Pointcut.class注解的方法。
     *
     *  整个获取增强器的大致流程：
     *
     * 遍历所有注册到容器中的beanName，并根据beanName获取对应的Class
     * 根据Class找出所有声明了@Aspect注解的类
     * 对标记了@Aspect的类进行增强器的提取，其实就是去找加了@Before, @Around, @After, @AfterReturning, @AfterThrowing, @Pointcut注解的方法。
     * 将增强器封装成InstantiationModelAwarePointcutAdvisorImpl类型
     * 将提取结果添加到缓存中

     拦截链封装到ReflectiveMethodInvocation类中，然后调用proceed()方法执行拦截连。
     最后通过MethodInterceptor.invoke()方法执行每个增强逻辑

    ReflectiveMethodInvocation的主要职责是维护了链接调用的计数器，记录着当前调用链接的位置，以便链可以有序地进行下去，
    ReflectiveMethodInvocation只负责拦截链的调用，所有的增强逻辑都在各各增强器中进行实现。
    拦截链在封装到ReflectiveMethodInvocation中之前，拦截链的顺序就已经按照一定规则拍顺序了，
    拦截链的执行其实就是proceed()方法的递归调用

    ReflectiveMethodInvocation.proceed()
     *
     */

    /**
     * 线程池
     */
    @Autowired
    protected ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    protected Sqlite sqlite;

    /**
     * 控制aop执行属顺序
     */
    @Override
    public int getOrder() {
        return  Ordered.HIGHEST_PRECEDENCE+1;
    }

    /**
     *  切入点表达式 :
     *         *:任意返回值;
     *         包名可以com.df.. (两个..)表示及其 子包;
     * 		      * *..*.save(..) 任意返回值,任意包下的任意类只要有save方法;  * *..*.*(..);所有方法都配置切面
     *             (..)表示有无参数均可 ,也可以指定(java.lang.String,com.df.po.Boss)参数  ;
     * 		     (*)任意参数,但是必须有参数 -->
     *
     * 		     <!-- 还有target表达式,this和target效果相同
     *     target(com.df.aop.InterfaceOfMy); 实现InterfaceOfMy接口的类;类的所有的方法自动增加通知织入实现切面;
     *     within(com.df.aop.*); within以包作为切入点;
     *     args(java.lang.String,com.df.Book); 参数符合要求作为切入点
     *     execution (public * com.ciel.provider.controller.AppController.*(..))
     *     @annotation(com.yitian.sbadmin.common.annotation.AutoLog)  对所有带有AutoLog注解的方法切面
     */


    @Pointcut("@annotation(com.ciel.scatquick.aoptxspi.LogsAnnal)")
    public void logPointCut() {
    }



    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {


//      System.out.println(Arrays.toString(point.getArgs()));; //返回被增强方法的参数列表
//		System.out.println(point.getSignature()); //获取连接点的方法签名对象；
//		System.out.println(point.getTarget().getClass());  //获得真实对象
//		System.out.println(point.getThis().getClass());  //获得代理对象


        long str = System.currentTimeMillis();

        HttpServletRequest request = SysUtils.currentRequest();

        Map<String, Serializable> info = new HashMap<>();

        try{
            ScaCusUser  scaCusUser = (ScaCusUser) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            info.put("name",scaCusUser.getUsername());
            info.put("id",scaCusUser.getId());
        }catch (Exception e){
            info.put("name","null");
            info.put("id",0);
        }

        //执行方法,或者继续执行下一个aop
        Object methodReturn = point.proceed();

        Long end = System.currentTimeMillis()- str;

        Signature sig = point.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("Logs注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());

        LogsAnnal annotation = currentMethod.getAnnotation(LogsAnnal.class);

        //AnnotatedElementUtils是spring提供的一个查找注解的工具类
        LogsAnnal annotation1 =
                AnnotatedElementUtils.getMergedAnnotation(HiController.class, LogsAnnal.class);

        threadPoolExecutor.submit(() -> {



            LogInfo logInfo = new LogInfo(null,
                    Faster.toString(point.getArgs()),
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    Faster.toJson(methodReturn,true),
                    Faster.toString(info.get("id")),
                    Faster.toString(info.get("name")),
                    end,
                    Faster.format(Faster.now()));

            try {
                sqlite.insert(logInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }

            log.info("{} -> 请求参数:{},请求方式:{},请求路径:{},请求IP:{}," +
                            "响应数据:{} ,当前用户:ID:{},当前用户:姓名:{}, 执行时间:{}ms",
                    annotation.prefix(), logInfo.getParam(), logInfo.getMethod(), logInfo.getPath(), logInfo.getIp(),
                    logInfo.getResp(), logInfo.getUserId(), logInfo.getUserName(), logInfo.getTime());
        });

        return methodReturn;
    }


    // 切点
//    @Pointcut("@annotation(com.xiaolyuh.aop.annotations.Log)")
//    public void pointCutMethod() {
//    }
//
//    @Pointcut("@within(com.xiaolyuh.aop.annotations.Log)")
//    public void pointCutType() {
//    }
//
//    // 建言
//    @Before("pointCutMethod() || pointCutType()")
//------------------------------------------------------------------

//    @Before("point()")  //之前执行
//    public void before(JoinPoint joinPoint){
//        System.out.println("bef");
//    }
//
//    @After("point()") //一定会执行
//    public void after(JoinPoint joinPoint){
//        System.out.println("after");
//    }
//
//    @AfterReturning(value = "point()",returning = "returnResult") //执行完后执行 有返回值
//    public void AfterReturning(JoinPoint joinPoint,Object returnResult){ //joinPoint要放在前面
//
//        System.out.println("AfterReturning");
//    }
//
//    @AfterThrowing(value = "point()",throwing = "exceptionResult") //发生异常执行
//    public void AfterThrowing(JoinPoint joinPoint,Exception exceptionResult){
//        System.out.println("AfterThrowing");
//    }

//    @Before：前置通知，在调用目标方法之前执行通知定义的任务
//    @After：后置通知，在目标方法执行结束后，无论执行结果如何都执行通知定义的任务
//    @AfterReturning：后置通知，在目标方法执行结束后，如果执行成功，则执行通知定义的任务
//    @AfterThrowing：异常通知，如果目标方法执行过程中抛出异常，则执行通知定义的任务
//    @Around：环绕通知，在目标方法执行前和执行后，都需要执行通知定义的任务
}
