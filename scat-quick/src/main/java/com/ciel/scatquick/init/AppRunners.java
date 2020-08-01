package com.ciel.scatquick.init;

import com.ciel.scaentity.entity.ScaGirls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 初始化
 */
@Order(3)
@Configuration
/**
 * ApplicationRunner 和  CommandLineRunner 基本相同
 * 需要bean被加载才会调用
 */
public class AppRunners implements ApplicationRunner,CommandLineRunner {

    @Autowired
    protected ApplicationContext applicationContext;



//    @Query(value = "SELECT p.* from sca_permissions p left join sca_role_permissions rp on p.id = rp.PERMISSIONS_ID where rp.ROLE_ID = 1",nativeQuery = true);
//    public List aa(){
//        return null;
//    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("INIT ORDER 3 -> CommandLineRunner");

        //删除所有权限;
      //  iScaGirlsService.remove(null);

        Map<String, Object> annotation = applicationContext.getBeansWithAnnotation(RestController.class);
        TreeSet<String> premmis = new TreeSet<>();
        annotation.forEach((x,y) -> {
            Class<?> yClass = y.getClass();
            Method[] methods = yClass.getMethods();
            for (Method method : methods) {
                GetMapping mapping = method.getAnnotation(GetMapping.class);
                if(mapping != null && mapping.value() != null) {
                    premmis.add(mapping.value()[0]);
                }
            }
        });

        Set<ScaGirls> collect = premmis.stream().map(x -> {
            //    new Press();
            return new ScaGirls();
        }).collect(Collectors.toSet());

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("INIT ORDER 3 -> ApplicationRunner");
    }

}
