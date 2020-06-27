package com.ciel.scaconsumer.feignext;

import com.ciel.scaentity.entity.ScaGirls;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * feign调用异常处理
 */
@Component
public class ProducerFallbackFactory implements FallbackFactory<FuckMyLifeXiaPeiXin> {

    @Override
    public FuckMyLifeXiaPeiXin create(Throwable throwable) {
        return new FuckMyLifeXiaPeiXin() {
            @Override
            public List<String> format(String name) {

                LinkedList<String> strings = new LinkedList<>();
                strings.add("get 异常");
                return strings;
            }

            @Override
            public String posts(ScaGirls scaGirls, Long id) {
                return "post 异常";
            }

            @Override
            public String puts(ScaGirls scaGirls, Long id) {
                return "put 异常";
            }

            @Override
            public String delete(Long id, String name) {
                return "delete 异常";
            }
        };
    }


}
