package com.ciel.springcloudalibabacommons.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MybatisPlusAuto implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createDate", LocalDateTime.now(), metaObject); //mp自动填充
        this.setFieldValByName("deleted", 0, metaObject);
        this.setFieldValByName("updateDate", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.setFieldValByName("updateDate", LocalDateTime.now(), metaObject);
    }
}
