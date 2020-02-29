package com.ciel.springcloudalibabacommons.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ciel.springcloudalibabaentity.entity.ScaOrder;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.caches.ehcache.EhcacheCache;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiapeixin
 * @since 2020-02-14
 */
@Mapper
public interface ScaOrderMapper extends MyBaseMapper<ScaOrder> {

}
