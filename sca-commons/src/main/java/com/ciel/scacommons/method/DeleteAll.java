package com.ciel.scacommons.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * sql 注入
 */
public class DeleteAll extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {

        String sql = String.format("DELETE FROM %s where id = #{id}", tableInfo.getTableName());

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        /**
         * 注意 id 这个参数 必须和 mapper里的方法名称一致  public int deleteAll(@Param("id") Long id);
         */
        return this.addDeleteMappedStatement(mapperClass, "deleteAll", sqlSource);

        // return this.addInsertMappedStatement(mapperClass, modelClass, "mysqlInsertAllBatch",
        //sqlSource, new NoKeyGenerator(), null, null);
    }
}