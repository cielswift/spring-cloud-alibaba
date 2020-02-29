package com.ciel.springcloudalibabaapi.crud;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ciel.springcloudalibabaentity.entity.ScaUser;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
public interface IScaUserService extends IService<ScaUser> {


    public Object testTransaction();

    /////////////////////////////////////////////////////////////////////////////////


}
