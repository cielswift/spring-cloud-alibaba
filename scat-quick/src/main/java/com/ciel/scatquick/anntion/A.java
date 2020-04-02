package com.ciel.scatquick.anntion;

import java.io.IOException;
import java.util.HashMap;

public class A {

    public static void main(String[] args) throws IOException {

        //String aaa = aaa(t -> t); //第一种lambda表达式; 相当于写了方法实现
       // System.out.println(aaa);

        String bbb = aaa(A::bbb);  //第2方法引用
        System.out.println(bbb);

        HashMap<String, String> hashMap = new HashMap<>();

//        File chm = new File(serverImagePath);
//        chm.setExecutable(true);//设置可执行权限
//        chm.setReadable(true);//设置可读权限
//        chm.setWritable(true);//设置可写权限
//
//        if (!System.getProperty("os.name").startsWith("Win")) {
//            String cmdGrant = "chmod -R 777 " + serverImagePath;
//            Runtime.getRuntime().exec(cmdGrant);
//        }
    }

    // git config --global user.name "你的名字"
    public static String aaa(Lambd lambd){

        return lambd.xxx("aa");  //第2方法引用,这里相当于bbb是 xxx的方法实现
    }

    public static String bbb(String bbb){

        System.out.println(bbb);
        return "bbb";
    }
}
