package com.ciel.scagateway.filter.routes;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义谓词工厂
 * 要已RoutePredicateFactory结尾
 */
@Component
public class RzvRoutePredicateFactory extends AbstractRoutePredicateFactory<ConfigMar> {

    public RzvRoutePredicateFactory(Class<ConfigMar> configClass) {
        super(configClass);
    }


    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("str","end");
    }

    @Override
    public Predicate<ServerWebExchange> apply(ConfigMar config) {

        String str = config.getStr();

        String end = config.getEnd();

        return x -> str.equals(end);
    }
}
