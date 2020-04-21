package com.ciel.scatquick.security.filter;

import com.ciel.scatquick.security.jwt.JWTPayload;
import com.ciel.scatquick.security.jwt.JWTUtils;
import com.ciel.scatquick.security.realm.ScaCusUser;
import com.ciel.scatquick.security.token.JwtToken;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
public class JwtFilter extends BasicAuthenticationFilter {

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        /**
         *  获取请求头中JWT的Token
         */
        String token = request.getHeader("Authentication");

        if (!StringUtils.isEmpty(token)) {
            try {

                JWTPayload user = JWTUtils.parseToken(token, JWTPayload.class);

                ScaCusUser cusUser = new ScaCusUser(user.getUserName(),
                                                   "",
                        user.getAuthority().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                        user.getId(),
                        user.getRealName(),
                        user.getIp());

                JwtToken jwtToken = new JwtToken(cusUser,"",cusUser.getAuthorities());
                /**
                 * 放入security 上下文中
                 */
                SecurityContextHolder.getContext().setAuthentication(jwtToken);

            } catch (ExpiredJwtException e) {
                log.error("TOKEN过期");
            } catch (Exception e) {
                log.error("TOKEN解析失败");
            }
        }
        chain.doFilter(request, response); //继续向下一个过滤器执行
    }
}
