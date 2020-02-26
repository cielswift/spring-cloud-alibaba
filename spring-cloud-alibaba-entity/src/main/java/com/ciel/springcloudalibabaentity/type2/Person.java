package com.ciel.springcloudalibabaentity.type2;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Person {

    /**
     * 使用varchar 255 存储,注意字段长度
     * 使用text 将影响性能
     *
     * 不能使用这个字段查询了;
     */
    private String name;
    private Integer age;
    private Boolean gender;
    private LocalDateTime byDate;

}
