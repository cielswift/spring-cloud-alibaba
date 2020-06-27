package com.ciel.scaapi.dubbo;

import com.ciel.scaentity.entity.ScaGirls;

/**
 * dubbo远程调用接口
 */
public interface GirlsServer {

    public String fuck(String name);

    public ScaGirls getGirls(ScaGirls scaGirls);
}
