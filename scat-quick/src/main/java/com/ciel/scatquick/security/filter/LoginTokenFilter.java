package com.ciel.scatquick.security.filter;

import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.JWTPayload;
import com.ciel.scaapi.util.JWTUtils;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scatquick.security.realm.ScaCusUser;
import com.ciel.scatquick.security.token.LoginToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 登录过滤器
 */
public class LoginTokenFilter extends UsernamePasswordAuthenticationFilter {

    public LoginTokenFilter(AuthenticationManager authenticationManager) {
       setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        LoginToken jwtToken = new LoginToken(request.getParameter("username"),
                request.getParameter("password"),
                request.getParameter("code"));
        setDetails(request, jwtToken);

        return this.getAuthenticationManager().authenticate(jwtToken);
    }

    /**
     *优先执行 成功后返回
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        ScaCusUser scaUser = (ScaCusUser)authResult.getPrincipal();

        JWTPayload payload = new JWTPayload();
        payload.setUserName(scaUser.getUsername());
        payload.setId(scaUser.getId());
        payload.setAuthority(scaUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        String token = JWTUtils.createToken(payload);

        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);

        ScaUser user = new ScaUser();
        user.setId(scaUser.getId());
        user.setUsername(scaUser.getUsername());
        map.put("user",user);

        Faster.respJson(Result.ok().data(map),response);
    }

    /**
     * 失败后返回
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        Faster.respJson(Result.error("ERROR:"+failed.getMessage()),response);
    }
}
