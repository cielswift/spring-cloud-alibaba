package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scaapi.crud.IScaRoleService;
import com.ciel.scaapi.crud.IScaUserRoleService;
import com.ciel.scacommons.mapper.ScaRoleMapper;
import com.ciel.scaentity.entity.ScaRole;
import com.ciel.scaentity.entity.ScaUserRole;
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
public class ScaRoleServiceImpl extends ServiceImpl<ScaRoleMapper, ScaRole> implements IScaRoleService {

    @Autowired
    private IScaUserRoleService scaUserRoleService;

    @Override
    public List<ScaRole> rolesByUserId(Long id) {

        List<ScaUserRole> list = scaUserRoleService.list(new LambdaQueryWrapper<ScaUserRole>()
                .select(ScaUserRole::getRoleId).eq(ScaUserRole::getUserId, id));

        if(list.isEmpty()){
            return new ArrayList<ScaRole>();
        }

        return list(new LambdaQueryWrapper<ScaRole>()
                .in(ScaRole::getId, list.stream().map(ScaUserRole::getRoleId).collect(Collectors.toList())));
    }
}
