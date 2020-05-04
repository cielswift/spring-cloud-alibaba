package com.ciel.scaapi.converters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import javax.management.MXBean;
import java.time.LocalDateTime;
import java.util.List;

@Configuration

@Slf4j
public class BaseProjectConfig {

    /**
     * 添加自定义转换器
     *
     * Converter的注册发生在GenericConversionService类中。也就是里面各种不同的重载方法addConverter().
     */
    @Bean
    public GenericConversionService genericConversionService(List<Converter> converters){

        //converters 为注入到容器里的2个转换器
        GenericConversionService conversionService = new GenericConversionService(); //提供类型转换服务的注册接口

        converters.forEach(conversionService::addConverter);
        return conversionService;
    }

//    Converter的注册发生在GenericConversionService类中。也就是里面各种不同的重载方法addConverter().
//    Formatter的注册发生在FormattingConversionService类中。也就是里面各种不同的addFormatterXXX()方法。

    @Bean
    public FormattingConversionService formattingConversionService(List<Formatter> formatters){

        FormattingConversionService formatCS = new FormattingConversionService();

        //FormattingConversionService formattirvice = new DefaultFormattingConversionService();

        formatters.forEach(formatCS::addFormatter);
        return formatCS;
    }


/**----------------------------------------------------------------------------------------------------------*/

    @Autowired
    protected ConversionService conversionService; //提供判断类型之间是否可以转换，以及转换方法

    protected void demo(){

        LocalDateTime convert = conversionService.convert("2019-11-20 15:23:20", LocalDateTime.class);
    }

}
