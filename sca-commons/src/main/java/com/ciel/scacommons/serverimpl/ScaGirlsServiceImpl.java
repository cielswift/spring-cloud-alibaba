package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scaapi.crud.IScaGirlsService;
import com.ciel.scacommons.mapper.ScaGirlsMapper;
import com.ciel.scaentity.entity.ScaGirls;
import org.springframework.stereotype.Service;

@Service
public class ScaGirlsServiceImpl extends ServiceImpl<ScaGirlsMapper, ScaGirls> implements IScaGirlsService {

}
