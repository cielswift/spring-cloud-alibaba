package com.ciel.scaapi.crud;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ciel.scaentity.entity.ScaUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
public interface IScaUserService extends IService<ScaUser> {

    List<ScaUser> lists(String name);

    boolean deleteByName(String name);

    public Object testTransaction();

    /////////////////////////////////////////////////////////////////////////////////

    ScaUser getByName(String name);


    ScaUser getByIp(String ip);

}
