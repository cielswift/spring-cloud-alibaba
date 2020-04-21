package com.ciel.scatquick;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.ciel.scatquick.anntion.SpiInterface;
import com.ciel.scatquick.init.AppScontext;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
@ComponentScan(basePackages = "com.ciel")
@MapperScan("com.ciel.scacommons.mapper")
@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE)
@Order(value=1) //配合CommandLineRunner 控制初始化顺序
public class ScatQuickApplication implements CommandLineRunner {

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(ScatQuickApplication.class);
        springApplication.addInitializers(new AppScontext());
        springApplication.run(args);

       // ConfigurableApplicationContext app = SpringApplication.run(ScatQuickApplication.class, args);
    }


    @Autowired
    protected ApplicationContext applicationContext;

    /**
     * 初始化任务
     *
     */
    @Override
    public void run(String... args) throws Exception {

        // git config --global user.name "你的名字"

//        File chm = new File(serverImagePath);
//        chm.setExecutable(true);//设置可执行权限
//        chm.setReadable(true);//设置可读权限
//        chm.setWritable(true);//设置可写权限
//
//        if (!System.getProperty("os.name").startsWith("Win")) { //设置文件权限, 执行终端/脚本命令
//            String cmdGrant = "chmod -R 777 " + serverImagePath;
//            Runtime.getRuntime().exec(cmdGrant);
//        }

        ServiceLoader<SpiInterface> serviceLoader = ServiceLoader.load(SpiInterface.class);

        serviceLoader.forEach(t -> {
            t.vue("a");
        });


        String reg = "\\d{2}\\w";

        String str = "29c";

        System.out.println(str.matches(reg));

        Pattern pa = Pattern.compile(reg);
        Matcher matcher = pa.matcher(str);

        if(matcher.find()){
            System.out.println(matcher.group(0));
        }

    }

}
