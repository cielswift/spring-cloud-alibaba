package com.ciel.springcloudalibabaentity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sca_user_permissions")
public class ScaUserPermissions implements Serializable {

    private static final long serialVersionUID = ScaBaseEntity.serialVersionUID;

    @TableField("USER_ID")
    private Long userId;

    @TableField("PERMISSIONS_ID")
    private Long permissionsId;


}
