package com.ciel.scatquick.aoptxspi;

import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.SysUtils;
import com.ciel.scatquick.security.realm.ScaCusUser;
import com.ciel.scatquick.sqlite.Sqlite;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
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

   // private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogAspect.class);

    /**
     * @EnableAspectJAutoProxy的主要作用是开启对AOP的支持;会在spring容器中注册一个bean
     * #AnnotationAwareAspectJAutoProxyCreator对符合条件的bean，自动生成代理对象
     * 注册器的主要作用是将AnnotationAwareAspectJAutoProxyCreator后置处理器注册到容器中
     *
     *  而AspectJProxyFactory这个类可以通过解析@Aspect标注的类来生成代理aop代理对象
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


    DefaultPointcutAdvisor; spring提供的实现 (切面+切入点实现+顺序) ;可以加入到spring容器中 和 @Aspect作用一样
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
     *     @within(com.javacode2018.aop.demo9.test10.Ann10) 匹配类上的注解
     */


    @Pointcut("@annotation(com.ciel.scatquick.aoptxspi.LogsAnnal)")
    public void logPointCut() {
    }


    /**
     * 环绕通知类型 AspectJAroundAdvice
     */
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
        //Method method = msig.getMethod(); 获取方法
        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());

        LogsAnnal annotation = currentMethod.getAnnotation(LogsAnnal.class);

        //AnnotatedElementUtils是spring提供的一个查找注解的工具类
//        LogsAnnal annotation1 =
//                AnnotatedElementUtils.getMergedAnnotation(HiController.class, LogsAnnal.class);

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
//    // &&：多个匹配都需要满足
//    ||：多个匹配中只需满足一个
//    !：匹配不满足的情况下
//    @Before("pointCutMethod() || pointCutType()")
//------------------------------------------------------------------

//    @Before("point()")  //之前执行  (对应类AspectJMethodBeforeAdvice)
//    public void before(JoinPoint joinPoint){
//        System.out.println("bef");

//    String toString();         //连接点所在位置的相关信息
//    String toShortString();     //连接点所在位置的简短相关信息
//    String toLongString();     //连接点所在位置的全部相关信息
//    Object getThis();         //返回AOP代理对象
//    Object getTarget();       //返回目标对象
//    Object[] getArgs();       //返回被通知方法参数列表，也就是目前调用目标方法传入的参数
//    Signature getSignature();  //返回当前连接点签名，这个可以用来获取目标方法的详细信息，如方法Method对象等
//    SourceLocation getSourceLocation();//返回连接点方法所在类文件中的位置
//    String getKind();        //连接点类型
//    StaticPart getStaticPart(); //返回连接点静态部分

//    }
//
//    @After("point()") //一定会执行 (对应类AspectJAfterAdvice)
//    public void after(JoinPoint joinPoint){
//        System.out.println("after");
//    }
//
//    @AfterReturning(value = "point()",returning = "returnResult") //执行完后执行 有返回值 (对应类AspectJAfterReturningAdvice)
//    public void AfterReturning(JoinPoint joinPoint,Object returnResult){ //joinPoint要放在前面
//
//        System.out.println("AfterReturning");
//    }
//
//    @AfterThrowing(value = "point()",throwing = "exceptionResult") //发生异常执行 (对应类AspectJAfterThrowingAdvice)
//    public void AfterThrowing(JoinPoint joinPoint,Exception exceptionResult){
//        System.out.println("AfterThrowing");
//    }

//    @Before：前置通知，在调用目标方法之前执行通知定义的任务
//    @After：后置通知，在目标方法执行结束后，无论执行结果如何都执行通知定义的任务
//    @AfterReturning：后置通知，在目标方法执行结束后，如果执行成功，则执行通知定义的任务
//    @AfterThrowing：异常通知，如果目标方法执行过程中抛出异常，则执行通知定义的任务
//    @Around：环绕通知，在目标方法执行前和执行后，都需要执行通知定义的任务

    /**
     * Aspect内部的通知执行顺序
     * try {
     *     Object result = null;
     *     try {
     *         System.out.println("@Around通知start");
     *         System.out.println("@Before通知!");
     *         result = service4.say("路人");
     *         System.out.println("@Around绕通知end");
     *         return result;
     *     } finally {
     *         System.out.println("@After通知!");
     *     }
     *     System.out.println("@AfterReturning通知!");
     *     return retVal;
     * } catch (Throwable ex) {
     *     System.out.println("@AfterThrowing通知!");
     *     //继续抛出异常
     *     throw ex;
     * }
     */
}
