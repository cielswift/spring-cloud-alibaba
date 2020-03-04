package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scaapi.crud.IScaUserRoleService;
import com.ciel.scacommons.mapper.ScaUserRoleMapper;
import com.ciel.scaentity.entity.ScaUserRole;
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
public class ScaUserRoleServiceImpl extends ServiceImpl<ScaUserRoleMapper, ScaUserRole> implements IScaUserRoleService {

}
