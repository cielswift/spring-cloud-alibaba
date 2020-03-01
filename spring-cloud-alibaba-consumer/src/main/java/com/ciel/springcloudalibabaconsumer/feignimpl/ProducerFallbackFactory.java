package com.ciel.springcloudalibabaconsumer.feignimpl;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ProducerFallbackFactory implements FallbackFactory<FuckMyLifeXiaPeiXin> {

    @Override
    public FuckMyLifeXiaPeiXin create(Throwable throwable) {
        return new FuckMyLifeXiaPeiXin() {
            @Override
            public List<String> fml(String name) {

                LinkedList<String> strings = new LinkedList<>();
                strings.add("发生了异常");
                return strings;
            }
        };
    }


}
