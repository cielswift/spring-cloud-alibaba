package com.ciel.scatquick.beanload;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;

/**
 * 全局bean加载的拦截方法
 *
 *  参考 org.springframework.context.support.AbstractApplicationContext.refresh()  preInstantiateSingletons
 *   getBean -> doGetBean -> createBean  已创建的bean 缓存到singletonObject (Map中)
 *  尝试给bean 创建代理bean
 *  org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#createBean(..) resolveBeforeInstantiation
 *
 *   InstantiationAwareBeanPostProcessor 是 BeanPostProcessor 的子接口
 *   SmartInstantiationAwareBeanPostProcessor 是 InstantiationAwareBeanPostProcessor  的子接口
 *
 *   registerBeanPostProcessors()：完成了所有后置处理器 BeanPostProcessor 的实例化，并注册到容器。
 *   finishBeanFactoryInitialization(beanFactory);：方法完成了对所有业务单例Bean的初始化，并注册到容器
 *
 *   finishBeanFactoryInitialization() 注册所有业务Bean
 *
 *   @Autowired的实现是通过AutowiredAnnotationBeanPostProcessor后置处理器中实现的
 *   buildAutowiringMetadata
 *------------------------------------------------------------------------------------------
 *
 *  在Spring的DefaultSingletonBeanRegistry类中，你会赫然发现类上方挂着这三个Map：
 * singletonObjects 它是我们最熟悉的朋友，俗称“单例池”“容器”，缓存创建完成单例Bean的地方。
 *
 * singletonFactories 映射创建Bean的原始工厂
 *
 * earlySingletonObjects 映射Bean的早期引用，也就是说在这个Map里的Bean不是完整的，甚至还不能称之为“Bean”，只是一个Instance.
 *
 *  singletonObjects：存放完成创建的Bean所有步骤的单实例Bean
 * earlySingletonObjects：存放只完成了创建Bean的第一步，且是由单实例工厂创建的Bean
 * singletonFactories：存放只完成了创建Bean的第一步后，提前暴露Bean的单实例工厂

 *
 * AutowiredAnnotationBeanPostProcessor：对注解@Autowired的实现
 * CommonAnnotationBeanPostProcessor： 对JSR-250的支持，如对 @Resource、@PostConstruct和@PreDestroy 的实现
 *
 */
@Component
public class BeanLoadInter  implements BeanPostProcessor,
        InstantiationAwareBeanPostProcessor,
        SmartInstantiationAwareBeanPostProcessor {

    /**
     * 初始化回调（例如InitializingBean的{@code afterPropertiesSet}或自定义的初始化方法）。
     * 该bean将已经用属性值填充 初始化bean之前，相当于把bean注入spring上下文之前
     *
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BEAN INIT LOAD BEFORE -> "+beanName);
        return bean;
    }

    /**
     * 该bean将已经用属性值填充
     *初始化bean之后，相当于把bean注入spring上下文之后
     *
     *
     * 后置处理 比如返回一个代理对象
     * AnnotationAwareAspectJAutoProxyCreator：AOP代理的后置处理器，
     * AOP生成代理的地方就是在后置处理器的postProcessAfterInitialization方法中实现的
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BEAN INIT LOAD AFTER -> "+beanName);
        return bean;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 实例化bean之前，相当于new这个bean之前
     *
     *
     * 如果此方法返回一个非null对象，则Bean创建过程 会短路。唯一的后续处理是postProcessAfterInitialization
     *
     * 返回 null 继续进行默认实例化
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    /**
     *实例化bean之后，相当于new这个bean之后
     *
     *
     * 但在发生Spring属性填充（通过显式属性或自动装配）之前
     * 这是在给定bean上执行自定义字段注入的理想回调
     * 正常返回true 返回false 也将阻止任何后续的InstantiationAwareBeanPostProcessor
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    /**
     * 在工厂应用它们之前对给定的属性值进行后处理到给定的bean
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**1
     * bean的类型；如果不可预测，则返回{@code null}
     *
     * 该触发点发生在postProcessBeforeInstantiation之前(在图上并没有标明，因为一般不太需要扩展这个点)，
     * 这个方法用于预测Bean的类型，返回第一个预测成功的Class类型，如果不能预测返回null；
     * 当你调用BeanFactory.getType(name)时当通过bean的名字无法得到bean类型信息时就调用该回调方法来决定类型信息
     *
     */
    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        return beanClass;
    }

    /**
     * 返回候选构造函数，如果未指定，则为{@code null
     *
     * 该触发点发生在postProcessBeforeInstantiation之后，用于确定该bean的构造函数之用，
     * 返回的是该bean的所有构造函数列表。用户可以扩展这个点，来自定义选择相应的构造器来实例化这个bean
     *
     */
    @Override
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    /**
     * 该触发点发生在postProcessAfterInstantiation之后，当有循环依赖的场景，当bean实例化好之后，
     * 为了防止有循环依赖，会提前暴露回调方法，用于bean实例化的后置处理。这个方法就是在提前暴露的回调方法中触发
     *
     */
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
