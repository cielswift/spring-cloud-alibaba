package com.ciel.scaapi.retu.jdkproxy;

public class Employee implements Boss {
    @Override
    public void doSomethinig() {
        System.out.println("员工工作中 ...");
    }
    @Override
    public void finishTasks(String name) {
        System.out.println("员工正在完成项目 " + name + " ...");
    }
}