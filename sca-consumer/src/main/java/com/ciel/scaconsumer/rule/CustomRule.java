//package com.ciel.scaconsumer.rule;
//
//import com.netflix.client.config.IClientConfig;
//import com.netflix.loadbalancer.AbstractLoadBalancerRule;
//import com.netflix.loadbalancer.ILoadBalancer;
//import com.netflix.loadbalancer.Server;
//import org.springframework.cloud.netflix.ribbon.RibbonClient;
//import org.springframework.cloud.netflix.ribbon.RibbonClients;
//
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.LongAdder;
//
///**
// * 自定义负载均很策略
// */
//@RibbonClient
////@RibbonClients
//public class CustomRule extends AbstractLoadBalancerRule {
//
//    private LongAdder count = new LongAdder();
//
//    @Override
//    public void initWithNiwsConfig(IClientConfig iClientConfig) {
//
//    }
//
//    @Override
//    public Server choose(Object key) {
//
//        ILoadBalancer loadBalancer = getLoadBalancer();
//
//        List<Server> upList = loadBalancer.getReachableServers();
//        List<Server> allList = loadBalancer.getAllServers();
//
//        int serverCount = allList.size();
//        int upCount = upList.size();
//
//        if (serverCount == 0) {
//            return null;
//        }
//
//        if (upCount == 0) {
//            return null;
//        }
//
//        if (count.intValue() >= upList.size()) {
//            count.reset();
//        }
//
//        Server server = upList.get(count.intValue());
//        count.increment();
//
//        return server;
//    }
//
//}