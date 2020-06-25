package com.ciel.scaapi.retu;

public enum RespCode {

    /**
     * 正常
     */
    OK(200),
    /**
     * 服务器错误
     */
    ERROR(500),
    /**
     * 错误的请求 服务器不理解客户端的请求，未做任何处理
     */
    BAD_REQUEST(400),
    /**
     * 未登录 用户未提供身份验证凭据，或者没有通过身份验证
     */
    UNAUTHORIZED(401),
    /**
     * 拒绝服务 无权限 用户通过了身份验证，但是不具有访问资源所需的权限
     */
    FORBIDDEN(403),
    /**
     * 请求地址错误 所请求的资源不存在，或不可用
     */
    NOT_FOUND(404);

    private RespCode(int code){
        this.code=code;
    }

    private int code;

    public String v(){
        return toString();
    }

    public int code(){
        return code;
    }

}
