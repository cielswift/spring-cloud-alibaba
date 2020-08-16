package com.xia.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Slf4j

/**
 * 配合 @ConfigurationPropertiesScan("com.xia.bean") 不再需要@Configuration或者@Component
 * ignoreInvalidFields 类型错误的字段（或不能强制转换为正确类型的字段会被忽略
 * ignoreUnknownFields 未知字段应被忽略
 */
@ConfigurationProperties(prefix = "xiapeixinfis",ignoreInvalidFields=true,ignoreUnknownFields=true)

public class XiapeixinFis {

    protected String name ;
    protected Byte age ;
}
