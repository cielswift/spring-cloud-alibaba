package com.ciel.scatquick.beanload.springbean;

public class SpringInitOrder {

//    Spring 容器初始化流程大致流程如下：
//
//    this()：注册内置的BeanPostProcessor的BeanDefinition到容器
//    register(annotatedClasses)：注册配置类 BeanDefinition 到容器
//    prepareRefresh()：初始化前的准备工作，列如对系统属性或者环境变量进行验证
//    obtainFreshBeanFactory()：初始化 BeanFactory
//    prepareBeanFactory(beanFactory)：对 BeanFactory 进行各种功能的填充，比如对表达式的支持等
//    postProcessBeanFactory(beanFactory)：留给子类扩展用
//    invokeBeanFactoryPostProcessors(beanFactory)：执行BeanFactoryPostProcessor后置处理器
//    registerBeanPostProcessors(beanFactory)：创建并注册所有的BeanPostProcessor后置处理
//    initMessageSource()：初始化消息组件（国际化，消息绑定，消息解析）
//    initApplicationEventMulticaster()：初始化容器的事件机制
//    onRefresh()：初始化其他特殊Bean（留给子类做扩展用）
//    registerListeners()：注册监听器（ApplicationListener）
//    finishBeanFactoryInitialization(beanFactory)：创建并注册所有的单例且非懒加载的Bean
//    finishRefresh()：完成刷新过程，通知生命周期处理器 lifecycleProcessor 刷新过程，同时发出 ContextRefreshEvent 通知别人。
//    resetCommonCaches()：重置缓存
/////////////////////////////////////////////////////////////////////////////////////////////////////////
//Spring Bean的生命周期：
//
//    InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation()：实例化Bean的前置处理
//    createBeanInstance(beanName, mbd, args)：实例化Bean
//    MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition()：合并Bean定义信息（自动装配元素）
//    InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation()：实例化Bean的后置处理
//    InstantiationAwareBeanPostProcessor#postProcessPropertyValues()：Bean的自动装配
//    部分Aware接口的自动装配：BeanNameAware#setBeanName()、BeanClassLoaderAware#setBeanClassLoader()、BeanFactoryAware#setBeanFactory()
//    BeanPostProcessor#postProcessBeforeInitialization()：Bean初始化的前置处理
//    剩余部分Aware接口的自动装配：EnvironmentAware#setEnvironment()、EmbeddedValueResolverAware#setEmbeddedValueResolver()、ResourceLoaderAware#setResourceLoader ()、ApplicationEventPublisherAware#setApplicationEventPublisher ()、MessageSourceAware#setMessageSource ()、ApplicationContextAware# setApplicationContext ()
//    @PostConstruct：执行初始化方法
//    InitializingBean#afterPropertiesSet()：执行初始化方法
//    @Bean(initMethod = “initMethod”)：执行初始化方法
//    BeanPostProcessor#postProcessAfterInitialization()：Bean初始化的后置处理
//    DestructionAwareBeanPostProcessor#postProcessBeforeDestruction()：销毁Bean的后置处理（@PreDestroy）
//    DisposableBean#destroy()：执行销毁方法
//    @Bean(destroyMethod = “destroyMethod”)：执行销毁方法

///////////////////////////////////////////////////////////////////////////////////////
//Spring AOP执行流程：
//
//    @EnableAspectJAutoProxy开启对AOP的支持，注册后置处理器 AnnotationAwareAspectJAutoProxyCreator
//    根据Class对象找出所有切面类（有@Aspect注解的类）
//    解析切面类中的增强器（解析@Before等注解），并放入缓存中
//            根据ProxyFactory工厂类创建AopProxy代理器
//    根据AopProxy代理器创建代理类
//    通过增强器执行入口执行增强器 JdkDynamicAopProxy#invoke()或CglibAopProxy.DynamicAdvisedInterceptor#intercept()
//    获取链接链 AdvisedSupport#getInterceptorsAndDynamicInterceptionAdvice()
//    以递归方式执行拦截链 ReflectiveMethodInvocation.proceed()
//////////////////////////////////////////////////////////////////////////////
//Spring 循环依赖流程：
//
//!this.singletonsCurrentlyInCreation.remove(beanName)：将当前正创建Bean的标示放到Set集合中，如果失败则说明发生了循环创建，直接抛出BeanCurrentlyInCreationException异常，防止死循环
//addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory)：将完成实例化但是还未完成赋值的对象放到singletonFactories容器中，提前暴露出来
//getSingleton(String beanName, boolean allowEarlyReference)：当有对象创建过程中需要当前正在创建中的对象的时候就需要从singletonFactories容器中获取对象，这时我们可以通过后置处理器SmartInstantiationAwareBeanPostProcessor对获取到的对象进特殊处理，然后放到earlySingletonObjects容器中
//最后将创建完成的Bean放到singletonObject容器中
    ////////////////////////////////////////////////////////////////////////////////////////
//Spring Bean的自动装配过程：
//
//    根据Class对象，通过反射获取所有的Field和Method信息
//    通反射获取Field和Method的注解信息，并根据注解类型，判断是否需要自动装配
//    将需要自动装配的元素，封装成AutowiredFieldElement或AutowiredMethodElement对象
//    调用AutowiredFieldElement或AutowiredMethodElement的inject方法，唤起后续步骤
//    通过调用容器的getBean()方法找到需要注入的源数据Bean
//    通过反射将找到的源数据Bean注入到目标Bean中

}
