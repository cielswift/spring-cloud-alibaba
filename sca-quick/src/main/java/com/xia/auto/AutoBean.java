package com.xia.auto;

import com.xia.bean.XiapeixinFas;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Slf4j
public class AutoBean {
    protected String name;

    protected Integer age;

    @Autowired
    protected XiapeixinFas xiapeixinFas;
}
