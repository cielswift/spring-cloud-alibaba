package com.ciel.springcloudalibabacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.springcloudalibabaapi.crud.IScaOrderService;
import com.ciel.springcloudalibabacommons.mapper.ScaOrderMapper;
import com.ciel.springcloudalibabaentity.entity.ScaOrder;
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
public class ScaOrderServiceImpl extends ServiceImpl<ScaOrderMapper, ScaOrder> implements IScaOrderService {

}
