package com.ciel.springcloudalibabacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.springcloudalibabaapi.crud.IScaUserService;
import com.ciel.springcloudalibabacommons.mapper.ScaUserMapper;
import com.ciel.springcloudalibabaentity.ScaUser;
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
public class ScaUserServiceImpl extends ServiceImpl<ScaUserMapper, ScaUser> implements IScaUserService {

}
