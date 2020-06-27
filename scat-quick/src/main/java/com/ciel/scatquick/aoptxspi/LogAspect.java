package com.ciel.scatquick.aoptxspi;

import com.ciel.scaapi.util.Faster;
import com.ciel.scatquick.security.realm.ScaCusUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Aspect //当前是一个切面类
@Component

@Slf4j
public class LogAspect {

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


    @Pointcut("@annotation(com.ciel.scatquick.aoptxspi.Logs)")
    public void logPointCut() {
    }



    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {


//      System.out.println(Arrays.toString(point.getArgs()));; //返回被增强方法的参数列表
//		System.out.println(point.getSignature()); //获取连接点的方法签名对象；
//		System.out.println(point.getTarget().getClass());  //获得真实对象
//		System.out.println(point.getThis().getClass());  //获得代理对象


        long str = System.currentTimeMillis();

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, Serializable> info = new HashMap<>();

        try{
            ScaCusUser  scaCusUser = (ScaCusUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            info.put("name",scaCusUser.getUsername());
            info.put("id",scaCusUser.getId());
        }catch (Exception e){
            info.put("name","null");
            info.put("id",0);
        }

        //执行方法
        Object methodReturn = point.proceed();

        String result = "null";
        if(Faster.isNotNull(methodReturn)){
            result = methodReturn.toString();
        }

        log.info("请求参数:{},请求方式:{},请求URL:{},请求IP:{}," +
                        "响应数据:{} ,当前用户:ID:{},当前用户:姓名:{}, 执行时间:{}ms",
                point.getArgs(), request.getMethod(), request.getRequestURI(), request.getRemoteAddr(),
                result, info.get("id"), info.get("name"), System.currentTimeMillis() - str);

        return methodReturn;
    }

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
}
