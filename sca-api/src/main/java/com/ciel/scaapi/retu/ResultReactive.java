package com.ciel.scaapi.retu;

import java.util.HashMap;

/**
 * reactive 模式使用
 */
public final class ResultReactive extends HashMap<String, Object> {

    public static final long serialVersionUID = 1L;

    /**
     * 200 正常
     */
    private static final String CODE = "code";
    private static final String MSG = "msg";
    private static final String DATA = "data";

    private ResultReactive(int code, String msg){
        put(CODE,code).put(MSG,msg);
    }

    /**
     * 返回成功信息
     * @param msg
     * @return
     */
    public static ResultReactive ok(String msg){
        return new ResultReactive(RespCode.OK.code(),msg);
    }

    /**
     * 直接返回成功
     * @return
     */
    public static ResultReactive ok(){
        return ResultReactive.ok(RespCode.OK.v());
    }
    /**
     * 返回错误信息
     * @param msg
     * @return
     */
    public static ResultReactive error(String msg){
        return  new ResultReactive(RespCode.ERROR.code(),msg);
    }

    /**
     * 返回自定义错误code 和信息
     * @param code
     * @param msg
     * @return
     */
    public static ResultReactive error(Integer code,String msg){
        return  new ResultReactive(code,msg);
    }

    /**
     * 设置对象
     * @param data
     * @return
     */
    public ResultReactive data(Object data){
        return this.put(DATA,data);
    }

    @Override
    public ResultReactive put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
