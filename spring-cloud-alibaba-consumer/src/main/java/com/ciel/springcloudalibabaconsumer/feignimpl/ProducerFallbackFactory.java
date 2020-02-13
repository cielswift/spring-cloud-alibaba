package com.ciel.springcloudalibabaconsumer.feignimpl;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProducerFallbackFactory implements FallbackFactory<FuckMyLifeXiaPeiXin> {

    @Override
    public FuckMyLifeXiaPeiXin create(Throwable throwable) {
        return new FuckMyLifeXiaPeiXin() {
            @Override
            public List<String> fml(String name) {

                return List.of("发生了异常>>".concat(throwable.getClass().getName()));
            }
        };
    }


}
