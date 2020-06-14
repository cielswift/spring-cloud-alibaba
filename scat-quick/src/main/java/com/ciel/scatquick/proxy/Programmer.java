package com.ciel.scatquick.proxy;

public class Programmer implements Empor {

    private String name;

    public Programmer(String name){
        this.name=name;
    }

    public Programmer(){ //必须有无参构造函数 否则报错

    }
    @Override
    public void work(String work) {
        System.out.println(name + "正在工作..."+work);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {

        //java 运算符号

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