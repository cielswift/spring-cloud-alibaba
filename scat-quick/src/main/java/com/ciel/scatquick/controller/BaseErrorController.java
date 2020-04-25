package com.ciel.scatquick.controller;

import com.ciel.scaapi.retu.Result;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * BaseErrController 这个类就是用来捕获 /error 的所有错误，而过滤器中的错误会被重定向到 /error
 */
@RestController
public class BaseErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public Result err(HttpServletRequest request) {
        // 状态码
        Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");

        // 错误原因
        Exception msg = (Exception) request.getAttribute("javax.servlet.error.exception");

        switch (code){
            case 401:
                return Result.error(code,"未认证");
            case 403:
                return Result.error(code,"权限不足");
            default:
                return Result.error();
        }
    }

    /**
     * 指定跳转路径/error
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
