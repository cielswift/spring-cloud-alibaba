package com.ciel.scaapi.crud;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scaentity.entity.ScaOrder;

import java.util.List;

public interface IScaGirlsService extends IService<ScaGirls> {


    List<ScaGirls> girlsByPrice(double str,double end);
}
