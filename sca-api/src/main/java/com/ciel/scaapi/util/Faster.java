package com.ciel.scaapi.util;

import com.alibaba.fastjson.JSON;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些常用快捷方法组合; 避免冗余
 *
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
    public synchronized static String format(Date date) {
        return FORMAT_STR.format(date);
    }

    /**
      转date
     */
    public synchronized static Date parse(String date) throws ParseException {
        return FORMAT_STR.parse(date);
    }

    /**
     * 格式化格式,返回新的对象避免线程安全问题;
     */
    public synchronized static String formatDetail(Date date) {
        return FORMAT_STR_DETAIL.format(date);
    }

    public synchronized static Date parseDetail(String date) throws ParseException {
        return FORMAT_STR_DETAIL.parse(date);
    }

    /**
     * 格式化格式,返回新的对象避免线程安全问题;
     */
    public synchronized static String formatFile(Date date) {
        return FORMAT_STR_FILE.format(date);
    }

    /**
     * 生产随机编号
     */
    public synchronized static String random(int len,String prefix){
        Double rm = (Math.random() + System.currentTimeMillis() +
                ThreadLocalRandom.current().nextInt(62))  * 0.75;
        String replace = String.valueOf(rm).replaceAll("\\d\\.",prefix);
        int bw = len - replace.length();
        return bw<=0 ? replace.substring(0,len) : supplement(replace,bw);
    }

    private static String supplement(String str,int len){
        StringBuilder sb = new StringBuilder(str);
        for(int i = 0 ; i<len;i++){
            sb.append(i);
        }
        return sb.toString();
    }

    /**
     * 转为json
     * @param object
     * @return
     */
    public static String toJson(Object object){
        return JSON.toJSONString(object);
    }

    /**
     * json 转对象
     */
    public static <T> T parseJson(String json,Class<T> tClass){
        return JSON.parseObject(json,tClass);
    }

    /**
     * 获取classpath下文件
     */
    public static File classPathFile(String path) throws IOException {
        return new ClassPathResource(path).getFile();
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
        response.addHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(file.getName(), CharEncoding.UTF_8).replace("+", "%20"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        InputStream inputStream = new FileInputStream(file);
        OutputStream os = response.getOutputStream();

        //IOUtils.copy(inputStream,os,1024*100);
        //循环写入输出流
        byte[] bytes = new byte[1024 * 100]; //100kb
        for (int len = inputStream.read(bytes); (len != -1); len = inputStream.read(bytes)) {
            os.write(bytes, 0, len);
        }
        os.close();
        inputStream.close();
    }

    /**
     * 响应二进制文件
     */
    public static void binary(InputStream inputStream,HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream,outputStream,1024*100);
        inputStream.close();
        outputStream.close();
    }


    //求百分比 保留几位小数
    public static Double getPercentage(Number tag, Number all, int decimal) {
        try {
            return BigDecimal.valueOf(tag.doubleValue()).divide(BigDecimal.valueOf(all.doubleValue())
                    , decimal, BigDecimal.ROUND_HALF_DOWN)
                    .multiply(new BigDecimal("100")).doubleValue();
        } catch (Exception e) {
            return 0.00;
        }
    }

    /**
     * 枚举值是否再合法的枚举序列中 ,根据哪一个字段判断
     */
    public static boolean throwNotIn(T t, List<T> list, String field) throws Exception {
        for (T ts : list) {
            if (ts.equals(t)) {
                if (t.getClass().getDeclaredField(field).get(t).equals(ts.getClass().getDeclaredField(field).get(ts))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 单个或多个对象转list, 适应于如:单个删除调用批量删除,转换参数
     */
    @SafeVarargs
    public static <T> List<T> toList(T... ts) {
        return new ArrayList<T>(Arrays.asList(ts));
    }

    /**
     * 如果对象为空或集合为空,则抛出异常
     */
    public static void throwNull(Object obj, String msg) throws AlertException {
        if (null == obj) {
            throw new AlertException(msg);
        }
        if (StringUtils.isEmpty(obj)) {
            throw new AlertException(msg);
        }
        if (obj instanceof Collection) {
            if (((Collection) obj).isEmpty()) {
                throw new AlertException(msg);
            }
        }
        if (obj instanceof Map) {
            if (((Map) obj).isEmpty()) {
                throw new AlertException(msg);
            }
        }
    }

    /**
     * 简化版
     */
    public static void throwNull(Object obj) throws AlertException {
        throwNull(obj, "对象为空异常");
    }

    /**
     * 是否为null 空
     *
     * @param obj
     */
    public static boolean isNull(Object obj) {
        if (null == obj) {
            return true;
        }
        if (StringUtils.isEmpty(obj)) {
            return true;
        }
        if (obj instanceof Collection) {
            if (((Collection) obj).isEmpty()) {
                return true;
            }
        }
        if (obj instanceof Map) {
            if (((Map) obj).isEmpty()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 当前时间
     */
    public static Date now() {
        return new Date();
    }

    public static void println(Object obj) {
        System.out.println(obj);
    }

    private Faster() { }

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