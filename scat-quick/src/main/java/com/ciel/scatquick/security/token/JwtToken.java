package com.ciel.scatquick.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtToken extends UsernamePasswordAuthenticationToken {

    /**
     * 第一个构造器是用于认证之前
     */
    public JwtToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    /**
     * 第二个构造器用于认证成功之后，封装认证用户的信息
     */
    public JwtToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials,authorities);
    }
}
