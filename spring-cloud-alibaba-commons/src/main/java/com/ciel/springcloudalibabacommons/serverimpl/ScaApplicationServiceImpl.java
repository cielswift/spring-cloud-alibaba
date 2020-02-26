package com.ciel.springcloudalibabacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.springcloudalibabaapi.crud.IScaApplicationService;
import com.ciel.springcloudalibabacommons.mapper.ScaApplicationMapper;
import com.ciel.springcloudalibabaentity.entity.ScaApplication;
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
public class ScaApplicationServiceImpl extends ServiceImpl<ScaApplicationMapper, ScaApplication> implements IScaApplicationService {

}
