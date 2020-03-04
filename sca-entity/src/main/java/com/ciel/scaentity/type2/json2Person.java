package com.ciel.scaentity.type2;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.*;
import org.springframework.util.StringUtils;

import java.sql.*;

@MappedTypes({Person.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class json2Person extends BaseTypeHandler<Person> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i,
                                    Person person, JdbcType jdbcType) throws SQLException {

        //插入数据
        if(null != person && !StringUtils.isEmpty(person.getName())){
            preparedStatement.setString(i, JSON.toJSONString(person));
        }
    }

    @Override
    public Person getNullableResult(ResultSet resultSet, String columnName) throws SQLException {

        //查询结果数据类型转换
        String result = resultSet.getString(columnName);

        if(StringUtils.isEmpty(result)){
            return null;
        }else{
            return JSON.parseObject(result,Person.class);
        }
    }

    @Override
    public Person getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {

        //查询结果数据类型转换
        String result = resultSet.getString(columnIndex);

        if(StringUtils.isEmpty(result)){
            return null;
        }else{
            return JSON.parseObject(result,Person.class);
        }
    }

    @Override
    public Person getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {

        //查询结果数据类型转换
        String result = callableStatement.getString(columnIndex);

        if(StringUtils.isEmpty(result)){
            return null;
        }else{
            return JSON.parseObject(result,Person.class);
        }
    }
}
