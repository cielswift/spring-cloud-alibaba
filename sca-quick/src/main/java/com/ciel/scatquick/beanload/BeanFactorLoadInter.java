package com.ciel.scatquick.beanload;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 全局bean factor加载的拦截方法
 *
 * 这个接口是beanFactory的扩展接口，调用时机在spring在读取beanDefinition信息之后，实例化bean之前。
 *
 * 在这个时机，用户可以通过实现这个扩展接口来自行处理一些东西，比如修改已经注册的beanDefinition的元信息
 *
 * 参考  AbstractApplicationContext.refresh()  invokeBeanFactoryPostProcessors
 *
 * 实现org.springframework.core.PriorityOrdered接口
 * 实现org.springframework.core.Ordered接口
 */
@Component
public class BeanFactorLoadInter implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

//  注意spring的4个阶段：bean定义阶段、BeanFactory后置处理阶段、BeanPostProcessor注册阶段、单例bean创建组装阶段
//
//  BeanDefinitionRegistryPostProcessor会在第一个阶段被调用，用来实现bean的注册操作，这个阶段会完成所有bean的注册
//  BeanFactoryPostProcessor会在第2个阶段被调用，到这个阶段时候，bean此时已经完成了所有bean的注册操作，这个阶段中你可以对BeanFactory中的一些信息进行修改，比如修改阶段1中一些bean的定义信息，修改BeanFactory的一些配置等等
//
//  阶段2的时候，2个禁止操作：禁止注册bean、禁止从容器中获取bean

        /**
         * postProcessBeanFactory方法中，强烈禁止去通过容器获取其他bean，此时会导致bean的提前初始化，
         * 会出现一些意想不到的问题，因为这个阶段中BeanPostProcessor还未准备好，本文开头4个阶段中有介绍，
         * BeanPostProcessor是在第3个阶段中注册到spring容器的，而BeanPostProcessor可以对bean的创建过程进行干预，
         * 比如spring中的aop就是在BeanPostProcessor的一些子类中实现的，
         * @Autowired也是在BeanPostProcessor的子类中处理的，此时如果去获取bean，
         * 此时bean不会被BeanPostProcessor处理，所以创建的bean可能是有问题的
         */

        //获取bean 定义
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("cacheRedis");
        //修改bean定义
        // beanDefinition.getPropertyValues().add("aa","bb");

        //加减乘除
        BigDecimal add = new BigDecimal("25.96").add(new BigDecimal("35.79")); // +
        BigDecimal decimal = new BigDecimal("25.96").subtract(new BigDecimal("36.96")); //-
        BigDecimal multiply = new BigDecimal("25.96").multiply(new BigDecimal("25.71")); // *
        BigDecimal divide = new BigDecimal("26.79")
                .divide(new BigDecimal("25.97"), 2, BigDecimal.ROUND_HALF_DOWN); // / 保留2位

        BigDecimal scale = divide.setScale(2, BigDecimal.ROUND_HALF_UP);//格式化
        Stream.generate(() -> UUID.randomUUID().toString()).limit(50).forEach(System.err::println);

        //正则表达式
        String reg = "^\\d{2}\\w$";
        String str = "29c";
        System.out.println(str.matches(reg));
        Pattern pa = Pattern.compile(reg);
        Matcher matcher = pa.matcher(str);

        if (matcher.find()) {
            System.out.println(matcher.group(0));
        }

        System.out.println(Arrays.toString(beanFactory.getBeanDefinitionNames()));
    }
}
