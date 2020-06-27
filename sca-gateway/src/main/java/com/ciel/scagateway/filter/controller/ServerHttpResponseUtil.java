package com.ciel.scagateway.filter.controller;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

public class ServerHttpResponseUtil {
    /**
     * JSON输出形式输出对象
     */
    public static Mono<Void> writeObjectJson(ServerHttpResponse response, Object object){
        return response.writeWith(Flux.create(sink -> {
 
            NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
            try {
                DataBuffer dataBuffer= nettyDataBufferFactory.wrap(JSON.toJSONStringWithDateFormat(object,JSON.DEFFAULT_DATE_FORMAT).getBytes("utf8"));
                //DataBuffer dataBuffer= nettyDataBufferFactory.wrap("asdf".getBytes("utf8"));
                sink.next(dataBuffer);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sink.complete();
        }));
    }
    /**
     * html输出
     */
    public static Mono<Void> writeHtml(ServerHttpResponse response,String html){
        return response.writeWith(Flux.create(sink -> {
 
            NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
            try {
                DataBuffer dataBuffer= nettyDataBufferFactory.wrap(html.getBytes("utf8"));
                sink.next(dataBuffer);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sink.complete();
        }));
    }
}