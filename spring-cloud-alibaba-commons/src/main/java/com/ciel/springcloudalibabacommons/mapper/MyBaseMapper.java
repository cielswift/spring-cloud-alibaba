package com.ciel.springcloudalibabacommons.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface MyBaseMapper<T> extends BaseMapper<T> {

  //  Integer deleteAll(); //这里名字要和 DeleteAll 定义的 method一致

}
