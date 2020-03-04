package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scaapi.crud.IScaUserPermissionsService;
import com.ciel.scacommons.mapper.ScaUserPermissionsMapper;
import com.ciel.scaentity.entity.ScaUserPermissions;
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
public class ScaUserPermissionsServiceImpl extends ServiceImpl<ScaUserPermissionsMapper, ScaUserPermissions> implements IScaUserPermissionsService {

}
