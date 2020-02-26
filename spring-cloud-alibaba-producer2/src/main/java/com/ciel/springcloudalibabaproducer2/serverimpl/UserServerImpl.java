package com.ciel.springcloudalibabaproducer2.serverimpl;

import com.ciel.springcloudalibabaapi.crud.IScaApplicationService;
import com.ciel.springcloudalibabaapi.crud.UserServer;
import com.ciel.springcloudalibabaentity.entity.ScaApplication;
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
        scaApplication.setName("刘学文");
        scaApplication.setUserId(System.currentTimeMillis());

        applicationService.save(scaApplication);

        if(name.contains("exc")){
            throw new RuntimeException("EXC");
        }

        return name.concat(">>经过了producer2");
    }


}
