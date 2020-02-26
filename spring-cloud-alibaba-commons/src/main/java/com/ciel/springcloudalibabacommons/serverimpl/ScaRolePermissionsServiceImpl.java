package com.ciel.springcloudalibabacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.springcloudalibabaapi.crud.IScaRolePermissionsService;
import com.ciel.springcloudalibabacommons.mapper.ScaRolePermissionsMapper;
import com.ciel.springcloudalibabaentity.entity.ScaRolePermissions;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
@Service
public class ScaRolePermissionsServiceImpl extends ServiceImpl<ScaRolePermissionsMapper, ScaRolePermissions> implements IScaRolePermissionsService {

}
