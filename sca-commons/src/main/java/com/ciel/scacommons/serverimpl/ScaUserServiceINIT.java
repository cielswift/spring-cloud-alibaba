package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scacommons.mapper.*;
import com.ciel.scaentity.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * USER 登陆用;避免事务失效,单独写一个;
 */
@Service
@AllArgsConstructor
public class ScaUserServiceINIT extends ServiceImpl<ScaUserMapper, ScaUser> {

    protected ScaUserRoleMapper userRoleMapper;
    protected ScaRoleMapper roleMapper;

    protected ScaUserPermissionsMapper userPermissionsMapper;
    protected ScaPermissionsMapper permissionsMapper;

    protected ScaRolePermissionsMapper rolePermissionsMapper;

    public ScaUser userByName(String name){
        return baseMapper.selectOne(new LambdaQueryWrapper<ScaUser>().eq(ScaUser::getUsername,name));
    }

    public List<ScaRole> rolesByuId(Long uid){
        List<ScaUserRole> userRoles =
                userRoleMapper.selectList(new LambdaQueryWrapper<ScaUserRole>().select(ScaUserRole::getRoleId).eq(ScaUserRole::getUserId, uid));
        if(userRoles.isEmpty()){
            return new ArrayList<ScaRole>();
        }
      return  roleMapper.selectList(new LambdaQueryWrapper<ScaRole>().
              in(ScaRole::getId, userRoles.stream().map(ScaUserRole::getRoleId).collect(Collectors.toList())));
    }

    public List<ScaPermissions> permissionsByuId(Long uid){

        List<ScaPermissions> psall = new ArrayList<>();

        List<ScaUserPermissions> permissions =
                userPermissionsMapper.selectList(new LambdaQueryWrapper<ScaUserPermissions>().select(ScaUserPermissions::getPermissionsId).eq(ScaUserPermissions::getUserId, uid));

        if(!permissions.isEmpty()){
            psall.addAll(permissionsMapper.selectList(new LambdaQueryWrapper<ScaPermissions>().
                    in(ScaPermissions::getId, permissions.stream().map(ScaUserPermissions::getPermissionsId)
                            .collect(Collectors.toList()))));
        }

        List<ScaRole> roles = rolesByuId(uid);
        if(!roles.isEmpty()){
            psall.addAll(permissionsMapper.byRoles(roles));
        }

        return psall.stream().distinct().collect(Collectors.toList());
    }


}
