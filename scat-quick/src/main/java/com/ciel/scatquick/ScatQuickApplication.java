package com.ciel.scatquick;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.ciel.scatquick.algorithm.LockMid;
import com.ciel.scatquick.aoptxspi.SpiInterface;
import com.ciel.scatquick.beanload.AppListener;
import com.ciel.scatquick.init.AppInitializer;
import com.ciel.scatquick.proxy.CglibProxyFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xia.bean.XiapeixinFcs;
import com.xia.bean.XiapexinFjs;
import com.xia.config.*;
import org.apache.commons.io.IOUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 排除 DruidDataSourceAutoConfigure 自动配置
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
/**
 * 开启基于注解的aop
 * exposeProxy=true 表示通过aop框架暴露该代理对象，aopContext能够访问;
 *      然后就可以方法里获取当前类的代理对象;
 *          private HelloServiceImpl getHelloServiceImpl() {
 *           return AopContext.currentProxy() != null ? (HelloServiceImpl) AopContext.currentProxy() : this;
 *          }
 *  proxyTargetClass=true 使用cglib进行代理
 */
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
/**
 * basePackages 扫描的位置
 * excludeFilters 排除 FilterType.ANNOTATION 按注解排除 ,FilterType.ASSIGNABLE_TYPE,明确指定类 ,FilterType.REGEX,类名满足表达式
 * includeFilters 包含 FilterType.CUSTOM 自定义
 * useDefaultFilters = false 禁用默认规则 也就是不在扫描 @Component 等
 */
//@ComponentScan(basePackages = "com.ciel",
//        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = {RepAction.class})},
//        includeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM,value = TypeFilterCustom.class)}
//        /*,useDefaultFilters = false */  )
@ComponentScan(basePackages = "com.ciel")
/**
 * 导入其他xml 配置文件
 */
@ImportResource(locations = "classpath:./sources/app-other.xml")

/**
 * 导入其他 bean 或 配置 类
 */
@Import({XiapeixinFcs.class, ImportSelectTest.class, ImportBeanDefinitionRegistrarTest.class,
        ConfigurationTest.class,ContionalConfig.class})

/**
 * 导入其他配置类
 */
@ImportAutoConfiguration(ImportAutoConfigurationTest.class)

/**
 * 自动扫描配置类,不再需要@Configuration或者@Component
 */
@ConfigurationPropertiesScan("com.xia.bean")

/**
 * 导入其他配置文件 Proper格式
 */
@PropertySource(value = {"classpath:./sources/xiapeixin.properties"})
/**
 * 自定义自动装配
 */
@EnableXiapeixnTest
/**
 * 扫描 mapper
 */
@MapperScan("com.ciel.scacommons.mapper")
/**
 * @Transactional 注解应该只被应用到 public 方法上
 *
 *  默认情况下，只有来自外部的方法调用才会被AOP代理捕获，
 *  也就是，类内部方法调用本类内部的其他方法并不会引起事务行为，即使被调用方法使用@Transactional注解进行修饰。
 *
 *  开启事务,order指定aop的执行顺序,在其他aop(cache)之前执行;
 * aop 执行属顺序, 子类代理 , mode ASPECTJ 方式
 */
@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE,proxyTargetClass = true)

/**
 * 配合CommandLineRunner 控制初始化顺序
 */
@Order(value = 1)
public class ScatQuickApplication implements CommandLineRunner {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(ScatQuickApplication.class);

        springApplication.addInitializers(new AppInitializer()); //添加初始化
        springApplication.addListeners(new AppListener()); //添加事件监听器

        springApplication.run(args);

        // ConfigurableApplicationContext app = SpringApplication.run(ScatQuickApplication.class, args);
    }

    @Autowired
    protected ApplicationContext applicationContext;
    /**
     * spring的上下文对象,可以直接获取bean
     */
    @Autowired
    protected AutowireCapableBeanFactory beanFactory;

    @Autowired
    protected ConfigurableApplicationContext configurableApplicationContext;

    /**
     * 初始化任务
     */
    @Override
    public void run(String... args) throws Exception {

        System.out.println("INIT ORDER 1");

        //beanFactory.autowireBean(new ScaGirls());  //给未被ioc管理的对象注入属性 属性必须带@Autowrite注解

        // @Configurable(preConstruction = true) //这个注解的作用是：告诉Spring在构造函数运行之前将依赖注入到对象中
        //这就就算当前这个对象是new出来的, 内部的字段也能使用@Autowired自动注入了; 需要aspectj 和启动aspectjAOP;

        //-----------动态注册bean--------------------------------------------------------------------------------
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory =
                (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();

        // 通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(XiapexinFjs.class);

        // 设置属性userService,此属性引用已经定义的bean:userService,这里userService已经被spring容器管理了.
        beanDefinitionBuilder.addPropertyReference("xiapeixinfas", "xiapeixinfas");
        // 注册bean
        defaultListableBeanFactory.registerBeanDefinition("xiapexinfjs", beanDefinitionBuilder.getRawBeanDefinition());

        //删除bean.
        //defaultListableBeanFactory.removeBeanDefinition("xiapexinfjs");

        //---------动态注册bean--------------------------------------------------------------------------------

        //设置git 提交的名字
        // git config --global user.name "你的名字"

        //spi机制
        ServiceLoader<SpiInterface> serviceLoader = ServiceLoader.load(SpiInterface.class);
        serviceLoader.forEach(t -> {
            t.vue("TEST");
        });

//---------------------------------------------------------------------------------------------------
        //类资源加载器
        URL resource = ScatQuickApplication.class.getResource("./"); //类所在路径,可以使用 ../../切换
        URL resource1 = ScatQuickApplication.class.getResource("/"); //classpath目录下
        System.out.println(resource);
        System.out.println(resource1);

        //直接获取inputstream
        InputStream resourceAsStream =
                ScatQuickApplication.class.getResourceAsStream("./ScatQuickApplication.class");
        IOUtils.readLines(resourceAsStream,Charset.forName("utf-8")).forEach(System.err::println);

        //在使用 ClassLoader().getResource 获取路径时，不能以 "/" 开头，且路径总是从 classpath 根路径开始；
        URL resource2 = ScatQuickApplication.class.getClassLoader().getResource("./");
        System.out.println(resource2);

        //获取类路径下文件 //打jar 包报错 这是因为打包后Spring试图访问文件系统路径，但无法访问JAR中的路径。
       // File cfgFile = ResourceUtils.getFile("classpath:./logback-spring.xml");

        //获取类路径下文件
        ClassPathResource classPathResource = new ClassPathResource("./logback-spring.xml");
        InputStream inputStream = classPathResource.getInputStream();
        IOUtils.readLines(inputStream, Charset.forName("utf-8")).forEach(System.out::println);

        //获取配置文件的配置信息;
        // @Autowired
        ConfigurableApplicationContext context;
        //context.getEnvironment().getProperty("org.dromara.hmily.serializer")

        /**
         * 获取方法参数名称
         */
        Method[] methods = LockMid.class.getMethods();
        for (Method me : methods) {

            //获取方法参数名称
            String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(me);
            System.out.println("METHOD_NAME:" + me.getName());
            if (parameterNames != null) {
                Arrays.stream(parameterNames).forEach(System.out::print);
            }
        }

    }
}
