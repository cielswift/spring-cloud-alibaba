package com.ciel.scaconsumer.brex;

import com.ciel.scaapi.dubbo.ApplicationServer;
import com.ciel.scaentity.entity.ScaApplication;
import com.ciel.scaentity.entity.ScaGirls;

/**
 * dubbo 服务降级
 * need mock=true
 * and Mock endWith
 */
public class BackMock implements ApplicationServer {

    @Override
    public ScaApplication select(ScaApplication name) {
        ScaApplication scaApplication = new ScaApplication();
        scaApplication.setName("服务调用失败");
        return scaApplication;
    }

    @Override
    public ScaGirls getGirls(ScaGirls girls) {
        girls.setName("服务调用失败");
        return girls;
    }

}
