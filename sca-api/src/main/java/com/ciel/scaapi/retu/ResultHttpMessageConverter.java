package com.ciel.scaapi.retu;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 响应转换为Result
 */

//@Component
public class ResultHttpMessageConverter implements HttpMessageConverter<Result> {

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return clazz.equals(Result.class);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return clazz.equals(Result.class);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
            List<MediaType> list = new ArrayList<>();
            list.add(MediaType.APPLICATION_JSON);
            return list;
    }

    @Override
    public Result read(Class<? extends Result> clazz, HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException {
        return Result.ok("OOK");
    }

    @Override
    public void write(Result result, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        System.out.println("aaa");
    }
}
