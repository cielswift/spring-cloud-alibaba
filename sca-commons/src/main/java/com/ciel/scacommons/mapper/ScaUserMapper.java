package com.ciel.scacommons.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ciel.scaentity.entity.ScaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    /**
     * 自定义sql 使用 querywarpe
     */
    //@Select("SELECT * FROM sca_user ${ew.customSqlSegment}")  //查询条件
    Page<ScaUser> myPage(Page<ScaUser> page, @Param(Constants.WRAPPER) QueryWrapper<ScaUser> queryWrapper);

    //@Update("UPDATE sca_user SET ${ew.sqlSet} ${ew.customSqlSegment}")  //更新的列
    int myChange(@Param(Constants.WRAPPER) UpdateWrapper<ScaUser> updateWrapper);

   // @Select("SELECT ${ew.sqlSelect}  FROM sca_user ${ew.customSqlSegment}")  //查询的列
    List<ScaUser> myList(@Param(Constants.WRAPPER) QueryWrapper<ScaUser> queryWrapper);

}
