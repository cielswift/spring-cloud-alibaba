package com.ciel.scaapi.util;

import com.alibaba.fastjson.JSON;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaUser;
import org.apache.commons.codec.CharEncoding;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 一些常用快捷方法组合; 避免冗余
 * @author xiapeixin
 * @date 2020-3-28
 */
public final class Faster {


    /**
     * 响应json
     */
    public static void respJson(Result result, HttpServletResponse response) throws IOException {

        response.setHeader("Access-control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("access-control-expose-headers", "Authentication");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSON.toJSONString(result));
        response.getWriter().close();
    }

    /**
     * 文件下载
     */
    public static void download(File file, HttpServletResponse response) throws IOException {

        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), CharEncoding.UTF_8).replace("+", "%20"));

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //打开本地文件流
        InputStream inputStream = new FileInputStream(file);
        //激活下载操作
        OutputStream os = response.getOutputStream();

        //循环写入输出流
        byte[] b = new byte[2048];
        int length;
        while ((length = inputStream.read(b)) > 0) {
            os.write(b, 0, length);
        }
        // 这里主要关闭。
        os.close();
        inputStream.close();
    }

    //求百分比
    public static Double getPercentage(Integer tag,Integer all){
        try{
            return new BigDecimal(tag).divide(new BigDecimal(all),2, BigDecimal.ROUND_HALF_DOWN)
                    .multiply(new BigDecimal("100")).doubleValue();
        }catch (Exception e){
            return 0.00;
        }
    }

    /**
     * 枚举值是否再合法的枚举序列中
     */
    public static <T> void throwNotIn(T t, List<T> list) throws AlertException {
        for (T ts : list) {
            if(ts.equals(t)){
                return;
            }
        }
        throw new AlertException(t + "不包含在:"+ list + "中");
    }

    /**
     * 单个或多个对象转list, 适应于如:单个删除调用批量删除,转换参数
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

    public static boolean isNull(Object obj){
        return obj == null;
    }

    public static boolean isNotNull(Object obj){
        return obj != null;
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

    public static Date now(){
        return new Date();
    }

    public static void println(Object obj){
        System.out.println(obj);
    }
    private Faster(){}
}