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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

/**
 * 指定缓存通用配置; cacheManager指定使用哪一个缓存管理器
 */
@CacheConfig(cacheNames = "scaUser",cacheManager = "cacheManagerJSON")
public class ScaUserServiceImpl extends ServiceImpl<ScaUserMapper, ScaUser> implements IScaUserService {

    @Autowired
    protected IScaApplicationService applicationService;

    @Override
    @Cacheable(value = "scaUser", keyGenerator = "autoGenMy")
    public List<ScaUser> lists(String name) {
        return baseMapper.selectList(null);
    }

    @Override
    @CachePut(value = "scaUser",keyGenerator = "autoGenMy")
    public boolean deleteByName(String name) {
        return baseMapper.delete(new LambdaQueryWrapper<ScaUser>().eq(ScaUser::getUsername,name)) > 1;
    }

    @Override
    public ScaUser getByName(String name) {
       return baseMapper.selectOne(new LambdaQueryWrapper<ScaUser>().eq(ScaUser::getUsername,name));
    }

    //////////////////////////////////////////////////////


    @Override
    public ScaUser getByIp(String ip) {
        return baseMapper.selectOne(new LambdaQueryWrapper<ScaUser>().eq(ScaUser::getIp,ip));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object testTransaction() {
        remove(new LambdaQueryWrapper<ScaUser>().eq(ScaUser::getUsername,"aaa"));
        applicationService.remove(new LambdaQueryWrapper<ScaApplication>().eq(ScaApplication::getName,"aaa"));
        return "yes";
    }
}
