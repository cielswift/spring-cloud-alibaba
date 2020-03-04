package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scaapi.crud.IScaOrderService;
import com.ciel.scacommons.mapper.ScaOrderMapper;
import com.ciel.scaentity.entity.ScaOrder;
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
