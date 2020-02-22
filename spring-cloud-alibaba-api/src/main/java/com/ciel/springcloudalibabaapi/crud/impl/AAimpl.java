package com.ciel.springcloudalibabaapi.crud.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.springcloudalibabaapi.anntion.TransactionXiaPeiXin;
import com.ciel.springcloudalibabaapi.crud.AAASe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AAimpl  implements AAASe {

    @Autowired
    @Qualifier("scaApplicationMapper")
    protected BaseMapper baseMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean testTransaction(){
        baseMapper.deleteById("999999999");
        baseMapper.deleteById("88888888");

        return true;
    }


    @Override
    @TransactionXiaPeiXin
    public boolean testCustomTransaction() {
        baseMapper.deleteById("999999999");
        baseMapper.deleteById("88888888");
        return true;

    }
}
