package com.ciel.scaproducer2.serverimpl;

import com.ciel.scaapi.crud.IScaApplicationService;
import com.ciel.scaapi.crud.UserServer;
import com.ciel.scaentity.entity.ScaApplication;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
@Service
public class UserServerImpl implements UserServer {

    @Autowired
    protected IScaApplicationService applicationService;

    @Override
    public String get(String name) {

        ScaApplication scaApplication = new ScaApplication();
        scaApplication.setName("-士大夫");
        scaApplication.setUserId(System.currentTimeMillis());

        applicationService.save(scaApplication);

        if(name.contains("exc")){
            throw new RuntimeException("EXC");
        }

        return name.concat(">>经过了producer2");
    }


}
