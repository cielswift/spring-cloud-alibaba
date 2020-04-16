package com.ciel.scaapi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public enum ErrorMsg {

    NF_TEAM("找不到团队信息"),
    NF_GROUP("找不到小组信息"),
    NF_MEMBER("找不到团队成员信息"),
    NF_USER("找不到用户信息"),
    NF_CUSTOM_INFO("找不到客户信息"),
    EMPTY_CUSTOM_INFOS("客户信息列表为空"),
    NF_CONTRACT("找不到签约信息");

    private String name;
    private ErrorMsg(String name){
        this.name=name;
    }

    public String v(){
        return name;
    }

    static{

        try {
            List<String> strings = Files.readAllLines(Paths.get("c:/ciel/cc.mp4"));

            System.out.println(strings);
        } catch (IOException e) {
            System.out.println("error");
        }

    }
}