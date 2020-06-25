package com.ciel.scaapi.retu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.Pager;
import com.ciel.scaentity.entity.ScaGirls;
import org.apache.poi.ss.formula.functions.T;
import org.aspectj.org.eclipse.jdt.internal.compiler.classfmt.MethodInfoWithAnnotations;
import org.springframework.context.event.EventListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 统一返回格式
 */
public final class Result extends HashMap<String,Object> {

    /**
     * 200 正常
     */
    private static final String CODE = "code";
    private static final String MSG = "msg";
    private static final String DATA = "data";

    private Result(int code, String msg){
        put(CODE,code).put(MSG,msg);
    }

    /**
     * 返回成功信息
     * @param msg
     * @return
     */
    public static Result ok(String msg){
        return new Result(RespCode.OK.code(),msg);
    }

    /**
     * 直接返回成功
     * @return
     */
    public static Result ok(){
       return Result.ok(RespCode.OK.v());
    }
    /**
     * 返回错误信息
     * @param msg
     * @return
     */
    public static Result error(String msg){
        return  new Result(RespCode.ERROR.code(),msg);
    }

    /**
     * 返回自定义错误code 和信息
     * @param code
     * @param msg
     * @return
     */
    public static Result error(Integer code,String msg){
        return  new Result(code,msg);
    }

    /**
     * 设置对象
     * @param data
     * @return
     */
    public Result data(Object data){
        return this.put(DATA,data);
    }

    /**
     * 快速设置分页对象
     */
    public Result pageData(IPage<?> iPage){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("pager",new Pager(iPage));
        hashMap.put("list",iPage.getRecords());
        return data(hashMap);
    }


    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public boolean isOk(){
        return get(CODE).equals(200);
    }

    /**
     * 把自身的data对象转换为JsonString
     * @return
     */
    public Result d2Sj(){
        return d2S("hl");
    }

    public Result d2S(String format){
        if("uc".equals(format)){

            /**
             * CamelCase策略，Java对象属性：personId，序列化后属性：persionId
             *
             * PascalCase策略，Java对象属性：personId，序列化后属性：PersonId
             *
             * SnakeCase策略，Java对象属性：personId，序列化后属性：person_id
             *
             * KebabCase策略，Java对象属性：personId，序列化后属性：person-id
             */

            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;

            put(DATA,JSON.toJSONString(get(DATA), config));

        }else if("hl".equals(format)){

            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;

            put(DATA,JSON.toJSONString(get(DATA), config));
        }
        return this;
    }
}
