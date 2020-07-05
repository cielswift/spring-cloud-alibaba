package com.ciel.scaconsumer.brex;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 降级和异常处理通用
 */
@Slf4j
public final class BlockAndException {
    private BlockAndException(){}
    public static final  Map<String, String> EXCEPTION_NAME  = new HashMap<>(1<<3);

    static{
        EXCEPTION_NAME.put("com.alibaba.csp.sentinel.slots.block.flow.FlowException","流控异常(SENTINEL触发)");
        EXCEPTION_NAME.put("com.alibaba.csp.sentinel.slots.block.degrade.DegradeException","熔断降级异常(SENTINEL触发)");
        EXCEPTION_NAME.put("com.alibaba.csp.sentinel.slots.system.SystemBlockException","系统保护异常(SENTINEL触发)");
        EXCEPTION_NAME.put("com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException","热点参数限流异常(SENTINEL触发)");
    }

    public static Result block(BlockException be){

        return Result.error(exceptionType(be)).data(getMessage(be));
    }

    public static Result exception(Throwable throwable){
        return Result.error("发生异常(SENTINEL触发)").data(getMessage(throwable));
    }

    public static Result def(Throwable throwable){
        return Result.error("发生异常(SENTINEL触发)").data(getMessage(throwable));
    }

    public static String exceptionType(BlockException be){
        String type = EXCEPTION_NAME.get(be.getClass().getName());
        return Faster.isNull(type)?("未知类型:"+be.getClass()):type;
    }

    public static String getMessage(Throwable throwable){
        if(Faster.isNull(throwable)){
            return "null";
        }
        return String.format("EXCEPTION: %s, MESSAGE: %s",throwable.getClass().getName(),throwable.getMessage());
    }

}
