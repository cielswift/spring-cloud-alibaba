package com.ciel.scatquick.security.filter;

import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.JWTPayload;
import com.ciel.scaapi.util.JWTUtils;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scatquick.security.realm.ScaCusUser;
import com.ciel.scatquick.security.token.IPToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * ip登录认证器
 */
public class IpFilter extends AbstractAuthenticationProcessingFilter {

    // 使用 /ipAuth 该端点进行 ip 认证
    public IpFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/ipLogin","POST"));
        this.setAuthenticationManager(authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // 获取 host 信息
        String host = request.getRemoteHost();
        // 交给内部的 AuthenticationManager 去认证，实现解耦

        return getAuthenticationManager().authenticate(new IPToken(host));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        ScaCusUser scaCusUser =  (ScaCusUser) authResult.getPrincipal();

        JWTPayload payload = new JWTPayload();
        payload.setUserName(scaCusUser.getUsername());
        payload.setId(scaCusUser.getId());
        payload.setAuthority(scaCusUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        String token = JWTUtils.createToken(payload);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);

        ScaUser user = new ScaUser();
        user.setId(scaCusUser.getId());
        user.setUsername(scaCusUser.getUsername());
        map.put("user",user);

        Faster.respJson(Result.ok().data(map),response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        Faster.respJson(Result.error("ERROR:"+failed.getMessage()),response);
    }


}