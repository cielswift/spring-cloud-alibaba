package com.ciel.scaapi.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Map;

/**
 *
 */
@Slf4j
public class CndPagUtils {
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
     */
    public static <T> QueryWrapper<T> autoCnd(Class<T> type) {
        HttpServletRequest request = SysUtils.currentRequest();
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();


        request.getParameterMap().forEach((k, v) -> { // 遍历所有请求参数
            if (null != v && v.length != 0 && StringUtils.isNotEmpty(v[0])) {  //请求参数值为空跳过。

                String[] split = k.split("_"); // 按下划线分割 条件_字段名
                if (split.length == 2) { //可以被_ 分割为2个

                    String prefix = split[0];  // 条件
                    String suffix = split[1]; // 字段名
                    String dbFieldName = null; // 对应数据库中的字段名
                    Field field = null;

                    try {
                        field = type.getDeclaredField(suffix);
                        field.setAccessible(true);

                        TableField tableField = field.getAnnotation(TableField.class);

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
                                log.error("不支持的前缀 : {}", prefix);  //不支持的前缀符号。
                        }
                    } catch (NoSuchFieldException e) {
                        log.error("找不到字段: {}", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
        return queryWrapper;
    }

    /**
     * 创建分页
     */
    public static <T> IPage<T> autoPage(Class<T> type) throws MissingServletRequestParameterException {
        HttpServletRequest request = SysUtils.currentRequest();
        Page<T> page = new Page<>();
        if (Faster.isNull(request.getParameter("current"))) {
            log.error("当前页current为空");
            throw new MissingServletRequestParameterException("current", "integer");
        }
        if (Faster.isNull(request.getParameter("size"))) {
            log.error("数量size为空");
            throw new MissingServletRequestParameterException("size", "integer");
        }
        page.setCurrent(Long.parseLong(request.getParameter("current")));
        page.setSize(Long.parseLong(request.getParameter("size")));
        return page;
    }


}