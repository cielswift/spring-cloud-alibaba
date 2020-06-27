package com.ciel.scaproducer3.dubbos;

import com.ciel.scaapi.dubbo.GirlsServer;
import com.ciel.scaentity.entity.ScaGirls;
import org.apache.dubbo.config.annotation.Service;

@Service
public class InfoService implements GirlsServer {

    @Override
    public String fuck(String name) {
        return String.format("SERVER PRODUCER3: %d",name);
    }

    @Override
    public ScaGirls getGirls(ScaGirls scaGirls) {
        scaGirls.setName(scaGirls.getName()+ "PRODUCER3");
        return scaGirls;
    }
}
