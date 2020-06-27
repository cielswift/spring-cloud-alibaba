package com.ciel.scaapi.util;

import com.alibaba.fastjson.JSON;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import org.apache.commons.codec.CharEncoding;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些常用快捷方法组合; 避免冗余
 * @author xiapeixin
 * @date 2020-3-28
 */
public final class Faster {

    /**
     * 文件存储时间格式
     */
    private static final SimpleDateFormat FORMAT_STR_FILE = new SimpleDateFormat("yyyy/MM/dd/");

    /**
     * 时间格式
     */
    private static final SimpleDateFormat FORMAT_STR = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 时间格式详细
     */
    private static final SimpleDateFormat FORMAT_STR_DETAIL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 格式化格式,返回新的对象避免线程安全问题;
     */
    public synchronized static String format(Date date){
        return FORMAT_STR.format(date);
    }

    public synchronized static Date parse(String date) throws ParseException {
        return FORMAT_STR.parse(date);
    }


    /**
     * 格式化格式,返回新的对象避免线程安全问题;
     */
    public synchronized static String formatDetail(Date date){
        return FORMAT_STR_DETAIL.format(date);
    }

    public synchronized static Date parseDetail(String date) throws ParseException {
        return FORMAT_STR_DETAIL.parse(date);
    }

    /**
     * 格式化格式,返回新的对象避免线程安全问题;
     */
    public synchronized static String formatFile(Date date){
        return FORMAT_STR_FILE.format(date);
    }


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
        return obj == null || StringUtils.isEmpty(obj);
    }

    public static boolean isNotNull(Object obj){
        return obj != null && !StringUtils.isEmpty(obj);
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


    /**
     * 匹配_加任意一个字符
     */
    private static final Pattern UNDER_LINE_PATTERN = Pattern.compile("_(\\w)");

    /***
     * 下划线命名转为驼峰命名
     *
     * @param source
     *        下划线命名的字符串
     */

    public static String underlineToHump(String source) {
        StringBuffer result = new StringBuffer();
        String a[] = source.split("_");
        for (String s : a) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


    /***
     * 驼峰命名转为下划线命名
     *
     * @param source
     *        驼峰命名的字符串
     */

    public static String humpToUnderline(String source) {
        StringBuffer sb = new StringBuffer(source);
        int temp = 0;//定位
        for (int i = 0; i < source.length(); i++) {
            if (Character.isUpperCase(source.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * Create by lrt<br/>
     * Date：2018/10/10
     * Description： 下划线转为驼峰格式
     *
     * @param source 原字符串
     * @return java.lang.String 返回转换后的驼峰格式字符串
     */
    public static String underLineToCamel(String source) {
        //用Pattern类的matcher()方法生成一个Matcher对象
        Matcher matcher = UNDER_LINE_PATTERN.matcher(source);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(result);
        return result.toString();
    }
}