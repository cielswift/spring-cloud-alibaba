package com.ciel.scaapi.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ciel.scaentity.entity.ScaUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SysUtils {

    /**
     * 获得当前登录的用户
     *
     * @return 当前用户
     */
    public static Object currentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if(Faster.isNotNull(authentication)){
            return authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取request
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    private static ServletRequestAttributes getRequestAttributes() {
        RequestContextHolder.currentRequestAttributes();
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取response
     *
     * @return response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 根据查询字段自动生成Cnd;
     *
     * @param type 需要生成wrapper的类
     * @return querywrapper
     */
    public static <T> QueryWrapper<T> autoCnd(Class<T> type) {
        return CndUtils.autoCnd(type);
    }

    /**
     * 驼峰转下划线
     *
     * @param str 驼峰字符
     * @return 下划线字符
     */
    public static String hump2Line(String str) {
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 判断是否是驼峰字符串。
     *
     * @param str 需要判断的字符串
     * @return 是否为驼峰命名字符串
     */
    public static Boolean isHumpStr(String str) {
        if (str == null) {
            return false;
        }
        if (str.contains("_")) {
            return false;
        }
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        return matcher.find();
    }

    /**
     * 判断是否是下划线字符串
     *
     * @param str 需要判断的字符串
     * @return 是否为下划线命名字符串
     */
    public static Boolean isUnderLineStr(String str) {
        if (str == null) {
            return false;
        }
        int index = str.indexOf("_");

        return index > 0 && index != str.length() - 1;
    }

    /**
     * 下划线驼峰互转
     *
     * @param str 下划线或驼峰字符串
     * @return 如果传入下划线将会返会驼峰字符串， 如果是驼峰字符串将会返会下划线字符串。
     */
    public static String lineHumpInterconvert(String str) {
        String interconvert = null;
        if (isHumpStr(str)) {
            interconvert = hump2Line(str);
        } else if (isUnderLineStr(str)) {
            interconvert = line2Hump(str);
        }
        return interconvert;
    }

    /**
     * 下划线转驼峰
     *
     * @param str 下划线字符
     * @return 驼峰字符
     */
    public static String line2Hump(String str) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}