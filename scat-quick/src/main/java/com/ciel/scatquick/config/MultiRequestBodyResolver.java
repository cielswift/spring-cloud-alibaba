package com.ciel.scatquick.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ciel.scaapi.util.Faster;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 多RequestBody解析器
 */
public class MultiRequestBodyResolver implements HandlerMethodArgumentResolver {

    /**
     * 设置支持的方法参数类型
     * 支持带@MultiRequestBody注解的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MultiRequestBody.class); //判断参数的注解

        // return parameter.getParameterType().isAssignableFrom(ScaUser.class); //判断参数的类型
    }

    /**
     * 参数解析，利用fastjson
     * 注意：非基本类型返回null会报空指针异常，要通过反射或者JSON工具类创建一个空对象
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String jsonBody = getRequestBody(webRequest);

        JSONObject jsonObject = JSON.parseObject(jsonBody);

        // 根据@MultiRequestBody注解value作为json解析的key
        MultiRequestBody parameterAnnotation = parameter.getParameterAnnotation(MultiRequestBody.class);

        // 获取的注解后的类型
        Class<?> parameterType = parameter.getParameterType();

        Object object = JSON.parseObject(jsonBody, parameterType);

        return object;
    }

    /**
     * 获取请求体JSON字符串
     */
    private String getRequestBody(NativeWebRequest webRequest) throws IOException {
        //获取request对象
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        Object jsonMulti = servletRequest.getAttribute("json_multi");

        if (Faster.isNull(jsonMulti)) {

            StringBuilder stringBuilder = new StringBuilder();
            for (String line = servletRequest.getReader().readLine(); StringUtils.isNotEmpty(line); line = servletRequest.getReader().readLine()) {
                stringBuilder.append(line);
            }
            String body = stringBuilder.toString();
            servletRequest.setAttribute("json_multi", body);
            return body;
        }

        return jsonMulti.toString();
    }
}