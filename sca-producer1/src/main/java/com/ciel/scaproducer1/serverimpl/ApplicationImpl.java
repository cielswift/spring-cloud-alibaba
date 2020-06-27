package com.ciel.scaproducer1.serverimpl;

import com.ciel.scaapi.dubbo.ApplicationServer;
import com.ciel.scaapi.dubbo.GirlsServer;
import com.ciel.scaapi.dubbo.UserServer;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaentity.entity.ScaApplication;
import com.ciel.scaentity.entity.ScaGirls;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

import java.math.BigDecimal;

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

    @Reference
    protected GirlsServer girlsServer;

    @Override
    public ScaApplication select(ScaApplication name) {
        name.setName(name.getName() + " PRODUCER1");
        return name;
    }


    @Override
    public ScaGirls getGirls(ScaGirls girls) {
        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setName("刘学文");
        scaGirls.setPrice(new BigDecimal("20.55"));
        scaGirls.setBirthday(Faster.now());

        //dubbo 调用另一个 dubbo
        ScaGirls scaGirls1 = girlsServer.getGirls(scaGirls);
        return scaGirls1;
    }
}
