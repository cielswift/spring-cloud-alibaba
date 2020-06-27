package com.ciel.scaproducer2.serverimpl;

import com.ciel.scaapi.crud.IScaApplicationService;
import com.ciel.scaapi.dubbo.UserServer;
import com.ciel.scaentity.entity.ScaApplication;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
@Service
public class UserServerImpl implements UserServer {

    @Override
    public String get(String name) {

        ScaApplication scaApplication = new ScaApplication();
        scaApplication.setName("A=P=P");
        scaApplication.setUserId(System.currentTimeMillis());

        if(name.contains("exc")){
            throw new RuntimeException("EXC");
        }

        return name.concat(" PRODUCER2");
    }


}
