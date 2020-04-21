package com.ciel.scaapi.crud;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ciel.scaentity.entity.ScaPermissions;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
public interface IScaPermissionsService extends IService<ScaPermissions> {

    /**
     * 根据角色ids获取权限
     * @param id
     * @return
     */
    List<ScaPermissions> getByRoleIds(List<Long> ids);

    /**
     * 根据角色ids获取权限
     * @param id
     * @return
     */
    List<ScaPermissions> getByRoleId(Long id);

    /**
     * 根据用户id获取权限
     * @param id
     * @return
     */
    List<ScaPermissions> getByUserId(Long id);
}
