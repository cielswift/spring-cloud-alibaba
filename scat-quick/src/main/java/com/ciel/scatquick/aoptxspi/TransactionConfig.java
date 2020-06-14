package com.ciel.scatquick.aoptxspi;

import org.springframework.context.annotation.Configuration;

/**
 * 事务
 */
@Configuration
public class TransactionConfig {

    /**
     * @Transactional解释
     */

    //    <tx:method name="zhuan*" read-only="false" propagation="REQUIRED" isolation="DEFAULT" rollback-for="java.lang.Exception"/>
//        			<!-- read-only="false" ;是否只读事务; 会数据库优化提升性能; 查询方法使用此属性(true) -->
//        			<!-- propagation: 控制事务==传播行为==
//    REQUIRED:默认, 这@EnableTransactionManagement个方法如果被另一个方法调用,如果另一个方法有事务,就加入这个事务,没有就新建事务;
//    SUPPORTS: 如果另一个方法有事务,就加入这个事务,没有在非事务状态下执行; 直接调用也不会有事务;
//    MANDATORY: 必须在事务内部执行,没有事务报错
//    REQUIRES_NEW:如果另一个方法有事务,就挂起另一个方法的事务,自己新建一个单独事务(两个事务没有关系),没有就新建事务;
//    NOT_SUPPORTED:必须在非事务状态下执行; 如果另一个方法有事务就挂起;没有正常执行;
//    NEVER:必须在非事务状态下执行,如果另一个方法有事务就报错;没有正常执行;
//    NESTED:必须在事务状态下执行; 没有事务新建事务;有事务,就新建嵌套事务
//        			 -->
//
//        			 <!-- isolation: 控制事务隔离级别
//    脏读:读取了未提交数据;
//    不可重复读:针对某行数据进行update,一个事务两次读取数据不一致,(其他事务中途修改) ;锁住查询到的数据
//    幻读:两次事务的查询结果不一致,((其他事务添加或删除了符合条件的数据); 锁住整个表
//
//            DEFAULT ;数据默认;
//    READ_UNCOMMITTED; 可能会脏读;
//    READ_COMMITTED ;只能读取已提交数据;防止脏读,会出现不可重复读,幻读;
//    REPEATABLE_READ ;防止重复读,,读取的数据会添加锁, 防止脏读,不可重复读,会出现幻读;
//    SERIALIZABLE ;排队操作 ,锁住整个表
//        			  -->
//
//        			  <!-- rollback-for="java.lang.Exception" 出现什么异常时需要回滚
//    no-rollback-for="":出现什么异常时不回滚
//            timeout="-1" ; 超过该时间限制但事务还没有完成，则自动回滚事务
//        			   -->

    //------------------------------------------------------------------------------------

//    propagation 代表事务的传播行为，默认值为 Propagation.REQUIRED，其他的属性信息如下：
//
//    Propagation.REQUIRED：如果当前存在事务，则加入该事务，如果当前不存在事务，则创建一个新的事务。( 也就是说如果A方法和B方法都添加了注解，在默认传播模式下，A方法内部调用B方法，会把两个方法的事务合并为一个事务 ）
//
//    Propagation.SUPPORTS：如果当前存在事务，则加入该事务；如果当前不存在事务，则以非事务的方式继续运行。
//
//    Propagation.MANDATORY：如果当前存在事务，则加入该事务；如果当前不存在事务，则抛出异常。
//
//    Propagation.REQUIRES_NEW：重新创建一个新的事务，如果当前存在事务，暂停当前的事务。( 当类A中的 a 方法用默认Propagation.REQUIRED模式，类B中的 b方法加上采用 Propagation.REQUIRES_NEW模式，然后在 a 方法中调用 b方法操作数据库，然而 a方法抛出异常后，b方法并没有进行回滚，因为Propagation.REQUIRES_NEW会暂停 a方法的事务 )
//
//    Propagation.NOT_SUPPORTED：以非事务的方式运行，如果当前存在事务，暂停当前的事务。
//
//    Propagation.NEVER：以非事务的方式运行，如果当前存在事务，则抛出异常。
//
//    Propagation.NESTED ：和 Propagation.REQUIRED 效果一样。
//
//    isolation 属性
//    isolation ：事务的隔离级别，默认值为 Isolation.DEFAULT。
//
//    Isolation.DEFAULT：使用底层数据库默认的隔离级别。
//
//    Isolation.READ_UNCOMMITTED
//
//    Isolation.READ_COMMITTED
//
//    Isolation.REPEATABLE_READ
//
//    Isolation.SERIALIZABLE
//
//    timeout 属性
//    timeout ：事务的超时时间，默认值为 -1。如果超过该时间限制但事务还没有完成，则自动回滚事务。
//
//    readOnly 属性
//    readOnly ：指定事务是否为只读事务，默认值为 false；为了忽略那些不需要事务的方法，比如读取数据，可以设置 read-only 为 true。
//
//    rollbackFor 属性
//    rollbackFor ：用于指定能够触发事务回滚的异常类型，可以指定多个异常类型。
//
//    noRollbackFor属性**
//    noRollbackFor：抛出指定的异常类型，不回滚事务，也可以指定多个异常类型。
}
