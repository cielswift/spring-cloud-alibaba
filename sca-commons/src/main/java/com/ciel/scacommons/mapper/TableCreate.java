package com.ciel.scacommons.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 非crud sql
 *
 * 动态创建表
 */
@Repository
public class TableCreate {

    private static final Logger logger = LoggerFactory.getLogger(TableCreate.class);

    @Autowired
    protected DataSource dataSource;

    public boolean isExitsTable(String tName) throws SQLException {

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet =
                statement.executeQuery("show TABLES like '${tName}'".replace("${tName}",tName));

        boolean flag = false;

        if(resultSet.next()){
            if(tName.equals(resultSet.getString(1))){
                flag = true;
            }
        }else{
            flag = false;
        }

        connection.close();
        statement.close();
        resultSet.close();

        return flag;
    }

    public boolean createTableOrder(String tName) throws SQLException {

        String sql  = "CREATE TABLE `${tName}`  (\n" +
                "  `ID` bigint(32) NOT NULL,\n" +
                "  `CREATE_DATE` datetime(0) NULL DEFAULT NULL,\n" +
                "  `UPDATE_DATE` datetime(0) NULL DEFAULT NULL,\n" +
                "  `DELETED` bit(1) NULL DEFAULT NULL,\n" +
                "  `ORDER_NUMBER` bigint(32) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (`ID`) USING BTREE\n" +
                ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;";


        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();


        int i = statement.executeUpdate(sql.replace("${tName}", tName));

        statement.close();
        connection.close();

        if(i!=0){
            return true;
        }else{
            return false;
        }

    }

}
