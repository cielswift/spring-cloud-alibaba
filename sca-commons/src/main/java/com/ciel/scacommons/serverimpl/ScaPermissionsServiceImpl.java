package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scaapi.crud.IScaPermissionsService;
import com.ciel.scacommons.mapper.ScaPermissionsMapper;
import com.ciel.scaentity.entity.ScaPermissions;
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
public class ScaPermissionsServiceImpl extends ServiceImpl<ScaPermissionsMapper, ScaPermissions> implements IScaPermissionsService {

}
