package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scaapi.crud.IScaPermissionsService;
import com.ciel.scaapi.crud.IScaRolePermissionsService;
import com.ciel.scaapi.crud.IScaUserPermissionsService;
import com.ciel.scacommons.mapper.ScaPermissionsMapper;
import com.ciel.scaentity.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private IScaRolePermissionsService rolePermissionsService;

    @Autowired
    private IScaUserPermissionsService userPermissionsService;

    @Override
    public List<ScaPermissions> getByRoleIds(List<Long> ids) {

        if(ids.isEmpty()){
            return new ArrayList<ScaPermissions>();
        }
        List<ScaRolePermissions> list = rolePermissionsService.list(new LambdaQueryWrapper<ScaRolePermissions>()
                .select(ScaRolePermissions::getPermissionsId).in(ScaRolePermissions::getRoleId, ids));
        if(list.isEmpty()){
            return new ArrayList<ScaPermissions>();
        }
        return list(new LambdaQueryWrapper<ScaPermissions>().in(ScaPermissions::getId,list.stream()
                .map(ScaRolePermissions::getPermissionsId).collect(Collectors.toList())));
    }

    @Override
    public List<ScaPermissions> getByUserId(Long id) {
        List<ScaUserPermissions> list = userPermissionsService.list(new LambdaQueryWrapper<ScaUserPermissions>()
                .select(ScaUserPermissions::getPermissionsId).eq(ScaUserPermissions::getUserId, id));
        if(list.isEmpty()){
            return new ArrayList<ScaPermissions>();
        }
        return list(new LambdaQueryWrapper<ScaPermissions>().in(ScaPermissions::getId,list.stream()
                .map(ScaUserPermissions::getPermissionsId).collect(Collectors.toList())));

    }

    @Override
    public List<ScaPermissions> getByRoleId(Long id) {
        List<ScaRolePermissions> list = rolePermissionsService.list(new LambdaQueryWrapper<ScaRolePermissions>()
                .select(ScaRolePermissions::getPermissionsId).eq(ScaRolePermissions::getRoleId, id));
        if(list.isEmpty()){
            return new ArrayList<ScaPermissions>();
        }
        return list(new LambdaQueryWrapper<ScaPermissions>().in(ScaPermissions::getId,list.stream()
                .map(ScaRolePermissions::getPermissionsId).collect(Collectors.toList())));
    }
}
