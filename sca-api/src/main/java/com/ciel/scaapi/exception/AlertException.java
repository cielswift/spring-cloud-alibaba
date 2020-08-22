package com.ciel.scaapi.exception;

/**
 * 全局异常
 */
public class AlertException extends Exception {

    public AlertException(String msg,Object...arr){
        super(String.format(msg,arr));
    }

    public AlertException(String msg,Throwable throwable){
        super(msg,throwable);
    }


}
