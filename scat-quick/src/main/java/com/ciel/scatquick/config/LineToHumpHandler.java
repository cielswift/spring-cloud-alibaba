package com.ciel.scatquick.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class LineToHumpHandler implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LineConvertHump.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {


        Class<?> type = parameter.getParameterType();
        Object instance = type.getConstructor().newInstance();

        Map<String, String[]> parameterMap =
                ((ServletWebRequest) webRequest).getRequest().getParameterMap();

        for (Map.Entry<String, String[]> pms : parameterMap.entrySet()){

            String key = pms.getKey();
            String hump = StringHelpers.underlineToHump(key);

            Field[] declaredFields = type.getDeclaredFields();

            for(Field f: declaredFields){
                f.setAccessible(true);

                if(f.getName().equals(hump)){
                    if(f.getType().isInstance(Collection.class)){
                        f.set(instance, Arrays.asList(pms.getValue()));
                    }else{
                        f.set(instance,pms.getValue()[0]);
                    }
                }
            }
        }

        return instance;
    }


}