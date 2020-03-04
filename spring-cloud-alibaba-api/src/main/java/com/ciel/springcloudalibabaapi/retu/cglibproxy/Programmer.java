package com.ciel.springcloudalibabaapi.retu.cglibproxy;

public class Programmer {
    public void work(String name) {
        System.out.println(name + " 正在工作...");
    }

    public static void main(String[] args) {

        int a = 1 << 30 ;

        int b = 64 >> 2;

        int c = 6^3;
//0110 //相同的为0，不同的为1
//0011
//0101
        int d = 6&3;
//0110 //一个为0即为0
//0011
//0010
        int e = 6|3;
//0110 //一个为1即为1
//0011
//0111
        System.out.println(e);


    }
}