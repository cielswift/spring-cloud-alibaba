package com.ciel.springcloudalibabaproducer2.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 事务 aop
 */
@Aspect
@Component
public class TransactionAspect {

    public static final Logger log = LoggerFactory.getLogger(TransactionAspect.class);

    //  对所有带有AutoLog注解的方法记录日志。
    @Pointcut("@annotation(com.ciel.springcloudalibabaapi.anntion.TransactionXiaPeiXin)")
    public void point() {
    }

    @Autowired
    protected DataSourceTransactionManager transactionManager;


    @Around("point()")
    public Object around(ProceedingJoinPoint pjp) throws Exception {
        Object rtValue = null;

        //2.获取事务定义
        DefaultTransactionDefinition def = null;
        //4.获得事务状态
        TransactionStatus status = null;

        try {
            Object[] args = pjp.getArgs();//得到方法执行所需的参数
            log.info("事务开始");

            //2.获取事务定义
            def = new DefaultTransactionDefinition();
            //3.设置事务隔离级别，开启新事务
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            status = transactionManager.getTransaction(def);

            rtValue = pjp.proceed(args);//明确调用业务层方法（切入点方法）

            transactionManager.commit(status);
            log.info("事务结束并提交");


            return rtValue;
        } catch (Throwable t) {

            transactionManager.rollback(status);
            log.info("事务异常--回滚");
            throw new Exception("事务异常--回滚:".concat(t.getMessage()));

        } finally {

            log.info("AOP 结束");
        }
    }

}
