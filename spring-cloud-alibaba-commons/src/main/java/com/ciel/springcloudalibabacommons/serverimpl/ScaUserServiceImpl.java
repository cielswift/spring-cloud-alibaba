package com.ciel.springcloudalibabacommons.serverimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.springcloudalibabaapi.crud.IScaApplicationService;
import com.ciel.springcloudalibabaapi.crud.IScaUserService;
import com.ciel.springcloudalibabacommons.mapper.ScaUserMapper;
import com.ciel.springcloudalibabaentity.entity.ScaApplication;
import com.ciel.springcloudalibabaentity.entity.ScaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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

    @Autowired
    protected IScaApplicationService applicationService;




    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object testTransaction() {
        remove(new LambdaQueryWrapper<ScaUser>().eq(ScaUser::getUsername,"aaa"));

        applicationService.remove(new LambdaQueryWrapper<ScaApplication>().eq(ScaApplication::getName,"aaa"));

        return "yes";
    }


    //////////////////////////////////////////////////////

}
