package com.ciel.scaentity.entity;

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
@TableName("sca_role_permissions")
public class ScaRolePermissions implements Serializable {

    private static final long serialVersionUID = ScaBaseEntity.serialVersionUID;

    @TableField("ROLE_ID")
    private Long roleId;

    @TableField("PERMISSIONS_ID")
    private Long permissionsId;


}
