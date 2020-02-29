package com.ciel.springcloudalibabaapi.retu;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一返回格式
 */
public class Result extends HashMap<String,Object> {

    /**
     * 200 正常
     * 400 异常
     */
    private static final String CODE = "code";
    private static final String MSG = "msg";
    private static final String BODY = "body";

    private Result(Integer code, String msg){
        put(CODE,code).put(MSG,msg);
    }

    public static Result ok(String msg){
        return new Result(200,msg);
    }

    public static Result ok(){
       return Result.ok("SUCCESS");
    }

    public static Result error(String msg){
        return  new Result(400,msg);
    }

    public static Result error(){
        return Result.error("ERROR");
    }

    public Result body(Object body){
        return this.put(BODY,body);
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
