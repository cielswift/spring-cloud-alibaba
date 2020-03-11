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
@EnableResourceServer //整合oauth2

public class OAuth2ResourceServerJwt extends ResourceServerConfigurerAdapter {

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
        converter.setSigningKey("xia123"); //密钥
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

        resources.resourceId("springcloud-producer") //资源id,数据库中配置的
                .tokenStore(tokenStore()) //验证令牌服务
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login/**","/logout/**","/favicon.ico").permitAll()

                .antMatchers(HttpMethod.GET,"/user/**").access("#oauth2.hasScope('read')")
                .antMatchers(HttpMethod.POST,"/user/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PUT,"/user/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE,"/user/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.OPTIONS,"/user/**").access("#oauth2.hasScope('write')")
                .and()
                .csrf().disable() //关闭csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //不记录session
                .and()
                .headers().addHeaderWriter((req,resp) -> {
            resp.setHeader("Access-Control-Allow-Origin","*"); //允许跨域
            if("OPTIONS".equals(req.getMethod())){ //如果是跨域的 预请求

                resp.setHeader("Access-Control-Allow-Methods",req.getHeader("Access-Control-Request-Method"));
                resp.setHeader("Access-Control-Allow-Headers",req.getHeader("Access-Control-Request-Headers"));
            }

        });
    }

}
