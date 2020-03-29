package com.ciel.scaapi.anntion;

import java.io.IOException;

public class A {

    public static void main(String[] args) throws IOException {

        //String aaa = aaa(t -> t); //第一种lambda表达式; 相当于写了方法实现
       // System.out.println(aaa);

        String bbb = aaa(A::bbb);  //第2方法引用
        System.out.println(bbb);
    }

    public static String aaa(Lambd lambd){

        return lambd.xxx("aa");  //第2方法引用,这里相当于bbb是 xxx的方法实现
    }

    public static String bbb(String bbb){

        System.out.println(bbb);
        return "bbb";
    }
}
