package com.ciel.scaproducer1.config.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer //整合oauth2 资源服务器

public class OAuth2ResourceServerJwt extends ResourceServerConfigurerAdapter {

    /**
     * ResourceServerConfigurerAdapter 内部关联了 (1)ResourceServerSecurityConfigurer 和 (2)HttpSecurity。
     * 前者与资源安全配置相关，后者与 http 安全配置相关。
     *
     * (1)创建 OAuth2AuthenticationProcessingFilter，即 OAuth2 核心过滤器。
     *  OAuth2AuthenticationProcessingFilter 提供固定的 AuthenticationManager 即 OAuth2AuthenticationManager，
     *  它并没有将 OAuth2AuthenticationManager 添加到spring的容器中,不然可能会影响spring security的普通认证流程（非 oauth2 请求），
     *  只有被 OAuth2AuthenticationProcessingFilter 拦截到的 oauth2 相关请求才被特殊的身份认证器处理。
     *
     *      OAuth2AuthenticationProcessingFilter.获取请求头里的Authorization 然后 ,
     *      OAuth2AuthenticationManager 认证(ResourceServerTokenServices解析), 然后  放到SecurityContextHolder 里
     *
     *  http://localhost:8080/order/1?access_token=950a7cc9-5a8a-42c9-a693-40e817b1a4b0
     *  唯一的身份凭证，便是这个 access_token，携带它进行访问，会进入 OAuth2AuthenticationProcessingFilter 之中，
     *  或者用请求头 Authorization =Bearer eyJhbGciOiJIUzI1NiIsIn...  一定要加 Bearer
     *
     *  OAuth2 在资源服务器端的异常处理不算特别完善，但基本够用，如果想要重写异常机制，可以直接替换掉相关的 Handler，
     *  如权限相关的 AccessDeniedHandler。具体的配置应该在 @EnableResourceServer 中被覆盖，这是适配器 + 配置器的好处。
     */

    /**
     * 检验token
     * http://127.0.0.1:3400/sso/oauth/check_token?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsic3ByaW5nY2xvdWQtcHJvZHVjZXIiXSwidXNlcl9uYW1lIjoieGlhcGVpeGluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTU4MDY2MTE1MiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9hZG1pbiIsInN5c19hZGQiXSwianRpIjoiZWU0NDFhMjAtNzJjNy00NzAwLTkyZTItYTA1ZWVlZjU1OGM3IiwiY2xpZW50X2lkIjoieGlhcGVpeGluIn0.3xIMWQOtOQ8YyQfRv01a31VXOUyjSjExCzi2LAHEhnU
     *
     * {
     *     "aud": [
     *         "springcloud-producer"
     *     ],
     *     "user_name": "xiapeixin",
     *     "scope": [
     *         "read",
     *         "write"
     *     ],
     *     "active": true,
     *     "exp": 1580661152,
     *     "authorities": [
     *         "ROLE_admin",
     *         "sys_add"
     *     ],
     *     "jti": "ee441a20-72c7-4700-92e2-a05eeef558c7",
     *     "client_id": "xiapeixin"
     * }
     *
     * http://127.0.0.1:3200/producer/user/list
     * 改header ; Authorization =Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsic3ByaW5nY2xvdWQtcHJvZHVjZXIiXSwidXNlcl9uYW1lIjoieGlhcGVpeGluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTU4MDY2MTE1MiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9hZG1pbiIsInN5c19hZGQiXSwianRpIjoiZWU0NDFhMjAtNzJjNy00NzAwLTkyZTItYTA1ZWVlZjU1OGM3IiwiY2xpZW50X2lkIjoieGlhcGVpeGluIn0.3xIMWQOtOQ8YyQfRv01a31VXOUyjSjExCzi2LAHEhnU
     *
     */


    /**
     * token 持久化策略
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("^x?ia-pe=i#xi&n!20_2$"); //密钥
        return converter;
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("sc-producer") //资源id,数据库中配置的
                .tokenStore(tokenStore()) //验证令牌服务
                .stateless(true);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {

        // http.addFilter()// 可以进行添加控制器之类的骚操作;

        http
                .authorizeRequests()
                //.antMatchers("/mother_fuck/**").permitAll()

                .antMatchers(HttpMethod.GET, "/ac/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.POST, "/ac/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PUT, "/ac/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE, "/ac/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.OPTIONS, "/ac/**").access("#oauth2.hasScope('write')")

                .antMatchers("/order/**").authenticated()  // 配置 order 访问控制，必须认证过后才可以访问

                .anyRequest().authenticated() //其他任何请求都要登录

                .and()
                .csrf().disable() //关闭csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //不记录session
                .and()
                .headers().addHeaderWriter((req, resp) -> {
            resp.setHeader("Access-Control-Allow-Origin", "*"); //允许跨域
            if ("OPTIONS".equals(req.getMethod())) { //如果是跨域的 预请求

                resp.setHeader("Access-Control-Allow-Methods", req.getHeader("Access-Control-Request-Method"));
                resp.setHeader("Access-Control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
            }
        });

    }

}
