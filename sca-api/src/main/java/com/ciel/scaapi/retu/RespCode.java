package com.ciel.scaapi.retu;

public enum RespCode {

    OK(200),
    ERROR(500),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405);

//    400BadRequest：服务器不理解客户端的请求，未做任何处理
//
//401Unauthorized：用户未提供身份验证凭据，或者没有通过身份验证
//
//403Forbidden：用户通过了身份验证，但是不具有访问资源所需的权限
//
//404NotFound：所请求的资源不存在，或不可用
//
//415UnsupportedMediaType：客户端要求的返回格式不支持。比如，API 只能返回 JSON 格式，但是客户端要求返回 XML 格式

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
