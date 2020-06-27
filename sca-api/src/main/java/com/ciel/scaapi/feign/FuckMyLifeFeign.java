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
    public List<String> format(String name);

    /**
     * post 调用带参数
     */
    public String posts(ScaGirls scaGirls,Long id);

    /**
     * put 调用
     */
    public String puts(ScaGirls scaGirls,Long id);

    /**
     * del 调用
     */
    public String delete(Long id,String name);
}
