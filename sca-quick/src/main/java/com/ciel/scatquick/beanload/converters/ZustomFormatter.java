package com.ciel.scatquick.beanload.converters;

import com.ciel.scaapi.util.Faster;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Formatter SPI用于实现格式化逻辑
 *
 * Format : Printer接口实现 T -> String,而Parser接口实现 String -> T.
 * Converter : 而Converter接口是实现 S -> T，从任意对象转换成任意对象。
 *
 * mvc controller 参数转换
 *
 */
@Component
public class ZustomFormatter implements Formatter<Date> {

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        return Faster.parse(text);
    }

    @Override
    public String print(Date object, Locale locale) {
        return Faster.format(object);
    }
}
