package com.ciel.scaentity.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public abstract class ScaBaseEntity implements Serializable {

    protected static final long serialVersionUID = 71L;

    @TableId(value = "ID", type = IdType.ASSIGN_ID)
    private Long id;


    /**
     * json 序列化策略 ; 使用string 防止js 精度丢掉问题  jack2
     */
    @JsonSerialize(using = ToStringSerializer.class)

    /**
     * json 序列化策略 ; 使用string 防止js 精度丢掉问题  fastjson
     */
    @JSONField(serializeUsing= com.alibaba.fastjson.serializer.ToStringSerializer.class)
    public Long getId() {
        return id;
    }


    @TableField(value = "CREATE_DATE", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @TableField(value = "UPDATE_DATE", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    @TableLogic
    @TableField(value = "DELETED", fill = FieldFill.INSERT)
    private Integer deleted;

    /**
     * 自身的json(str)属性, 转自身的object 属性;
     *
     * 例如 两个属性 private String personStr -> private Person person
     *
     *
     * //这个不太好,后期想法是直接 重写 getPerson() 方法,来完成转换
     * @param json
     * @param filed
     */
    public void toObject(String json,Object filed){
        filed = JSON.parseObject(json,filed.getClass());
    }
}
