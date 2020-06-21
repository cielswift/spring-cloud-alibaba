package com.ciel.scacommons.mapper;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface MyBaseMapper<T> extends BaseMapper<T> {

  //  Integer deleteAll(); //这里名字要和 DeleteAll 定义的 method一致

}
