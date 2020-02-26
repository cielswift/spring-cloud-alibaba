package com.ciel.springcloudalibabaentity.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.ciel.springcloudalibabaentity.type2.Person;
import com.ciel.springcloudalibabaentity.type2.json2Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "sca_user",autoResultMap = true) //开启注释映射
public class ScaUser extends ScaBaseEntity {

    @TableField("USERNAME")
    private String username;

    @TableField("BIRTHDAY")
    private LocalDate birthday;

    @TableField("SEX")
    private Boolean sex;

    @TableField("IMAGE")
    private String image;

    @TableField("PASSWORD")
    private String password;

    @TableField("PRICE")
    private BigDecimal price;

    /**
     * 乐观锁
     */
    @TableField(value = "VERSION",fill = FieldFill.INSERT)
    @Version
    private Integer version;

    //@TableField(typeHandler = JacksonTypeHandler.class)

    /**
     * 类型转换器 ,要开启映射@TableName(value = "sca_user",autoResultMap = true) //开启注释映射
     */
    @TableField(typeHandler = json2Person.class)
    private Person person;
}
