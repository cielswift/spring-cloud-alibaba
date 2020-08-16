package com.xia.contional;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

@Data
@Slf4j
//@Order(0)  //指定Condition的顺序
public class WindowsSystem implements Condition , Ordered {

    //指定Condition的顺序
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 返回true 生效
     *
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        printSystem();


        /**
         * 返回bean定义注册器，可以通过注册器获取bean定义的各种配置信息
         */
        //BeanDefinitionRegistry getRegistry();

        /**
         * 返回ConfigurableListableBeanFactory类型的bean工厂，相当于一个ioc容器对象
         */
        //ConfigurableListableBeanFactory getBeanFactory();

        /**
         * 返回当前spring容器的环境配置信息对象
         */
        //Environment getEnvironment();

        /**
         * 返回资源加载器
         */
        //ResourceLoader getResourceLoader();

        /**
         * 返回类加载器
         */
        //ClassLoader getClassLoader();

        return true;
    }

    public void printSystem() {
        try {
            Runtime r = Runtime.getRuntime();
            Properties props = System.getProperties();
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();
            Map<String, String> map = System.getenv();
            String userName = map.get("USERNAME");// 获取用户名
            String computerName = map.get("COMPUTERNAME");// 获取计算机名
            String userDomain = map.get("USERDOMAIN");// 获取计算机域名
            System.out.println("用户名:    " + userName);
            System.out.println("计算机名:    " + computerName);
            System.out.println("计算机域名:    " + userDomain);
            System.out.println("本地ip地址:    " + ip);
            System.out.println("本地主机名:    " + addr.getHostName());
            System.out.println("JVM可以使用的总内存:    " + r.totalMemory());
            System.out.println("JVM可以使用的剩余内存:    " + r.freeMemory());
            System.out.println("JVM可以使用的处理器个数:    " + r.availableProcessors());
            System.out.println("Java的运行环境版本：    " + props.getProperty("java.version"));
            System.out.println("Java的运行环境供应商：    " + props.getProperty("java.vendor"));
            System.out.println("Java供应商的URL：    " + props.getProperty("java.vendor.url"));
            System.out.println("Java的安装路径：    " + props.getProperty("java.home"));
            System.out.println("Java的虚拟机规范版本：    " + props.getProperty("java.vm.specification.version"));
            System.out.println("Java的虚拟机规范供应商：    " + props.getProperty("java.vm.specification.vendor"));
            System.out.println("Java的虚拟机规范名称：    " + props.getProperty("java.vm.specification.name"));
            System.out.println("Java的虚拟机实现版本：    " + props.getProperty("java.vm.version"));
            System.out.println("Java的虚拟机实现供应商：    " + props.getProperty("java.vm.vendor"));
            System.out.println("Java的虚拟机实现名称：    " + props.getProperty("java.vm.name"));
            System.out.println("Java运行时环境规范版本：    " + props.getProperty("java.specification.version"));
            System.out.println("Java运行时环境规范供应商：    " + props.getProperty("java.specification.vender"));
            System.out.println("Java运行时环境规范名称：    " + props.getProperty("java.specification.name"));
            System.out.println("Java的类格式版本号：    " + props.getProperty("java.class.version"));
            System.out.println("Java的类路径：    " + props.getProperty("java.class.path"));
            System.out.println("加载库时搜索的路径列表：    " + props.getProperty("java.library.path"));
            System.out.println("默认的临时文件路径：    " + props.getProperty("java.io.tmpdir"));
            System.out.println("一个或多个扩展目录的路径：    " + props.getProperty("java.ext.dirs"));
            System.out.println("操作系统的名称：    " + props.getProperty("os.name"));
            System.out.println("操作系统的构架：    " + props.getProperty("os.arch"));
            System.out.println("操作系统的版本：    " + props.getProperty("os.version"));
            System.out.println("文件分隔符：    " + props.getProperty("file.separator"));
            System.out.println("路径分隔符：    " + props.getProperty("path.separator"));
            System.out.println("行分隔符：    " + props.getProperty("line.separator"));
            System.out.println("用户的账户名称：    " + props.getProperty("user.name"));
            System.out.println("用户的主目录：    " + props.getProperty("user.home"));
            System.out.println("用户的当前工作目录：    " + props.getProperty("user.dir"));
        }catch (Exception e){

        }
    }
}
