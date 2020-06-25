package com.ciel.scaapi.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 */
public class CndPagUtils {
    public static final Logger logger = LoggerFactory.getLogger(CndPagUtils.class);

    /**
     * 相等
     */
    public static final String EQ = "eq";
    /**
     * 包含
     */
    public static final String IN = "in";
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
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        // 遍历所有请求参数。
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((k, v) -> {

            if (null != v && v.length != 0 && StringUtils.isNotEmpty(v[0])) {  // 请求参数值为空跳过。

                // 按下划线分割 条件_字段名
                String[] split = k.split("_");
                if (split.length != 2) {
                    return;
                }
                // 条件
                String prefix = split[0];
                // 字段名
                String suffix = split[1];
                // 对应数据库中的字段名
                String dbFieldName = null;
                Field declaredField = null;

                try {
                    declaredField = type.getDeclaredField(suffix);
                    declaredField.setAccessible(true);

                    TableField tableField = declaredField.getAnnotation(TableField.class);

                    if (tableField != null && !StringUtils.isEmpty(tableField.value())) {
                        dbFieldName = tableField.value();
                    } else {
                        dbFieldName = SysUtils.hump2Line(suffix);
                    }

                    switch (prefix) {
                        case EQ:
                            queryWrapper.eq(dbFieldName, v[0]);
                            break;
                        case IN:
                            queryWrapper.in(dbFieldName, (Object[]) v[0].split(","));
                            break;
                        case NOT:
                            queryWrapper.ne(dbFieldName, v[0]);
                            break;
                        case LIKE:
                            queryWrapper.like(dbFieldName, v[0]);
                            break;
                        case L_LIKE:
                            queryWrapper.likeLeft(dbFieldName, v[0]);
                            break;
                        case R_LIKE:
                            queryWrapper.likeRight(dbFieldName, v[0]);
                            break;
                        case LT:
                            queryWrapper.lt(dbFieldName, v[0]);
                            break;
                        case GT:
                            queryWrapper.gt(dbFieldName, v[0]);
                            break;
                        case LE:
                            queryWrapper.le(dbFieldName, v[0]);
                            break;
                        case GE:
                            queryWrapper.ge(dbFieldName, v[0]);
                            break;
                        case ASC:
                            if ("1".equals(v[0])) {
                                queryWrapper.orderByAsc(dbFieldName);
                            }
                            break;
                        case DESC:
                            if ("1".equals(v[0])) {
                                queryWrapper.orderByDesc(dbFieldName);
                            }
                            break;
                        default:
                            // 不支持的前缀符号。
                            logger.debug("不支持的前缀");
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        });

        return queryWrapper;
    }

    public static <T> IPage<T> autoPage(Class<T> type){
        HttpServletRequest request = SysUtils.getRequest();
        Page<T> page = new Page<>();
        page.setCurrent(Long.parseLong(request.getParameter("current")));
        page.setSize(Long.parseLong(request.getParameter("size")));
        return page;
    }


}