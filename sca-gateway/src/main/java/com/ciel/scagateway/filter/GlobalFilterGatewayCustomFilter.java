package com.ciel.scagateway.filter;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webflux.SentinelWebFluxFilter;
import com.alibaba.csp.sentinel.adapter.spring.webflux.exception.SentinelBlockExceptionHandler;
import com.alibaba.fastjson.JSONObject;
import com.ciel.scaapi.crud.IScaUserService;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
public class GlobalFilterGatewayCustomFilter {

    @Autowired
    protected ScaUserServiceINIT userServiceINIT;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //整合Sentinel 限流相关
    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GlobalFilterGatewayCustomFilter(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(-2)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

/**--------------------------------------------------------------------------------------------

    /**
     * 自定义异常处理[@@]注册Bean时依赖的Bean，会从容器中直接获取，所以直接注入即可
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
     * 全局过滤器
     */

    @Bean
    @Order(-7)
    public GlobalFilter a() {
        return (exchange, chain) -> {

            System.out.println("第1个过滤器在请求之前执行");

            ServerHttpRequest request = exchange.getRequest();

            if(request.getURI().getPath().endsWith("/login") && HttpMethod.POST.equals(request.getMethod())){

                String username = request.getQueryParams().getFirst("username");
                String password = request.getQueryParams().getFirst("password");

                if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){

                    ScaUser scaUser = userServiceINIT.userByName(username);

                    if(bCryptPasswordEncoder().matches(password,scaUser.getPassword())){

                        List<ScaRole> roles = userServiceINIT.rolesByuId(scaUser.getId());

                        List<ScaPermissions> scaPermissions = userServiceINIT.permissionsByuId(scaUser.getId());



                    }
                }else{

                    //密码错误
                }

            }




           // List<String> authentication = exchange.getRequest().getQueryParams().get("Authentication");

            List<String> token = exchange.getRequest().getHeaders().get("Authentication");

            if ((null != token && !token.isEmpty()) || true) { //故意放行

                return chain.filter(exchange).then(Mono.fromRunnable(() -> {

                    System.out.println("第1个过滤器在请求之后执行");
                }));

            } else {

                ServerHttpResponse response = exchange.getResponse();
                JSONObject message = new JSONObject();

                message.put("status", -1);
                message.put("data", "鉴权失败");

                byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);

                DataBuffer buffer = response.bufferFactory().wrap(bits);
                response.setStatusCode(HttpStatus.UNAUTHORIZED); //设置状态码
                //指定编码，否则在浏览器中会中文乱码
                response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
                return response.writeWith(Mono.just(buffer));

                //return response.setComplete();  //请求已经结束
            }
        };
    }


    @Bean
    @Order(1)
    public GlobalFilter b() {

        return (exchange, chain) -> {
            System.out.println("第2个过滤器在请求之前执行");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

                System.out.println("第2个过滤器在请求之后执行");

            }));
        };

    }
}
