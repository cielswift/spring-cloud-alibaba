package com.ciel.scaapi.dubbo;

import com.ciel.scaentity.entity.ScaApplication;
import com.ciel.scaentity.entity.ScaGirls;

/**
 * dubbo远程调用接口
 */
public interface ApplicationServer {

    public ScaApplication select(ScaApplication name);

    public ScaGirls getGirls(ScaGirls girls);
}
