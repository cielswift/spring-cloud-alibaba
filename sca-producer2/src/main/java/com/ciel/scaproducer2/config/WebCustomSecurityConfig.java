package com.ciel.scaproducer2.config;

import com.ciel.scaproducer2.config.filter.JWTAuthenticationTokenFilter;
import com.ciel.scaproducer2.config.haldel.UserAuthAccessDeniedHandler;
import com.ciel.scaproducer2.config.haldel.UserAuthenticationEntryPointHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限注解,默认是关闭的

@AllArgsConstructor
public class WebCustomSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 未登录
     */
    protected UserAuthenticationEntryPointHandler userAuthenticationEntryPointHandler;

    /**
     * 无权限
     */
    protected UserAuthAccessDeniedHandler userAuthAccessDeniedHandler;

    /**
     * 密码认证器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 获取manager,整合oauth2需要这个对象
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     *不经过security的过滤器
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/fuck_you_mother/**");
    }

    /**
     * 配置security的控制逻辑
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 其他的需要登陆后才能访问
                .anyRequest().authenticated()
                .and()//未登录
                .httpBasic().authenticationEntryPoint(userAuthenticationEntryPointHandler)
                .and()//无权限
                .exceptionHandling().accessDeniedHandler(userAuthAccessDeniedHandler)
                .and()
                // 开启跨域
                .cors()
                .and()
                // 取消跨站请求伪造防护
                .csrf().disable();

        // 基于Token不需要session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        // 添加JWT过滤器
        http.addFilter(new JWTAuthenticationTokenFilter(authenticationManager()));

    }

}
