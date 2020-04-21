package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scaapi.crud.IScaApplicationService;
import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scacommons.mapper.ScaUserMapper;
import com.ciel.scaentity.entity.ScaApplication;
import com.ciel.scaentity.entity.ScaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
@Service
@Primary
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


    @Override
    public ScaUser getByName(String name) {
       return baseMapper.selectOne(new LambdaQueryWrapper<ScaUser>().eq(ScaUser::getUsername,name));
    }

    //////////////////////////////////////////////////////

}
