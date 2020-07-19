package com.ciel.scaapi.feign;

import com.ciel.scaentity.entity.ScaGirls;

import java.util.List;

/**
 * feign调用
 */
public interface FuckMyLifeFeign {

    /**
     * get 调用 带参数
     */
     List<String> format(String name);
    /**
     * 传递请求头
     */
     String head(String token);
    /**
     * get 请求传递对象
     */
     String getQueryMap(ScaGirls scaGirls);

    /**
     * post 调用带参数
     */
     String posts(ScaGirls scaGirls,Long id);

    /**
     * put 调用
     */
     String puts(ScaGirls scaGirls,Long id);

    /**
     * del 调用
     */
     String delete(Long id,String name);
}
