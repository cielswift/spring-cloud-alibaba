package com.ciel.springcloudalibabaentity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
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

}
