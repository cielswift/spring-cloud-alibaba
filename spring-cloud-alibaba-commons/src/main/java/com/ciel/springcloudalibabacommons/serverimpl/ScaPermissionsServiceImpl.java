package com.ciel.springcloudalibabacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.springcloudalibabaapi.crud.IScaPermissionsService;
import com.ciel.springcloudalibabacommons.mapper.ScaPermissionsMapper;
import com.ciel.springcloudalibabaentity.ScaPermissions;
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
public class ScaPermissionsServiceImpl extends ServiceImpl<ScaPermissionsMapper, ScaPermissions> implements IScaPermissionsService {

}
