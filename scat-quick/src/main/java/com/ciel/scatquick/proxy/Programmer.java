package com.ciel.scatquick.proxy;

import com.ciel.scatquick.aoptxspi.LogsAnnal;
import org.springframework.aop.framework.AopContext;

public class Programmer implements Empor {

    private String name;

    public Programmer(String name) {
        this.name = name;
    }

    public Programmer() { //必须有无参构造函数 否则报错

    }

    @LogsAnnal
    @Override
    public String work(String work) throws Exception {

        try{
            Object proxy = AopContext.currentProxy();
            System.out.println("代理对象为:" + proxy);
        }catch (IllegalStateException e){
            System.err.println("没有代理对象");
        }

        System.out.println(name + "正在工作..." + work);
        if (System.currentTimeMillis() % 2 == 0) {
            throw new Exception("停电了");
        }

        return "加班的代码成果";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}