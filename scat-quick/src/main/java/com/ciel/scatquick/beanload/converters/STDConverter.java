package com.ciel.scatquick.beanload.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 对象转换
 */
@Component
public class STDConverter implements Converter<String, LocalDateTime> {


    @Override
    public LocalDateTime convert(@NonNull String str) {

        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
