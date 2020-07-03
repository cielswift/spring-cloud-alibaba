package com.ciel.scacommons.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.poi.ss.formula.functions.T;
import sun.util.resources.ga.LocaleNames_ga;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 自定义的 mapper 接口
 * @param <T>
 */
@SuppressWarnings("all")
public interface MyBaseMapper<T> extends BaseMapper<T> {

    /**
     * sql 注入
     */
    public int deleteAll(@Param("id") Long id);


    @Select("SELECT * FROM ${name} WHERE id = #{id}") //表名不能使用 #{}
    public T yiGe(@Param("name") String name,@Param("id") Long id);


    @SelectProvider(type = MyBaseMapper.class,method = "getOneImp")
    //自增长主键，@Options在插入数据后自动获取到该主键值;  配合@InsertProvider
    //@Options(useGeneratedKeys = true,keyProperty="id",keyColumn="id");
    public T getOne(@Param("cls") Class<T> cls);

    /**
     * 获取一个
     */
    public static <T> String getOneImp(@Param("cls")Class<T> cls){
        TableName annotation = cls.getAnnotation(TableName.class);
        return String.format("SELECT * FROM %s limit 0, 1",annotation.value());

        //else //return "SELECT * FROM %s  WHERE ID = #{cls.id} LIMIT 0,1";
    }


}
