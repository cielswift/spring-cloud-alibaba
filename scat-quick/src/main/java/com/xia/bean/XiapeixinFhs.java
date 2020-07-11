package com.xia.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Slf4j
public class XiapeixinFhs {

    protected String name ="fhs";
    protected Byte age = 20;

    @Autowired
    protected XiapeixinFas xiapeixinFas;
}
