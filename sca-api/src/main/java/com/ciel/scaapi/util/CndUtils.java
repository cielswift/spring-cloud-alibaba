package com.ciel.scaapi.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Enumeration;

/**
 * @Title: CndUtils
 * @Description: 条件 工具类
 * @Auther: Zhangjiashun
 * @Version: 1.0
 * @create 2019/11/4 14:54
 */
public class CndUtils {
    public static final Logger logger = LoggerFactory.getLogger(CndUtils.class);

    /**
     * 相等
     */
    public static final String EQ = "eq";
    /**
     * 不相等
     */
    public static final String NOT = "not";
    /**
     * 相似
     */
    public static final String LIKE = "like";
    /**
     * 左相似
     */
    public static final String L_LIKE = "llike";
    /**
     * 右相似
     */
    public static final String R_LIKE = "rlike";
    /**
     * 小于
     */
    public static final String LT = "lt";
    /**
     * 大于
     */
    public static final String GT = "gt";
    /**
     * 小于等于
     */
    public static final String LE = "le";
    /**
     * 大于等于
     */
    public static final String GE = "ge";

    /**
     * 正序
     */
    private static final String ASC = "asc";
    /**
     * 倒序
     */
    private static final String DESC = "desc";



    /**
     * 传入对应类型。 根据前段传入的参数进行解析。 解析成功后返回相应的条件构造器。
     *
     * @param type 需要解析的类型。
     * @param <T>  QueryWrapper 中的泛型。
     * @return 构建完成的 queryWrapper
     */
    public static <T> QueryWrapper<T> autoCnd(Class<T> type) {
        HttpServletRequest request = SysUtils.getRequest();
        // 遍历所有请求参数。
        Enumeration<String> enumeration = request.getParameterNames();
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        while (null != enumeration && enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getParameter(name);
            // 请求参数值为空跳过。
            if (Strings.isBlank(value)) {
                continue;
            }
            // 解析请求。
            parse(queryWrapper, type, name, value);
        }

        return queryWrapper;
    }


    /**
     * 解析请求传入的参数
     *
     * @param wrapper 条件
     * @param entity  实体
     * @param name    参数名
     * @param value   参数值
     */
    private static <T> void parse(QueryWrapper<T> wrapper, Class<?> entity, String name, Object value) {

        // 按下划线分割。    条件_字段名
        int index = name.indexOf("_");
        if (index <= 0) {
            return;
        }
        // 条件
        String prefix = name.substring(0, index);
        // 字段名
        String suffix = name.substring(index + 1);
        // 对应数据库中的字段名
        String dbFieldName;

        try {
            Field declaredField = entity.getDeclaredField(suffix);
            declaredField.setAccessible(true);
            TableField annotation = declaredField.getAnnotation(TableField.class);
            if (annotation == null) {
                // 没有 TableField 注解说明数据库中字段名为 这个属性名的驼峰转下划线。
                dbFieldName = SysUtils.hump2Line(suffix);
            } else {
                // 有 TableField 注解 取注解值为数据库中字段名
                dbFieldName = annotation.value();
            }

            if(StringUtils.isEmpty(dbFieldName)){
                dbFieldName = SysUtils.hump2Line(suffix);
            }

        } catch (NoSuchFieldException e) {
            logger.debug("类：{} 不存在成员：{}", entity.getName(), suffix);
            return;
        }

        // 根据传入前缀判断
        switch (prefix) {
            case EQ:
                wrapper.eq(dbFieldName, value);
                break;
            case NOT:
                wrapper.ne(dbFieldName, value);
                break;
            case LIKE:
                wrapper.like(dbFieldName, value);
                break;
            case L_LIKE:
                wrapper.likeLeft(dbFieldName, value);
                break;
            case R_LIKE:
                wrapper.likeRight(dbFieldName, value);
                break;
            case LT:
                wrapper.lt(dbFieldName, value);
                break;
            case GT:
                wrapper.gt(dbFieldName, value);
                break;
            case LE:
                wrapper.le(dbFieldName, value);
                break;
            case GE:
                wrapper.ge(dbFieldName, value);
                break;
            case ASC:
                if ("1".equals(value)) {
                    wrapper.orderByAsc(dbFieldName);
                }
                break;
            case DESC:
                if ("1".equals(value)) {
                    wrapper.orderByDesc(dbFieldName);
                }
                break;
            default:
                // 不支持的前缀符号。
                logger.debug("不支持的前缀");
        }

    }


}