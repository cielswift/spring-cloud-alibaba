package com.ciel.scatquick;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.ciel.scatquick.anntion.RepAction;
import com.ciel.scatquick.aoptxspi.SpiInterface;
import com.ciel.scatquick.beanload.boot.TypeFilterCustom;
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
import com.xia.config.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
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
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
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
@Import({XiapeixinFcs.class, ImportSelectTest.class, ImportBeanDefinitionRegistrarTest.class, ConfigurationTest.class})

/**
 * 导入其他配置类
 */
@ImportAutoConfiguration(ImportAutoConfigurationTest.class)

/**
 * 自动扫描配置类,不再需要@Configuration或者@Component
 */
@ConfigurationPropertiesScan("com.xia.bean")

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
        springApplication.run(args);

        // ConfigurableApplicationContext app = SpringApplication.run(ScatQuickApplication.class, args);
    }

    /**
     * redis
     */
    @Bean
    @Lazy
    @Primary
    public RedisTemplate<String, Object> redisString(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // redisTemplate.setDefaultSerializer(RedisSerializer.string());
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());

        //下面代码解决LocalDateTime序列化与反序列化不一致问题
        Jackson2JsonRedisSerializer<Object> j2jrs = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 解决jackson2无法反序列化LocalDateTime的问题
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        om.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        j2jrs.setObjectMapper(om);
        // 序列化 value 时使用此序列化方法
        redisTemplate.setValueSerializer(j2jrs);
        redisTemplate.setHashValueSerializer(j2jrs);
        return redisTemplate;
    }

    @Autowired
    protected ApplicationContext applicationContext;

    /**
     * spring的上下文对象,可以直接获取bean
     */
    @Autowired
    protected AutowireCapableBeanFactory beanFactory;

    /**
     * 初始化任务
     */
    @Override
    public void run(String... args) throws Exception {

        System.out.println("INIT ORDER 1");

        //beanFactory.autowireBean(new ScaGirls());  //给未被ioc管理的对象注入属性

        // @Configurable(preConstruction = true) //这个注解的作用是：告诉Spring在构造函数运行之前将依赖注入到对象中
        //这就就算当前这个对象是new出来的, 内部的字段也能使用@Autowired自动注入了; 需要aspectj 和启动aspectjAOP;


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

        InputStream resourceAsStream =
                ScatQuickApplication.class.getResourceAsStream("./ScatQuickApplication.class");

        byte[] temp = new byte[1024 * 1024];
        resourceAsStream.read(temp);
        System.out.println(new String(temp));

        URL resource2 = ScatQuickApplication.class.getClassLoader().getResource("./");
        //在使用 ClassLoader().getResource 获取路径时，不能以 "/" 开头，且路径总是从 classpath 根路径开始；
        System.out.println(resource2);
        //获取类路径下文件
        File cfgFile = ResourceUtils.getFile("classpath:bootstrap.yml");
        //获取配置文件的配置信息;
        //    @Autowired
        ConfigurableApplicationContext context;
        //context.getEnvironment().getProperty("org.dromara.hmily.serializer")

        /**
         * 获取方法参数名称
         */
        Method[] methods = CglibProxyFactory.class.getMethods();
        for (Method me : methods) {
            String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(me);
            System.out.println("METHOD_NAME:" + me.getName());

            if (parameterNames != null) {
                Arrays.stream(parameterNames).forEach(System.out::print);
            }
        }

//----------------------------------------------------------------------------------------------------------

        //正则表达式
        String reg = "\\d{2}\\w";
        String str = "29c";
        System.out.println(str.matches(reg));
        Pattern pa = Pattern.compile(reg);
        Matcher matcher = pa.matcher(str);

        if (matcher.find()) {
            //System.out.println(matcher.group(0));
        }
    }
}
