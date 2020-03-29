package com.ciel.scaapi.util;

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
}