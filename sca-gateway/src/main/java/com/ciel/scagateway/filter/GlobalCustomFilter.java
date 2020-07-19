package com.ciel.scagateway.filter;

import com.alibaba.fastjson.JSON;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.JWTUtils;
import com.ciel.scacommons.serverimpl.ScaUserServiceINIT;
import com.ciel.scaentity.entity.ScaPermissions;
import com.ciel.scaentity.entity.ScaRole;
import com.ciel.scaentity.entity.ScaUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
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
     * 全局过滤器
     *
     * @return
     */
    @Bean
    @Order(-7) //执行顺序
    public GlobalFilter loginJwtFilter() {
        return (exchange, chain) -> {


            Faster.println("登录检查token 过滤器 PRE 执行");

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if (request.getURI().getPath().endsWith("/login") && HttpMethod.POST.equals(request.getMethod())) {

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


                    String token = JWTUtils.createToken(JSON.toJSONString(userInfo));
                    response.getHeaders().set("Authentication", token);
                    userInfo.put("token", token);


                    byte[] bits = JSON.toJSONString(Result.ok("登录成功").data(userInfo))
                            .getBytes(StandardCharsets.UTF_8);
                    DataBuffer buffer = response.bufferFactory().wrap(bits);

                    response.setStatusCode(HttpStatus.OK); //设置状态码
                    response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);//设置格式

                    return response.writeWith(Mono.just(buffer));//请求已经结束
                } else {

                    DataBuffer buffer = response.bufferFactory()
                            .wrap(JSON.toJSONString(Result.error("账户和密码错误"))
                            .getBytes(StandardCharsets.UTF_8));

                    response.setStatusCode(HttpStatus.BAD_REQUEST); //设置状态码
                    response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

                    return response.writeWith(Mono.just(buffer));
                }
            } else {
                String token = request.getHeaders().getFirst("Authentication");

                if (StringUtils.isEmpty(token)) {
                    DataBuffer buffer = response.bufferFactory()
                            .wrap(JSON.toJSONString(Result.error("未登录")).getBytes(StandardCharsets.UTF_8));

                    response.setStatusCode(HttpStatus.BAD_REQUEST); //设置状态码
                    response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

                    return response.writeWith(Mono.just(buffer));
                } else {

                    try {
                        if (JWTUtils.isRefresh(token)) {//判断token 是否需要刷新

                            HashMap map = JSON.parseObject(JWTUtils.parseToken(token,String.class), HashMap.class);
                            response.getHeaders().set("Authentication", JSON.toJSONString(map));
                        }
                    } catch (Exception e) {


                        DataBuffer buffer = response.bufferFactory().wrap(JSON.toJSONString(Result.error("TOKEN解析失败")).
                                        getBytes(StandardCharsets.UTF_8));

                        response.setStatusCode(HttpStatus.BAD_REQUEST); //设置状态码
                        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                        return response.writeWith(Mono.just(buffer));
                    }

//                    exchange.getRequest().mutate().path() //修改请求
//                    exchange.mutate() //修改ex

                    return chain.filter(exchange).then(Mono.fromRunnable(() -> {//传递给下一个过滤器

                        Faster.println("登录检查token 过滤器 POST 执行");
                    }));
                }
            }

        };
    }

}
