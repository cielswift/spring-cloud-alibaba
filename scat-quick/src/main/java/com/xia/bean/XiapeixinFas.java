package com.xia.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * 测试 @ImportResource(locations = "classpath:./sources/app-other.xml")
 */
@Data
@Slf4j
public class XiapeixinFas {

    protected String name;

    protected Byte age;

}
