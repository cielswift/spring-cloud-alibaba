package com.ciel.scaconsumer.config;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * restTemplate 拦截器
 */
public class RestInterceptor implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        request.getHeaders().add("token","aa");
        return execution.execute(request,body);
    }
}
