package com.ciel.scaproducer3.dubbos;

import com.ciel.scaapi.crud.GirlsServer;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Service
public class InfoService implements GirlsServer {


    @Override
    public String fuck(String name) {

        return "I AM P3 FORM HELL; FUCK "+name;
    }

}
