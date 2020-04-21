package com.ciel.scatquick.security.filter;

import com.alibaba.fastjson.JSON;
import com.ciel.scaapi.retu.Result;
import com.ciel.scatquick.security.jwt.JWTPayload;
import com.ciel.scatquick.security.jwt.JWTUtils;
import com.ciel.scatquick.security.realm.ScaCusUser;
import com.ciel.scatquick.security.token.JwtToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LoginTokenFilter extends UsernamePasswordAuthenticationFilter {


    public LoginTokenFilter(AuthenticationManager authenticationManager) {
       setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        JwtToken jwtToken = new JwtToken(request.getParameter("username"),request.getParameter("password"));

        setDetails(request, jwtToken);

        return this.getAuthenticationManager().authenticate(jwtToken);
    }


    /**
     *优先执行
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {


        ScaCusUser scaCusUser =  (ScaCusUser)authResult.getPrincipal();
        JWTPayload payload = new JWTPayload();
        payload.setUserName(scaCusUser.getUsername());
        payload.setId(scaCusUser.getId());
        payload.setAuthority(scaCusUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        String token = JWTUtils.createToken(payload);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSON.toJSONString(Result.ok().data(token)));

        response.getWriter().close();

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSON.toJSONString(Result.error("ERROR")));

        response.getWriter().close();
    }
}
