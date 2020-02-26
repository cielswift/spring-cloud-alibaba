package com.ciel.springcloudalibabacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.springcloudalibabaapi.crud.IScaRoleService;
import com.ciel.springcloudalibabacommons.mapper.ScaRoleMapper;
import com.ciel.springcloudalibabaentity.entity.ScaRole;
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
public class ScaRoleServiceImpl extends ServiceImpl<ScaRoleMapper, ScaRole> implements IScaRoleService {

}
