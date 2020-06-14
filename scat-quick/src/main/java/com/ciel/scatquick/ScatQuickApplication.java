package com.ciel.scatquick;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.ciel.scatquick.aoptxspi.SpiInterface;
import com.ciel.scatquick.init.AppInitializer;
import com.ciel.scatquick.proxy.CglibProxyFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
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

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)

@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@ComponentScan(basePackages = "com.ciel")
@MapperScan("com.ciel.scacommons.mapper")
@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE)
@Order(value = 1) //配合CommandLineRunner 控制初始化顺序

@ConfigurationPropertiesScan("com.ciel.scatquick.guava")
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
     * 初始化任务
     */
    @Override
    public void run(String... args) throws Exception {

        System.out.println("INIT ORDER 1");

        //设置git 提交的名字
        // git config --global user.name "你的名字"

        //设置文件权限
//        if (!System.getProperty("os.name").startsWith("Win")) { //设置文件权限, 执行终端/脚本命令
//            String cmdGrant = "chmod -R 777 " + serverImagePath;
//            Runtime.getRuntime().exec(cmdGrant);
//        }

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

        InputStream resourceAsStream = ScatQuickApplication.class.getResourceAsStream("./ScatQuickApplication.class");

        byte[] temp = new byte[1024*1024];
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
        for (Method me: methods){
            String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(me);
            System.out.println("METHOD_NAME:"+me.getName());

            if(parameterNames != null){
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
