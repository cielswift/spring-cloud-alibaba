package com.ciel.scaapi.exception;

/**
 * 全局异常
 */
public class AlertException extends Exception {

    public AlertException(String msg){
        super(msg);
    }

    public AlertException(String msg,Throwable throwable){
        super(msg,throwable);
    }
}
