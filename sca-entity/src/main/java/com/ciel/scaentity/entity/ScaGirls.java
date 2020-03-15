package com.ciel.scaentity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sca_girls")
public class ScaGirls implements Serializable {

    protected static final long serialVersionUID = 71L;

    @TableId(value = "ID",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("NAME")
    private String name;

    @TableField("PRICE")
    private BigDecimal price;

    @TableField("USER_ID")
    private Long userId;

}
