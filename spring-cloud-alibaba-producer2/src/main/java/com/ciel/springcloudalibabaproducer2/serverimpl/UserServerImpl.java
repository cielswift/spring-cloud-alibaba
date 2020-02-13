package com.ciel.springcloudalibabaproducer2.serverimpl;

import com.ciel.springcloudalibabaapi.crud.UserServer;
import org.apache.dubbo.config.annotation.Service;

@Service
public class UserServerImpl implements UserServer {

    @Override
    public String get(String name) {
        if(name.contains("exc")){
            throw new RuntimeException("EXC");
        }

        return name.concat(">>经过了producer2");
    }

}
