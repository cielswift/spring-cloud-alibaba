package com.ciel.scaproducer1.serverimpl;

import com.ciel.scaapi.crud.ApplicationServer;
import com.ciel.scaapi.crud.UserServer;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

/**
 * 注意, 是dubbo的service,暴漏位dubbo接口;
 */
@Service
public class ApplicationImpl implements ApplicationServer {

    /**
     * dubbo 调用另一个 dubbo
     *
     * 注意,这里如果一个dubbo调用另一个dubbo 两者一定不能使用一个spring.application.name
     * 否则报错; 因为一个名称默认是一个服务下集群下的不同实例;
     */
    @Reference
    protected UserServer userServer;

    @Override
    public String select(String name) {

        return userServer.get(name.concat(">>经过了producer1"));
    }

}
