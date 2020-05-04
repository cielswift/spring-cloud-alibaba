package com.ciel.scagateway.filter;

import com.alibaba.fastjson.JSON;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scacommons.jwt.JwtUtils;
import com.ciel.scacommons.serverimpl.ScaUserServiceINIT;
import com.ciel.scaentity.entity.ScaPermissions;
import com.ciel.scaentity.entity.ScaRole;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scagateway.filter.web.JsonExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Configuration
public class GlobalCustomFilter {

    @Autowired
    protected ScaUserServiceINIT userServiceINIT;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * --------------------------------------------------------------------------------------------
     * <p>
     * /**
     * 自定义异常处理[]注册Bean时依赖的Bean，会从容器中直接获取，所以直接注入即可
     *
     * @return
     */
    @Primary
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                                             ServerCodecConfigurer serverCodecConfigurer) {

        JsonExceptionHandler jsonExceptionHandler = new JsonExceptionHandler();
        jsonExceptionHandler.setViewResolvers(viewResolversProvider.getIfAvailable(Collections::emptyList));
        jsonExceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        jsonExceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        log.debug("Init Json Exception Handler Instead Default ErrorWebExceptionHandler Success");
        return jsonExceptionHandler;
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }


    /**
     * 全局过滤器
     * @return
     */
    @Bean
    @Order(-7) //执行顺序
    public GlobalFilter aFilter() {
        return (exchange, chain) -> {

            Faster.println("第1个过滤器在请求之前执行");

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (request.getURI().getPath().endsWith("/login") && HttpMethod.POST.equals(request.getMethod())) {

                System.out.println("登录请求======================");

                String username = request.getQueryParams().getFirst("username");
                String password = request.getQueryParams().getFirst("password");

                ScaUser scaUser = userServiceINIT.userByName(username);

                if (null != scaUser && bCryptPasswordEncoder().matches(password, scaUser.getPassword())) {

                    List<ScaRole> roles = userServiceINIT.rolesByuId(scaUser.getId());

                    List<ScaPermissions> scaPermissions = userServiceINIT.permissionsByuId(scaUser.getId());

                    List<String> collect = Stream.concat(roles.stream().map(n -> "ROLE_".concat(n.getName())),
                            scaPermissions.stream().map(ScaPermissions::getName)).collect(Collectors.toList());

                    HashMap<String, Object> userInfo = new HashMap<>();

                    userInfo.put("username", scaUser.getUsername());
                    userInfo.put("realname", scaUser.getUsername());
                    userInfo.put("id", scaUser.getId());
                    userInfo.put("authorites", collect);

                    String token = JwtUtils.createToken(JSON.toJSONString(userInfo));

                    response.getHeaders().set("Authentication", token);

                    userInfo.put("token", token);

                    byte[] bits = JSON.toJSONString(Result.ok("登录成功").data(userInfo)).getBytes(StandardCharsets.UTF_8);

                    DataBuffer buffer = response.bufferFactory().wrap(bits);
                    response.setStatusCode(HttpStatus.OK); //设置状态码
                    //指定编码，否则在浏览器中会中文乱码
                    response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

                    return response.writeWith(Mono.just(buffer));

                    //return response.setComplete();  //请求已经结束

                } else {

                    byte[] bits = JSON.toJSONString(Result.error("账户密码错误")).getBytes(StandardCharsets.UTF_8);

                    DataBuffer buffer = response.bufferFactory().wrap(bits);
                    response.setStatusCode(HttpStatus.OK); //设置状态码
                    //指定编码，否则在浏览器中会中文乱码
                    response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

                    return response.writeWith(Mono.just(buffer));
                }

            } else {

                String token = request.getHeaders().getFirst("Authentication");

                if (StringUtils.isEmpty(token)) {

                    byte[] bits = JSON.toJSONString(Result.error("未登录")).getBytes(StandardCharsets.UTF_8);

                    DataBuffer buffer = response.bufferFactory().wrap(bits);
                    response.setStatusCode(HttpStatus.OK); //设置状态码
                    //指定编码，否则在浏览器中会中文乱码
                    response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

                    return response.writeWith(Mono.just(buffer));
                } else {

                    try {
                        //判断token 是否需要刷新
                        boolean refresh = JwtUtils.isRefresh(token);
                        if (refresh) {
                            HashMap map =
                                    JSON.parseObject(JwtUtils.parseToken(token), HashMap.class);

                            response.getHeaders().set("Authentication", JSON.toJSONString(map));
                        }

                    } catch (Exception e) {

                        byte[] bits = JSON.toJSONString(Result.error("TOKEN 解析失败,不是合法的")).getBytes(StandardCharsets.UTF_8);

                        DataBuffer buffer = response.bufferFactory().wrap(bits);
                        response.setStatusCode(HttpStatus.OK); //设置状态码
                        //指定编码，否则在浏览器中会中文乱码
                        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

                        return response.writeWith(Mono.just(buffer));
                    }

                    return chain.filter(exchange);
                }
            }

        };
    }

}
