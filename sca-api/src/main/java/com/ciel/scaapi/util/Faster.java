package com.ciel.scaapi.util;

import com.ciel.scaapi.exception.AlertException;

import java.util.*;

/**
 * 一些常用快捷方法组合; 避免冗余
 * @author xiapeixin
 * @date 2020-3-28
 */
public final class Faster {

    /**
     * 单个对象转list, 适应于如:单个删除调用批量删除,转换参数
     */
    public static <T> List<T> toList(T...ts){
        return new ArrayList<T>(Arrays.asList(ts));
    }

    /**
     * 如果对象为空或集合为空,则抛出异常
     */
    public static void throwNull(Object obj,String msg) throws AlertException {
        if(null == obj){
            throw new AlertException(msg);
        }
        if(obj instanceof Collection){
            if( ((Collection)obj).isEmpty()){
                throw new AlertException(msg);
            }
        }
        if(obj instanceof Map){
            if( ((Map)obj).isEmpty()){
                throw new AlertException(msg);
            }
        }
    }

    public static void throwNull(Object obj) throws AlertException {
        throwNull(obj,"对象为空异常");
    }

    /**
     * 集合不为null ,且不为空
     */
    public static boolean IsNotEmpty(Collection<?> collection){
        return collection != null && !collection.isEmpty();
    }

    /**
     * 集合不为null ,且不为空
     */
    public static boolean IsNotEmpty(Map<?,?> map){
        return map != null && !map.isEmpty();
    }


    private Faster(){}
}
