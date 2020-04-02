//package com.ciel.scatquick.s2d;
//
//import com.github.dozermapper.core.DozerBeanMapperBuilder;
//import com.github.dozermapper.core.Mapper;
//import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
//import com.github.dozermapper.core.loader.api.TypeMappingOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DozerConfig {
//
//    @Bean
//    public Mapper dozerMapper(){
//        Mapper mapper = DozerBeanMapperBuilder.create()
//                //指定 dozer mapping 的配置文件(放到 resources 类路径下即可)，可添加多个 xml 文件，用逗号隔开
//                .withMappingFiles("dozerBeanMapping.xml")
//                .withMappingBuilder(beanMappingBuilder())
//                .build();
//        return mapper;
//    }
//
//    @Bean
//    public BeanMappingBuilder beanMappingBuilder() {
//        return new BeanMappingBuilder() {
//            @Override
//            protected void configure() {
//                // 个性化配置添加在此
//
//                mapping(StudentDomain.class, StudentVo.class).fields("address", "addr");
//
//                //关闭隐式匹配
//                mapping(StudentDomain.class, StudentVo.class, TypeMappingOptions.wildcard(false))
//                        .fields("address", "addr");
//
//                mapping(StudentDomain.class, StudentVo.class)
//                        .exclude("mobile") //exclude 方法排除不想映射的字段修改 configure
//                        .fields("address", "addr");
//            }
//        };
//    }
//}