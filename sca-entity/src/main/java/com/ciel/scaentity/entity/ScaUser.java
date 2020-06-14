package com.ciel.scaentity.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;
import java.util.Date;

import com.ciel.scaentity.type2.Person;
import com.ciel.scaentity.type2.json2Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sca_user",autoResultMap = true) //开启注释映射


//@JsonIgnoreProperties，忽略一组属性，作用于类上，比如JsonIgnoreProperties({ "password", "age" })。

//@JsonSerialize，指定一个实现类来自定义序列化。类必须实现JsonSerializer接口，代码如下：
//@JsonDeserialize，用户自定义反序列化，同@JsonSerialize ，类需要实现JsonDeserializer接口。
public class ScaUser extends ScaBaseEntity {


    @TableField("USERNAME")
    private String username;

    @TableField("BIRTHDAY")
    private Date birthday;

    @TableField("SEX")
    private Integer sex;

    @TableField("IMAGE")
    private String image;

    @TableField("PASSWORD")
    @JsonIgnore  //返回时排除掉这个字段
    private String password;

    @TableField("PRICE")
    private BigDecimal price;

    @TableField("IP")
    private String ip;

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
