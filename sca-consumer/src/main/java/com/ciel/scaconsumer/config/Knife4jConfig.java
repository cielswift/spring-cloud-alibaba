package com.ciel.scaconsumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Knife4jConfig {

    /**
     * 接口文档
     */
    // http://127.0.0.1:5011/producer20/doc.html#/home
    /**
     * 如果404 需要
     * <p>
     * public class SwaggerBootstrapUiDemoApplication  implements WebMvcConfigurer{
     *
     * @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
     * registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
     * registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
     * }
     * }
     */

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ciel.scaconsumer.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("夏培鑫的标题")
                .description("没有什么详情")
                .termsOfServiceUrl("http://没有条款地址:65537/")
                .contact(contact())
                .version("1.0")
                .build();
    }

    private Contact contact() {
        return new Contact("夏培鑫", "https://cielswift.github.io/", "15966504931@163.com");
    }


}
