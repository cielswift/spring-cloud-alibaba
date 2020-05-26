package com.ciel.scagateway.filter.config;

import lombok.Data;

@Data
public class GirlWhite {

    private String name;

    private Integer age;

    public static void main(String[] args) {

        StringBuilder phone = new StringBuilder("15966504931");

        StringBuilder replace = phone.replace(3, 7, "****");

        System.out.println(replace);
    }
}
