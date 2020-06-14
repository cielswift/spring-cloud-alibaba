package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scaapi.crud.IScaDictService;
import com.ciel.scacommons.mapper.ScaDictMapper;
import com.ciel.scaentity.entity.ScaDict;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiapeixin
 * @since 2020-06-14
 */
@Service
public class ScaDictServiceImpl extends ServiceImpl<ScaDictMapper, ScaDict> implements IScaDictService {

}