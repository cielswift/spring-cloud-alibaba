package com.xia.config;

import com.xia.bean.XiapeixinFas;
import com.xia.bean.XiapeixinFbs;
import com.xia.bean.XiapeixinFis;
import com.xia.contional.WindowsSystem;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(WindowsSystem.class) //返回true才会生效
@AutoConfigureAfter(XiapeixinFas.class)  //XiapeixinFas加载完后,再加载本类配置
@AutoConfigureBefore(XiapeixinFbs.class) //XiapeixinFbs加载完后,再加载本类配置

/**
 * 加载顺序 越小 优先级越高
 */
@AutoConfigureOrder(1)

/**
 * 使@ConfigurationProperties 注解的类生效,而不用使用 @Component 或 @Configuration
 */
@EnableConfigurationProperties(XiapeixinFis.class)

public class ContionalConfig {

    /**
     * @ConditionalOnClass ： classpath中存在该类时起效
     * @ConditionalOnMissingClass ： classpath中不存在该类时起效
     * @ConditionalOnBean ： DI容器中存在该类型Bean时起效
     * @ConditionalOnMissingBean ： DI容器中不存在该类型Bean时起效
     * @ConditionalOnSingleCandidate ： DI容器中该类型Bean只有一个或@Primary的只有一个时起效
     * @ConditionalOnExpression ： SpEL表达式结果为true时
     * @ConditionalOnProperty ： 参数设置或者值一致时起效
     * @ConditionalOnResource ： 指定的文件存在时起效
     * @ConditionalOnJndi ： 指定的JNDI存在时起效
     * @ConditionalOnJava ： 指定的Java版本存在时起效
     * @ConditionalOnWebApplication ： Web应用环境下起效
     * @ConditionalOnNotWebApplication ： 非Web应用环境下起效
     */


}
