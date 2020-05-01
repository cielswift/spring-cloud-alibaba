package com.ciel.scatquick.config;

import com.ciel.scatquick.security.filter.IpFilter;
import com.ciel.scatquick.security.filter.JwtFilter;
import com.ciel.scatquick.security.filter.LoginTokenFilter;
import com.ciel.scatquick.security.provider.IPProvider;
import com.ciel.scatquick.security.provider.JwtLoginProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限注解,默认是关闭的
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    /**
     *不经过security的过滤器
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/sms/**","/qq/**");
    }

    /**
     * 加密方式
     *
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录认证处理类
     */
    @Autowired
    private JwtLoginProvider jwtProvider;

    /**
     * ip登录处理类
     */
    @Autowired
    private IPProvider ipProvider;


    // 配置ip登录端点
    @Bean
    public LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint(){
        return new LoginUrlAuthenticationEntryPoint("/ipAuth");
    }

    /**
     * 配置登录验证逻辑
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        //这里可启用我们自己的登陆验证逻辑
        auth.authenticationProvider(jwtProvider);
        //配置IP登录逻辑
        auth.authenticationProvider(ipProvider);
    }

    /**
     * 配置security的控制逻辑
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/ipLogin").permitAll() //ip登录端点
                .anyRequest().authenticated()  // 其他的需要登陆后才能访问
                .and()//未登录
                .httpBasic()//.authenticationEntryPoint(userAuthenticationEntryPointHandler)
                .and()//无权限
                .exceptionHandling()//.accessDeniedHandler(userAuthAccessDeniedHandler)
                .and()

                .formLogin().loginProcessingUrl("/login")
                .and()

                .logout()
                .logoutUrl("/logout")

                .and()

                .exceptionHandling()
                .accessDeniedPage("/ipLogin") //ip登录页面
                .authenticationEntryPoint(loginUrlAuthenticationEntryPoint())

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
        http.addFilterAt(new JwtFilter(authenticationManager()), BasicAuthenticationFilter.class);

        http.addFilterAt(new LoginTokenFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        // 注册 IpFilter  注意放置的顺序 这很关键
        http.addFilterBefore(new IpFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }
}
