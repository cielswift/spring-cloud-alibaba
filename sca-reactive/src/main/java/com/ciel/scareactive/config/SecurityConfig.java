package com.ciel.scareactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
 

@Configuration
/**
 * 启用webflux登陆权限校验 
 */
@EnableWebFluxSecurity
/**
 * 启用@PreAuthorize注解配置，如果不加这个注解的话，即使方法中加了@PreAuthorize也不会生效
 */
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = new String[]{"/**"};

    /**
     * 此处的代码会放在SecurityConfig类中，此处只是摘要下
     * @param http
     * @return
     */
    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        ServerHttpSecurity.FormLoginSpec formLoginSpec = http.formLogin();
        //formLoginSpec
                //.authenticationSuccessHandler(createAuthenticationSuccessHandler())
               // .authenticationFailureHandler(createAuthenticationFailureHandler());
        return formLoginSpec.and()
                .csrf().disable()
                .httpBasic().disable()
                .authorizeExchange()
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .anyExchange().authenticated()
                .and().build();
    }

}