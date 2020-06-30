package com.ciel.scaentity.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import com.ciel.scaentity.type2.Person;
import com.ciel.scaentity.type2.json2Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

/**
 *
 * @author xiapeixin
 * @since 2020-02-14
 */

//lombok 常用注解
//@AllArgsConstructor //所有参数的构造函数
//@NoArgsConstructor  //无参数的构造函数
//@Accessors(chain = true) //链式风格访问
//@RequiredArgsConstructor  //将标记为@NoNull的属性生成一个构造器
//@ToString //生成所有属性的toString()方法
//@EqualsAndHashCode(callSuper = false) //生成equals()方法和hashCode方法,callSuper使用父类继承的属性; 可通过参数exclude排除一些属性;可通过参数of指定仅使用哪些属性
//@Data //@Data相当于@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集

//校验注解
// @NotBlank	判断字符串是否为null 或者是空串(去掉首尾空格)
// @NotEmpty:	判断字符串是否null 或者是空串。
//@Length	判断字符的长度(最大或者最小)
//@Min	判断数值最小值
//@Max	判断数值最大值
//@Email	判断邮箱是否合法

//    @NotBlank(message="用户名不能为空")  //非空校验
//    @Length(min=3,max=6)
//   @Valid
//   @Validated

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sca_user",autoResultMap = true) //开启注释映射


//jackjson 常用注解

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
