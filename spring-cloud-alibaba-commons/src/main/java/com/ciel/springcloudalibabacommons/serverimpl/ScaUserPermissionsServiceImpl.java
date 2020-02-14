package com.ciel.springcloudalibabacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.springcloudalibabaapi.crud.IScaUserPermissionsService;
import com.ciel.springcloudalibabacommons.mapper.ScaUserPermissionsMapper;
import com.ciel.springcloudalibabaentity.ScaUserPermissions;
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
public class ScaUserPermissionsServiceImpl extends ServiceImpl<ScaUserPermissionsMapper, ScaUserPermissions> implements IScaUserPermissionsService {

}
