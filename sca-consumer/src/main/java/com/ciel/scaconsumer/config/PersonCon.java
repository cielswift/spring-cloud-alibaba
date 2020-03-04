package com.ciel.scaconsumer.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PersonCon {

    private String name;
    private Integer age;
    private Date birthday;
}
