package com.ciel.scatquick.sqlite;

import com.alibaba.druid.pool.DruidDataSource;
import com.ciel.scatquick.aoptxspi.LogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public final class Sqlite {

    private Connection connection;

    protected JdbcTemplate jdbcTemplate;

    @PostConstruct
    private  void init() throws ClassNotFoundException, SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:sqlite:C:/CustomSoftware/sqlite/log.db");
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setDbType("sqlite");
        dataSource.setMaxActive(2);
        dataSource.setMaxWait(2000);
        dataSource.setConnectionErrorRetryAttempts(0);
        dataSource.setBreakAfterAcquireFailure(true);
        dataSource.setValidationQuery("SELECT 1 ");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public <T> List<T> select(Class<T> tClass) throws Exception {

        String sql = String.format("SELECT * FROM %s", tClass.getSimpleName());
        return jdbcTemplate.queryForList(sql,tClass);

//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(sql);
//
//        List<Field> fields = Arrays.stream(tClass.getDeclaredFields()).collect(Collectors.toList());
//        ArrayList<T> list = new ArrayList<>();
//        while (resultSet.next()) {
//            T instance = tClass.getConstructor().newInstance();
//            for (Field x : fields) {
//                x.setAccessible(true);
//                Object object = resultSet.getObject(x.getName(), x.getType());
//                x.set(instance, object);
//            }
//            list.add(instance);
//        }
//
//        resultSet.close();
//        statement.close();
//        return list;
    }

    public <T> boolean insert(T t) throws IllegalAccessException, SQLException {

        StringBuilder stringBuilder = new StringBuilder("INSERT INTO ").append(t.getClass().getSimpleName());

        List<Field> fields = Arrays.stream(t.getClass().getDeclaredFields()).collect(Collectors.toList());

        StringBuilder fie = new StringBuilder(32).append("(");
        StringBuilder vl = new StringBuilder(32).append("(");

        for (Field x : fields) {

            if("id".equals(x.getName())){
                continue;
            }

            x.setAccessible(true);
            fie.append(x.getName()).append(",");
            Object o = x.get(t);
            if(o instanceof String){
                vl.append("'").append(o).append("',");
            }else{
                vl.append(o).append(",");
            }
        }

        fie.deleteCharAt(fie.length()-1).append(")");
        vl.deleteCharAt(vl.length()-1).append(")");

//        Statement statement = connection.createStatement();
//        int update = statement.executeUpdate(stringBuilder.append(fie.toString()).append(" values ").append(vl.toString()).toString());
//        statement.close();
//        return update>0;

        jdbcTemplate.execute(stringBuilder.append(fie.toString()).append(" values ").append(vl.toString()).toString());

        return true;
    }


}
