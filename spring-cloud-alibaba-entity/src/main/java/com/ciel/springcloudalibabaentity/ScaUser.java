package com.ciel.springcloudalibabaentity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
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
@TableName("sca_user")
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
}
