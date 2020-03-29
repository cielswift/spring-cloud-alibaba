package com.ciel.scaapi.converters;

import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Formatter SPI用于实现格式化逻辑
 *
 * Format : Printer接口实现 T -> String,而Parser接口实现 String -> T.
 * Converter : 而Converter接口是实现 S -> T，从任意对象转换成任意对象。
 *
 */
@Component
public class CustomFormatter implements Formatter<String> {

    @Override
    public String parse(@NonNull String s, Locale locale) throws ParseException {

        return "fuck2";
    }

    @Override
    public String print(@NonNull String s, Locale locale) {

        return "fuck1";
    }
}