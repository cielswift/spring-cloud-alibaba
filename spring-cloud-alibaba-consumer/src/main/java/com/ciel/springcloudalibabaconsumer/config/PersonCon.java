package com.ciel.springcloudalibabaconsumer.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class PersonCon {

    private String name;
    private Integer age;
    private Date birthday;
}
