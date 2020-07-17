package com.ciel.scatquick.beanload;

import org.springframework.beans.BeansException;
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
 * 参考  org.springframework.context.support.AbstractApplicationContext.refresh()  invokeBeanFactoryPostProcessors
 */
@Component
public class BeanFactorLoadInter implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        //加减乘除
        BigDecimal add = new BigDecimal("25.96").add(new BigDecimal("35.79")); // +
        BigDecimal decimal = new BigDecimal("25.96").subtract(new BigDecimal("36.96")); //-
        BigDecimal multiply = new BigDecimal("25.96").multiply(new BigDecimal("25.71")); // *
        BigDecimal divide = new BigDecimal("26.79")
                .divide(new BigDecimal("25.97"), 2, BigDecimal.ROUND_HALF_DOWN); // / 保留2位

        divide.setScale(2,BigDecimal.ROUND_HALF_UP); //格式化
        Stream.generate(() -> UUID.randomUUID().toString()).limit(50000).forEach(System.err::println);

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
