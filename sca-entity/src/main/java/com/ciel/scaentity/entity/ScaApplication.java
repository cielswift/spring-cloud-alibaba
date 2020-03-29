package com.ciel.scaentity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sca_application")
public class ScaApplication extends ScaBaseEntity {

    @TableField("NAME")
    private String name;

    @TableField("USER_ID")
    private Long userId;


}
