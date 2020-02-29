package com.ciel.springcloudalibabacommons.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ciel.springcloudalibabaentity.entity.ScaUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
@Mapper
//@DS("master")  手动指定数据源
public interface ScaUserMapper extends MyBaseMapper<ScaUser> {

}
