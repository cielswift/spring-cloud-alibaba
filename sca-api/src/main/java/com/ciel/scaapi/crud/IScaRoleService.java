package com.ciel.scaapi.crud;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ciel.scaentity.entity.ScaRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
public interface IScaRoleService extends IService<ScaRole> {


    List<ScaRole> rolesByUserId(Long id);
}
