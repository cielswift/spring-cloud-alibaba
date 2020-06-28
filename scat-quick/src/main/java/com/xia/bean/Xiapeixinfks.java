package com.xia.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Slf4j
public class Xiapeixinfks {

    protected String name ;

    protected Byte age ;

    @Autowired
    protected XiapeixinFas xiapeixinfas;

}