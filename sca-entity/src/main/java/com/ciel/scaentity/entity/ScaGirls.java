package com.ciel.scaentity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sca_girls_")
public class ScaGirls implements Serializable {

    protected static final long serialVersionUID = 1L;

    @TableId(value = "ID",type = IdType.ASSIGN_ID)
    /**
     * json 序列化策略 ; 使用string 防止js 精度丢掉问题
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("NAME")
    private String name;

    @TableField("PRICE")
    private BigDecimal price;

    @TableField("USER_ID")
    private Long userId;

    @TableField("BIRTHDAY")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date birthday;

    @TableField("IMGS")
    private String imgs;

    @TableField("IS_ONLINE")
    private Integer isOnline = 1;

}
