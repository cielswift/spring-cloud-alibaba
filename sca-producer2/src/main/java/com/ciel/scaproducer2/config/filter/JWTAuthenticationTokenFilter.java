package com.ciel.scaproducer2.config.filter;

import com.alibaba.fastjson.JSON;
import com.ciel.scaproducer2.config.relm.CustomUser;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//这里不能加入spring 容器 否则成为全局过滤器

@Slf4j
public class JWTAuthenticationTokenFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     *是否登录
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /**
         *  获取请求头中JWT的Token
         */
        String token = request.getHeader("Authentication");

        /**
         * 从参数中获取token,以后删除
         */
        if(StringUtils.isEmpty(token)){
            token = request.getParameter("Authentication");
        }

        if (!StringUtils.isEmpty(token)) {
            try {
                HashMap<String, Object> map = JSON.parseObject(JwtUtils.parseToken(token), HashMap.class);

                List<String> authorites = (List<String>)map.get("authorites");

                Set<SimpleGrantedAuthority> collect =
                        authorites.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
                CustomUser customUser = new CustomUser((String)map.get("username"),
                        "",
                        collect,
                        (Long)map.get("id"),
                        (String)map.get("username"),
                        request.getRemoteHost());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(customUser, (Long)map.get("id"), collect);
                /**
                 * 放入security 上下文中
                 */
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                log.error("token 过期了");
            } catch (Exception e) {
                log.error("token 解析失败");
            }
        }
        filterChain.doFilter(request, response); //继续向下一个过滤器执行
    }

}